package org.example.schedule.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    //4~10자 a-z,0-9 사용
    @Pattern(regexp = "^[0-9a-z]{4,10}$")
    private String username;

    //8~15자 a-z,A-Z,0-9 사용
    @Pattern(regexp = "^[0-9a-zA-Z]{8,15}$")
    private String password;

}
