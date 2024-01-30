package org.example.schedule.dto;

import lombok.Getter;
import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.User;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private String titleSchedule;
    private String bodySchedule;
    private User user;
    private LocalDateTime createdAt;

    //객체 생성시 가지고 있는 정보를 입력해줌
    public ScheduleResponseDto(Schedule schedule, User user) {
        this.titleSchedule = schedule.getTitleSchedule();
        this.bodySchedule = schedule.getBodySchedule();
        this.createdAt = schedule.getCreatedAt();
        this.user = user;
    }
}
