package org.example.schedule.dto;

import lombok.Getter;
import org.example.schedule.entity.Schedule;

import java.time.LocalDateTime;

@Getter
public class UpdateScheduleResponseDto {
    private String titleSchedule;
    private String bodySchedule;
    private String username;
    private LocalDateTime createdAt;


    //객체 생성시 가지고 있는 정보를 입력해줌
    public UpdateScheduleResponseDto(Schedule schedule) {
        this.titleSchedule = schedule.getTitleSchedule();
        this.bodySchedule = schedule.getBodySchedule();
        this.username = schedule.getUser().getUsername();
        this.createdAt = schedule.getCreatedAt();
    }
}
