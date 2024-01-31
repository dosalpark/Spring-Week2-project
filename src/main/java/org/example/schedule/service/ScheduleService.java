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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

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
    //상황에 따라 본인 할일 카드가 아니면 String을 본인 할일카드 수정된 내용을 반환해야해서 ResponseEntity에 타입을 주지 않고 사용
    @Transactional
    public ResponseEntity<?> updateSchedule(Long id,
                                            HttpServletRequest httpServletRequest,
                                            UpdateScheduleRequestDto updateScheduleRequestDto,
                                            UserDetailsImpl userDetails) {
        //토큰 유효성 검사
        //토큰을 검중해서 토큰이 유효하지 않을 경우 String과 http 400code를 반환
        if (!jwtUtil.validateToken(jwtUtil.getJwtFromHeader(httpServletRequest))) {
            return new ResponseEntity<>("유효하지않은 토큰입니다", HttpStatusCode.valueOf(400));
        }

        // 로그인한 사용자와 작성한 사용자 비교해서 본인 할일카드인지 확인
        //본인 할일카드가 아닐 경우 String과 http 400code를 반환
        if (!findMySchedule(id, userDetails)) {
            return new ResponseEntity<>("작성자만 삭제/수정할 수 있습니다.", HttpStatusCode.valueOf(400));
        }

        // 본인 할일카드 + 윺효성이 검증되면 받아온 id로 해당 할일카드를 찾음
        Schedule schedule = findSchedule(id);
        // 만들어논 update 메소드를 이용해서 해당 할일카드 업데이트
        schedule.update(updateScheduleRequestDto);
        //Choice..Dtd(반환값 title,body,username,createAt)에 수정내용 전달
        ChoiceScheduleResponseDto choiceScheduleResponseDto = new ChoiceScheduleResponseDto(schedule);
        // controller로 ResponseEntity에 Dto와 code를 같이 담아서 전달
        return new ResponseEntity<>(choiceScheduleResponseDto, HttpStatusCode.valueOf(200));
    }

    //선택한 할일카드 완료 처리
    @Transactional
    public void clearSchedule(Long id, HttpServletRequest httpServletRequest, UserDetailsImpl userDetails) {
        //토큰 유효성 검사
        //토큰을 검중해서 토큰이 유효한지 확인
        if (jwtUtil.validateToken(jwtUtil.getJwtFromHeader(httpServletRequest))) {
            // 로그인한 사용자와 작성한 사용자 비교해서 본인 할일카드인지 확인
            if (findMySchedule(id, userDetails)) {
                Schedule schedule = findSchedule(id);
                schedule.setClearYn(true);
            }
        }
    }


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

    //입력받은 id 값으로 해당하는 글의 작성자인지 확인
    private boolean findMySchedule(Long id, UserDetailsImpl userDetails) {
        String loginUsername = userDetails.getUser().getUsername();
        String loginPassword = userDetails.getUser().getPassword();
        String scheduleUsername = findUser(id).getUsername();
        String schedulePassword = findUser(id).getPassword();
        if ((loginUsername.equals(scheduleUsername) && loginPassword.equals(schedulePassword))) {
            return true;
        } else {
            return false;
        }
    }
}

