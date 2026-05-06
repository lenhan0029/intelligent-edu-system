package com.edu.analytics.repository;

import com.edu.analytics.entity.AnalyticsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<AnalyticsRecord, Long> {
    List<AnalyticsRecord> findByStudentId(String studentId);
    List<AnalyticsRecord> findByOrganizationId(Long organizationId);
}
