package org.example.schedule.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.schedule.dto.UserRequestDto;
import org.example.schedule.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //유저 생성
    @PostMapping("/user/signup")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserRequestDto userRequestDto, BindingResult bindingResult) {
        //valid 예외처리
        //BindingResult 를 통해서 에러사항을 리스트에 저장
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        //에러가 하나라도 있으면 에러메세지를 보여주고 실패
        if (fieldErrors.size() > 0) {
            for (FieldError error : fieldErrors) {
                log.error(error.getField() + "필드 : " + error.getDefaultMessage());
            }
            return ResponseEntity.status(500).body("에러가 발생했습니다..");
        }
        //userService에서 받아온 값을 넘겨줌
        return userService.addUser(userRequestDto);
    }

//    @PostMapping("/user/login")
//    public ResponseEntity<String> loginUser(@RequestBody UserRequestDto userRequestDto){
//        return userService.loginUser(userRequestDto);
//    }

}
