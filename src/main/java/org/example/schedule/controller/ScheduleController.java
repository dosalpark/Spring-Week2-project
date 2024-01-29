package org.example.schedule.controller;


import org.example.schedule.dto.ScheduleRequestDto;
import org.example.schedule.dto.ScheduleResponseDto;
import org.example.schedule.dto.UpdateScheduleRequestDto;
import org.example.schedule.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    //공통으로 사용할 Service 필드 선언 및 생성자
    final private ScheduleService scheduleService;
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    //일정 등록
    //입력되는 정보에 PW 포함되어있어 @RequestBody 사용
    @PostMapping("/schedule")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto){
        //서비스로 넘겨줘야 함
        return scheduleService.createSchedule(scheduleRequestDto);
    }

//    //담당자 이름으로 일정 조회
//    @GetMapping("/schedule/username")
//    public List<ScheduleResponseDto> getScheduleByUser(@RequestParam String username){
//        return scheduleService.getScheduleByUser(username);
//
//    }

    //모든 일정 조회
    @GetMapping("/schedule")
    public List<ScheduleResponseDto> getSchedule(){
        return scheduleService.getSchedule();

    }

    //단건 일정 조회
    @GetMapping("/schedule/select/{id}")
    public ScheduleResponseDto getChoiceSchedule(@PathVariable Long id){
        return scheduleService.getChoiceSchedule(id);
    }

    //선택 일정 수정 OK
//    @PutMapping("/schedule/{id}")
//    //수정시 password로 확인해야해서 @RequestBody사용해서 URL에서 안보이게 처리
//    public ScheduleResponseDto updateSchedule(@PathVariable Long id, @RequestBody UpdateScheduleRequestDto updateScheduleRequestDto){
//        return scheduleService.updateSchedule(id, updateScheduleRequestDto);
//    }
}
