package com.SmartClassOllama.SmartClass.Scheduler.controller;

import com.SmartClassOllama.SmartClass.Scheduler.Dto.PeriodDTO;

import com.SmartClassOllama.SmartClass.Scheduler.services.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    @PostMapping("/generate")
    public Map<String, List<PeriodDTO>> generateTimetable() {
        return timetableService.generateFromDB();
    }


}

