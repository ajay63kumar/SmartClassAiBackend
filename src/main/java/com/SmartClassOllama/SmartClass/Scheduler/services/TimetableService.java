package com.SmartClassOllama.SmartClass.Scheduler.services;

import com.SmartClassOllama.SmartClass.Scheduler.Dto.PeriodDTO;
import com.SmartClassOllama.SmartClass.Scheduler.Dto.TimetableRequestDTO;
import com.SmartClassOllama.SmartClass.Scheduler.entity.Room;
import com.SmartClassOllama.SmartClass.Scheduler.entity.Teacher;
import com.SmartClassOllama.SmartClass.Scheduler.repository.RoomRepository;
import com.SmartClassOllama.SmartClass.Scheduler.repository.TeacherRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimetableService {

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private ChatClient.Builder builder;


    public Map<String, List<PeriodDTO>> generateFromDB() {

        ChatClient chatClient = builder.build();

        List<Teacher> teachers = teacherRepo.findAll();
        List<Room> rooms = roomRepo.findAll();

        if (teachers.isEmpty() || rooms.isEmpty()) {
            throw new RuntimeException("Teachers or Rooms data missing");
        }

        List<String> teacherNames = teachers.stream().map(Teacher::getName).toList();
        List<String> roomNames = rooms.stream().map(Room::getRoomName).toList();

        List<String> subjects = teachers.stream()
                .map(Teacher::getSubject)
                .distinct()
                .toList();

        Map<String, String> map = new HashMap<>();
        for (Teacher t : teachers) {
            map.put(t.getName(), t.getSubject());
        }

        TimetableRequestDTO dto = new TimetableRequestDTO();
        dto.setTeachers(teacherNames);
        dto.setRooms(roomNames);
        dto.setSubjects(subjects);
        dto.setTeacherSubjectMap(map);
        dto.setDays(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));
        dto.setPeriodsPerDay(6);

        String prompt = buildPrompt(dto);

        ObjectMapper mapper = new ObjectMapper();

        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("🚀 AI Attempt: " + attempt);

                // 🔥 STEP 1: RAW response
                String response = chatClient
                        .prompt(prompt)
                        .call()
                        .content();

                System.out.println("🔥 RAW RESPONSE:\n" + response);

                // 🔥 STEP 2: Clean markdown
                response = response
                        .replace("```json", "")
                        .replace("```", "")
                        .trim();

                // 🔥 STEP 3: Extract JSON (object or array safe)
                int startObj = response.indexOf("{");
                int endObj = response.lastIndexOf("}");

                int startArr = response.indexOf("[");
                int endArr = response.lastIndexOf("]");

                if (startObj != -1 && endObj != -1) {
                    response = response.substring(startObj, endObj + 1);
                } else if (startArr != -1 && endArr != -1) {
                    response = response.substring(startArr, endArr + 1);
                } else {
                    throw new RuntimeException("No valid JSON found");
                }

                System.out.println("✅ CLEANED JSON:\n" + response);

                // 🔥 STEP 4: Validate JSON
                mapper.readTree(response);

                // 🔥 STEP 5: Convert
                // 🔥 STEP 5: Convert JSON → Object
                Map<String, List<PeriodDTO>> result = mapper.readValue(
                        response,
                        new TypeReference<Map<String, List<PeriodDTO>>>() {}
                );

// 🔥 STEP 6: Ensure all days exist (VERY IMPORTANT)
                for (String day : List.of("Monday","Tuesday","Wednesday","Thursday","Friday")) {
                    result.putIfAbsent(day, List.of());
                }

// ✅ RETURN SAFE DATA
                return result;

//            } catch (Exception e) {
//                System.out.println("❌ Attempt " + attempt + " failed: " + e.getMessage());
//
//                if (attempt == maxRetries) {
//                    throw new RuntimeException("AI failed after 3 attempts.\nLast error: " + e.getMessage());
//                }
//
//                System.out.println("🔁 Retrying...\n");
//            }
            } catch (Exception e) {
                System.out.println("❌ Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == maxRetries) {
                    throw new RuntimeException("AI failed after 3 attempts.\nLast error: " + e.getMessage());
                }

                try {
                    System.out.println("⏳ Waiting for rate limit reset...");
                    Thread.sleep(6000); // 🔥 ADD HERE
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("🔁 Retrying...\n");
            }
        }

        throw new RuntimeException("Unexpected error");
    }

    private String buildPrompt(TimetableRequestDTO request) {

        return "Generate a FULL WEEK school timetable in STRICT JSON format.\n\n" +

                "IMPORTANT:\n" +
                "1. Return ONLY valid JSON\n" +
                "2. No explanation\n" +
                "3. No extra text\n" +
                "4. No markdown\n" +
                "5. JSON must be COMPLETE and CLOSED\n\n" +

                "DAYS (MUST INCLUDE ALL):\n" +
                request.getDays() + "\n\n" +

                "DATA:\n" +
                "Teachers: " + request.getTeachers() + "\n" +
                "Rooms: " + request.getRooms() + "\n" +
                "Teacher-Subject Map: " + request.getTeacherSubjectMap() + "\n" +
                "Periods per day: " + request.getPeriodsPerDay() + "\n\n" +

                "CRITICAL RULES:\n" +
                "- Generate ALL days (Monday to Friday)\n" +
                "- Each day must have EXACTLY " + request.getPeriodsPerDay() + " periods\n" +
                "- Each period must contain classes equal to number of rooms\n" +
                "- Use ALL rooms in every period\n" +
                "- Each class must have subject, teacher, room\n" +
                "- A teacher cannot repeat in same period\n" +
                "- A room cannot repeat in same period\n" +
                "- Assign teachers only to their subjects\n" +
                "- Ensure DIFFERENT schedules across days\n\n" +

                "STRICT JSON FORMAT:\n" +
                "{\n" +
                "  \"Monday\": [\n" +
                "    {\n" +
                "      \"period\": 1,\n" +
                "      \"classes\": [\n" +
                "        {\"subject\":\"Math\",\"teacher\":\"A\",\"room\":\"R1\"},\n" +
                "        {\"subject\":\"English\",\"teacher\":\"B\",\"room\":\"R2\"}\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"period\": 2,\n" +
                "      \"classes\": [\n" +
                "        {\"subject\":\"Physics\",\"teacher\":\"C\",\"room\":\"R1\"}\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +

                "  \"Tuesday\": [],\n" +
                "  \"Wednesday\": [],\n" +
                "  \"Thursday\": [],\n" +
                "  \"Friday\": []\n" +
                "}";
    }
}




