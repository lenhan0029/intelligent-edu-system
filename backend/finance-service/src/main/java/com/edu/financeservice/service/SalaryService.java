package com.edu.financeservice.service;

import com.edu.financeservice.entity.Salary;
import com.edu.financeservice.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final SalaryRepository salaryRepository;

    public List<Salary> getTeacherSalaries(Long teacherId) {
        return salaryRepository.findByTeacherId(teacherId);
    }

    @Transactional
    public Salary paySalary(Long teacherId, java.math.BigDecimal amount, String period) {
        Salary salary = Salary.builder()
                .teacherId(teacherId)
                .amount(amount)
                .period(period)
                .paidAt(new Date())
                .build();
        return salaryRepository.save(salary);
    }
}
