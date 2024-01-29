package org.example.schedule.service;


import org.example.schedule.dto.ScheduleRequestDto;
import org.example.schedule.dto.ScheduleResponseDto;
import org.example.schedule.dto.UpdateScheduleRequestDto;
import org.example.schedule.entity.Schedule;
import org.example.schedule.repository.ScheduleRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {
    //공통으로 사용 할 Repository 필드 선언 및 생성자 생성
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    //일정 등록
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto) {
        //입력받은 정보로 Schedule Entity 생성
        Schedule schedule = new Schedule(scheduleRequestDto);
        //Repository 에 저장
        Schedule addSchedule = scheduleRepository.save(schedule);
        //생성한 일정을 ScheduleResponseDto에 담아서 Controller로 전달
        return new ScheduleResponseDto(addSchedule);
    }

    //선택한 일정만 조회
    public List<ScheduleResponseDto> getScheduleByUser(String username) {
        //username이 들어간 일정만 찾아서 리스트로 생성 후 Controller로 전달
        return scheduleRepository.findAllByUserEqualsOrderByCreatedAtDesc(username).stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }
    //모든 일정 조회
    public List<ScheduleResponseDto> getSchedule() {
        //모든 일정을 리스트로 생성 후 Controller로 전달
        return scheduleRepository.findAllByOrderByCreatedAtDesc().stream().
                map(ScheduleResponseDto::new).
                toList();
    }

    //단건 일정 조회
    public ScheduleResponseDto getChoiceSchedule(Long id) {
        //입력 받은 id 값으로 Schedule Entity 생성
        Schedule schedule = findSchedule(id);
        //생성한 일정을 Controller로 전달
        return new ScheduleResponseDto(schedule);
    }

    //선택 일정 수정
//    @Transactional
//    public ScheduleResponseDto updateSchedule(Long id, UpdateScheduleRequestDto updateScheduleRequestDto) {
//        //입력받은 값에서 password만 String Password로 저장
//        String password = updateScheduleRequestDto.getPassword();
//        //패스워드 검증(pwCheck 메소드) 후 이상 없으면 Schedule Entity 생성
////        Schedule schedule = pwCheck(id, password);
//        //유저가 입력한 수정된 내용으로 update 메소드 이용해서 정보 업데이트
//        schedule.update(updateScheduleRequestDto);
//        //Controller에게 수정된 일정 전달.
//        return new ScheduleResponseDto(schedule);
//    }

//    //선택 일정 삭제
//    public Long deleteSchedule(Long id, PwCheckScheduleRequestDto pwCheckScheduleRequestDto) {
//        //입력받은 패스워드를 String Password로 저장
//        String password = pwCheckScheduleRequestDto.getPassword();
//        //패스워드 검증(pwCheck 메소드) 후 이상 없으면 Schedule Entity 생성
//        Schedule schedule = pwCheck(id, password);
//        // 해당 일정 삭제
//        scheduleRepository.delete(schedule);
//        //Controller에게 삭제된 id 값 만 전달
//        return id;
//    }


    //입력받은 id 값으로 해당하는 일정 찾기
    private Schedule findSchedule(Long id) {
        //입력 받은 id 값에 해당되는 일정을 생성한 Schedule Entity에 입력
        //해당하는 Entity 없으면 Exception 발생
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 스케쥴은 등록되어있지 않습니다."));
    }

//    //패스워드 체크
//    private Schedule pwCheck(Long id, String password) {
//        //이용자에게 입력받은 id 값으로 findSchedule 메소드를 이용해서 Schedule Entity 생성
//        Schedule schedule = findSchedule(id);
//        //수정 전 password 값을 String oldPassword 에 입력
//        String oldPassword = schedule.getPassword();
//        //이용자가 입력한 패스워드(String password)와 변경 전 패스워드(String oldPassword)를 비교해서 일치하면 Schedule Entity 반환
//        //패스워드 일치하지 않을시 Exception 발생
//        if (!oldPassword.equals(password)) {
//            throw new BadCredentialsException("패스워드가 일치하지 않습니다.");
//        }
//        return schedule;
//
////        패스워드 null 일 때 Exception 생성했으나 Schedule 클래스에 password  @Column(nullable = false) 로 null 발생 할 수 없어서 주석처리
////        if (scheduleRequestDto.getPw() == null){
////            throw new NullPointerException("패스워드가 입력되지 않았습니다.");
////        }
//    }
}

