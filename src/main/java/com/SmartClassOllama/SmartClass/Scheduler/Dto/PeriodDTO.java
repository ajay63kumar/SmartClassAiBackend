package com.SmartClassOllama.SmartClass.Scheduler.Dto;

import lombok.Data;

import java.util.List;

@Data
public class PeriodDTO {
    private int period;
    private List<ClassScheduleDTO> classes; // 🔥 KEY CHANGE
    private String time; // optional
}

