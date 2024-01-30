package org.example.schedule.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.example.schedule.dto.*;
import org.example.schedule.entity.User;
import org.example.schedule.security.UserDetailsImpl;
import org.example.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    //공통으로 사용할 Service 필드 선언 및 생성자
    final private ScheduleService scheduleService;
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    //할일카드 등록
    @PostMapping("/schedule")
    public AddScheduleResponseDto createSchedule(@RequestBody AddScheduleRequestDto addScheduleRequestDto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        //서비스로 넘겨줘야 함
        return scheduleService.createSchedule(addScheduleRequestDto, userDetails.getUser());
    }

    //모든 할일카드 조회
    @GetMapping("/schedule")
    public List<AllScheduleResponseDto> getSchedule(){
        return scheduleService.getSchedule();

    }

    //선택한 할일카드 조회
    @GetMapping("/schedule/{id}")
    public ChoiceScheduleResponseDto getChoiceSchedule(@PathVariable Long id){
        return scheduleService.getChoiceSchedule(id);
    }

    //선택한 할일카드 수정 OK
    @PutMapping("/schedule/{id}")
    //수정시 password로 확인해야해서 @RequestBody사용해서 URL에서 안보이게 처리
    public Optional<?> updateSchedule(@PathVariable Long id,
                                   HttpServletRequest httpServletRequest,
                                   @RequestBody UpdateScheduleRequestDto updateScheduleRequestDto,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        return scheduleService.updateSchedule(id, httpServletRequest, updateScheduleRequestDto, userDetails);

    }
}
