package com.SmartClassOllama.SmartClass.Scheduler.repository;

import com.SmartClassOllama.SmartClass.Scheduler.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    ClassEntity findByClassName(String className);
}
