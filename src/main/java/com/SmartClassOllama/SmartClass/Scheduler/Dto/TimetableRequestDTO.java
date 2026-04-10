package com.SmartClassOllama.SmartClass.Scheduler.Dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class TimetableRequestDTO {

    private List<String> teachers;
    private List<String> subjects;
    private List<String> rooms;
    private Map<String, String> teacherSubjectMap;
    private List<String> days;
    private int periodsPerDay;

    // getters setters
}
