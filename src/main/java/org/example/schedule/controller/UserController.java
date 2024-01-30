package org.example.schedule.controller;

import jakarta.validation.Valid;
import org.example.schedule.dto.UserRequestDto;
import org.example.schedule.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //유저 생성
    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserRequestDto userRequestDto){
        userService.addUser(userRequestDto);

        return ResponseEntity.ok().body("생성되었습니다.");
    }

}
