package org.example.schedule.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.example.schedule.dto.*;
import org.example.schedule.entity.Schedule;
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
    @GetMapping("/schedule/{scheduleId}")
    public ChoiceScheduleResponseDto getChoiceSchedule(@PathVariable Long scheduleId){
        return scheduleService.getChoiceSchedule(scheduleId);
    }

    //선택한 할일카드 수정
    @PutMapping("/schedule/{scheduleId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long scheduleId,
                                                   HttpServletRequest httpServletRequest,
                                                   @RequestBody UpdateScheduleRequestDto updateScheduleRequestDto,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        return scheduleService.updateSchedule(scheduleId, httpServletRequest, updateScheduleRequestDto, userDetails);

    }

    //선택한 할일카드 완료처리
    @PutMapping("/schedule/clear/{scheduleId}")
    public void clearSchedule(@PathVariable Long scheduleId,
                                            HttpServletRequest httpServletRequest,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        scheduleService.clearSchedule(scheduleId, httpServletRequest, userDetails);

    }
}
