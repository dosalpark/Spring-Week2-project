package org.example.schedule.dto;

import lombok.Getter;
import org.example.schedule.entity.User;

@Getter
public class AddScheduleRequestDto {
    //일정 입력시 유저한테 입력받음
    private String titleSchedule;
    private String bodySchedule;
    private User user;

}
