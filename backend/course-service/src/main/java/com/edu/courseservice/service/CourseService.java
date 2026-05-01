package com.edu.courseservice.service;

import com.edu.courseservice.entity.Course;
import com.edu.courseservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository repository;

    public List<Course> getAll() {
        return repository.findAll();
    }

    public Course getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course save(Course course) {
        return repository.save(course);
    }
}
