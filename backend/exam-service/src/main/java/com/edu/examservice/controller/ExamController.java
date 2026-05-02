package com.edu.examservice.controller;

import com.edu.examservice.entity.Exam;
import com.edu.examservice.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService service;

    @GetMapping
    public List<Exam> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Exam getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPERADMIN')")
    public Exam create(@RequestBody Exam entity) {
        return service.save(entity);
    }
}
