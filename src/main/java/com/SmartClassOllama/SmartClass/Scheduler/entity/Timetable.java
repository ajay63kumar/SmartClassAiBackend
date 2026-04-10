package com.SmartClassOllama.SmartClass.Scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String timetableJson;

    private LocalDateTime createdAt;
}
