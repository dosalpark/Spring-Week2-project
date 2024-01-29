package org.example.schedule.dto;

import lombok.Getter;

@Getter
public class PwCheckScheduleRequestDto {
    //일정 삭제 때 유저에게 패스워드만 입력받음
    private String password;
}
