package org.example.schedule.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.schedule.dto.*;
import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.User;
import org.example.schedule.jwt.JwtUtil;
import org.example.schedule.repository.ScheduleRepository;
import org.example.schedule.security.UserDetailsImpl;
import org.example.schedule.security.UserDetailsServiceImpl;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ScheduleService {
    //공통으로 사용 할 Repository 필드 선언 및 생성자 생성
    private final ScheduleRepository scheduleRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;


    public ScheduleService(ScheduleRepository scheduleRepository, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.scheduleRepository = scheduleRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    //할일카드 등록
    public AddScheduleResponseDto createSchedule(AddScheduleRequestDto addScheduleRequestDto, User user) {
        //새로운 Schedule Entity에 user 정보까지 같이담아서 repo에 저장
        Schedule addSchedule = scheduleRepository.save(new Schedule(addScheduleRequestDto, user));
        //생성한 할일카드를 ScheduleResponseDto에 담아서 Controller로 전달
        //user정보가 AddScheduleResponseDto 생성하면서 username 만들어줌
        return new AddScheduleResponseDto(addSchedule, user);
    }

    //모든 할일카드 조회
    public List<AllScheduleResponseDto> getSchedule() {
        //모든 할일카드를 리스트로 생성 후 Controller로 전달
        List<AllScheduleResponseDto> allScheduleResponseDtoList =
                scheduleRepository.findAllByOrderByCreatedAtDesc().stream()
                        .map(AllScheduleResponseDto::new).toList();
        //비어있으면 Exception 발생
        if (allScheduleResponseDtoList.isEmpty()) {
            throw new NullPointerException("입력된 할일이 없습니다.");
        }
        return allScheduleResponseDtoList;
    }

    //선택한 할일카드 조회
    public ChoiceScheduleResponseDto getChoiceSchedule(Long id) {
        //입력 받은 id 값으로 Schedule Entity 생성
        Schedule schedule = findSchedule(id);
        //생성한 할일카드를 Controller로 전달
        return new ChoiceScheduleResponseDto(schedule);
    }

    //선택한 할일카드 수정
    @Transactional
    public Optional<?> updateSchedule(Long id,
                                   HttpServletRequest httpServletRequest,
                                   UpdateScheduleRequestDto updateScheduleRequestDto,
                                   UserDetailsImpl userDetails) {
        // 로그인한 사용자와 작성한 사용자 비교해서 본인 할일카드인지 확인
        String loginUsername = userDetails.getUser().getUsername();
        String loginPassword = userDetails.getUser().getPassword();
        String scheduleUsername = findUser(id).getUsername();
        String schedulePassword = findUser(id).getPassword();
        if (!(loginUsername.equals(scheduleUsername) && loginPassword.equals(schedulePassword))){
            ResponseEntity<String> entity = new ResponseEntity<String>("작성자만 삭제/수정할 수 있습니다.", HttpStatusCode.valueOf(400));
            return Optional.of(ResponseEntity.status(400).body("작성자만 삭제/수정할 수 있습니다."));
        }

        //토큰 유효성 검사
        if (!jwtUtil.validateToken(jwtUtil.getJwtFromHeader(httpServletRequest))){
            ResponseEntity<String> entity = new ResponseEntity<String>("유효하지않은 토큰입니다.", HttpStatusCode.valueOf(400));
            return Optional.of(ResponseEntity.status(400).body("유효하지않은 토큰입니다."));
        }

        // 수정
        Schedule schedule = findSchedule(id);
        schedule.update(updateScheduleRequestDto);
        return Optional.of(new ChoiceScheduleResponseDto(schedule));


        //입력받은 값에서 password만 String Password로 저장
//        String password = updateScheduleRequestDto.getPassword();
        //패스워드 검증(pwCheck 메소드) 후 이상 없으면 Schedule Entity 생성
//        Schedule schedule = pwCheck(id, password);
        //유저가 입력한 수정된 내용으로 update 메소드 이용해서 정보 업데이트
//        schedule.update(updateScheduleRequestDto);
        //Controller에게 수정된 일정 전달.
    }

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


    //입력받은 id 값으로 해당하는 작성자 찾기
    private User findUser(Long id) {
        //입력 받은 id 값에 해당되는 할일카드를 생성한 Schedule Entity에 입력
        //해당하는 Entity 없으면 Exception 발생
        Schedule schedule = findSchedule(id);
        return schedule.getUser();
    }

    //입력받은 id 값으로 해당하는 할일카드 찾기
    private Schedule findSchedule(Long id) {
        //입력 받은 id 값에 해당되는 할일카드를 생성한 Schedule Entity에 입력
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

