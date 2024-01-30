package org.example.schedule.dto;

import lombok.Getter;
import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.User;

import java.time.LocalDateTime;

@Getter
public class AllScheduleResponseDto {
    private String titleSchedule;
    private String username;
    private LocalDateTime createdAt;
    private boolean clearYn;

    //객체 생성시 가지고 있는 정보를 입력해줌
    public AllScheduleResponseDto(Schedule schedule) {
        this.titleSchedule = schedule.getTitleSchedule();
        this.username = schedule.getUser().getUsername();
        this.createdAt = schedule.getCreatedAt();
        this.clearYn = schedule.getClearYn();
    }
}
