package org.example.schedule.dto;

import lombok.Getter;

@Getter
public class UpdateScheduleRequestDto{
    //일정 수정시 유저한테 입력받음
    private String titleSchedule;
    private String bodySchedule;
    private String user;
    private String password;
}
