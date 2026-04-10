


package com.SmartClassOllama.SmartClass.Scheduler.controller;

import com.SmartClassOllama.SmartClass.Scheduler.entity.Room;
import com.SmartClassOllama.SmartClass.Scheduler.entity.Teacher;
import com.SmartClassOllama.SmartClass.Scheduler.repository.RoomRepository;
import com.SmartClassOllama.SmartClass.Scheduler.repository.TeacherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // ===================== TEACHER CRUD =====================

    @Autowired
    private TeacherRepository teacherRepo;

    // ✅ CREATE
    @PostMapping("/teacher")
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        return teacherRepo.save(teacher);
    }

    // ✅ READ ALL
    @GetMapping("/teacher")
    public List<Teacher> getAllTeachers() {
        return teacherRepo.findAll();
    }

    // ✅ READ BY ID
    @GetMapping("/teacher/{id}")
    public Teacher getTeacherById(@PathVariable Long id) {
        return teacherRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id " + id));
    }

    // ✅ UPDATE
    @PutMapping("/teacher/{id}")
    public Teacher updateTeacher(@PathVariable Long id, @RequestBody Teacher updatedTeacher) {
        Teacher teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        teacher.setName(updatedTeacher.getName());
        teacher.setSubject(updatedTeacher.getSubject());

        return teacherRepo.save(teacher);
    }

    // ✅ DELETE
    @DeleteMapping("/teacher/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherRepo.deleteById(id);
        return "Teacher deleted successfully";
    }


    // ===================== ROOM CRUD =====================

    @Autowired
    private RoomRepository roomRepo;

    // ✅ CREATE
    @PostMapping("/room")
    public Room addRoom(@RequestBody Room room) {
        return roomRepo.save(room);
    }

    // ✅ READ ALL
    @GetMapping("/room")
    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    // ✅ READ BY ID
    @GetMapping("/room/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id " + id));
    }

    // ✅ UPDATE
    @PutMapping("/room/{id}")
    public Room updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // ✅ Only update name
        room.setRoomName(updatedRoom.getRoomName());

        return roomRepo.save(room);
    }

    // ✅ DELETE
    @DeleteMapping("/room/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomRepo.deleteById(id);
        return "Room deleted successfully";
    }
}