package com.edu.courseservice.controller;

import com.edu.courseservice.entity.Course;
import com.edu.courseservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;

    @GetMapping
    public List<Course> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('TEACHER', 'CONTENT_CREATOR', 'ADMIN', 'SUPERADMIN')")
    public Course create(@RequestBody Course course) {
        return service.save(course);
    }
}
