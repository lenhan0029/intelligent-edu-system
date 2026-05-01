package com.edu.scheduleservice.config;

import com.edu.scheduleservice.entity.Schedule;
import com.edu.scheduleservice.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final ScheduleRepository scheduleRepository;

    @Override
    public void run(String... args) {
        if (scheduleRepository.count() == 0) {
            scheduleRepository.save(Schedule.builder()
                    .subject("Advanced Java")
                    .room("A101")
                    .dayOfWeek("Monday")
                    .startTime("08:00")
                    .endTime("10:00")
                    .build());

            scheduleRepository.save(Schedule.builder()
                    .subject("Angular UI")
                    .room("Lab 2")
                    .dayOfWeek("Tuesday")
                    .startTime("13:30")
                    .endTime("15:30")
                    .build());

            scheduleRepository.save(Schedule.builder()
                    .subject("Microservices")
                    .room("B204")
                    .dayOfWeek("Wednesday")
                    .startTime("10:00")
                    .endTime("12:00")
                    .build());
            
            scheduleRepository.save(Schedule.builder()
                    .subject("Database Systems")
                    .room("A101")
                    .dayOfWeek("Thursday")
                    .startTime("09:00")
                    .endTime("11:00")
                    .build());
            
            scheduleRepository.save(Schedule.builder()
                    .subject("System Design")
                    .room("Online")
                    .dayOfWeek("Friday")
                    .startTime("14:00")
                    .endTime("16:00")
                    .build());
        }
    }
}
