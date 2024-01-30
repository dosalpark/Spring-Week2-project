package org.example.schedule.dto;

import lombok.Getter;
import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.User;

import java.time.LocalDateTime;

@Getter
public class AddScheduleResponseDto {
    private String titleSchedule;
    private String bodySchedule;
    private String username;
    private LocalDateTime createdAt;

    //객체 생성시 가지고 있는 정보를 입력해줌
    public AddScheduleResponseDto(Schedule schedule, User user) {
        this.titleSchedule = schedule.getTitleSchedule();
        this.bodySchedule = schedule.getBodySchedule();
        this.createdAt = schedule.getCreatedAt();
        this.username = user.getUsername();
    }
}
