package com.edu.courseservice.repository;

import com.edu.courseservice.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    java.util.List<Enrollment> findByStudentId(Long studentId);
    java.util.List<Enrollment> findByCourse_Id(UUID courseId);
}
