package com.edu.courseservice.service;

import com.edu.courseservice.entity.Course;
import com.edu.courseservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseRecommendationService {

    private final CourseRepository courseRepository;

    public List<Course> recommendCourses(UUID studentId) {
        // Basic implementation: return most recent courses for now
        // In a real system, this would use AI/ML based on student's history
        return courseRepository.findAll().stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}
