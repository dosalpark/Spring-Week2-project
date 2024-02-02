package org.example.schedule.service;


import lombok.extern.slf4j.Slf4j;
import org.example.schedule.dto.*;
import org.example.schedule.entity.Code;
import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.User;
import org.example.schedule.repository.ScheduleRepository;
import org.example.schedule.security.UserDetailsImpl;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ScheduleService {
    //공통으로 사용 할 Repository 필드 선언 및 생성자 생성
    private final ScheduleRepository scheduleRepository;
    //에러코드담기
    private final List<String> result = new ArrayList<>();
    private Code c1;


    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    //할일카드 등록
    public ResponseEntity<?> createSchedule(AddScheduleRequestDto addScheduleRequestDto,
                                            UserDetailsImpl userDetails) {
        //새로운 Schedule Entity에 user 정보까지 같이담아서 repo에 저장
        Schedule addSchedule = scheduleRepository.save(new Schedule(addScheduleRequestDto, userDetails.getUser()));
        //생성한 할일카드를 AddScheduleResponseDto에 유저정보와 같이 담음
        return new ResponseEntity<>(new AddScheduleResponseDto(addSchedule, userDetails.getUser()), HttpStatusCode.valueOf(200));

    }

    //모든 할일카드 조회
    public ResponseEntity<?> getSchedule() {
        //모든 할일카드를 리스트로 생성 후 Controller로 전달
        List<AllScheduleResponseDto> allScheduleResponseDtoList =
                scheduleRepository.findAllByOrderByCreatedAtDesc().stream()
                        .map(AllScheduleResponseDto::new).toList();
        //비어있으면 Exception 발생
        if (allScheduleResponseDtoList.isEmpty()) {
            c1 = Code.FAIL_403;
            return new ResponseEntity<>(c1.getStatusComment(), HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity<>(allScheduleResponseDtoList, HttpStatusCode.valueOf(200));
    }

    //선택한 할일카드 조회
    public ResponseEntity<?> getChoiceSchedule(Long scheduleId) {
        //입력 받은 id 값으로 Schedule Entity 생성
        Schedule schedule = findSchedule(scheduleId);// 할일카드 가져오면서 검증하기
        return new ResponseEntity<>(new ChoiceScheduleResponseDto(schedule), HttpStatusCode.valueOf(200));
    }

    //선택한 할일카드 수정
    @Transactional
    public ResponseEntity<?> updateSchedule(Long scheduleId,
                                            UpdateScheduleRequestDto updateScheduleRequestDto,
                                            UserDetailsImpl userDetails) {
        findMySchedule(scheduleId, userDetails); //작성자 본인인지 확인
        Schedule schedule = findSchedule(scheduleId);// 할일카드 가져오면서 검증하기
        //에러가 있는지 확인
        if (result.isEmpty()) {
            //해당 할일카드를 찾아서 업데이트 후 controller 리턴
            schedule.update(updateScheduleRequestDto);
            return new ResponseEntity<>(new UpdateScheduleResponseDto(schedule), HttpStatusCode.valueOf(200));
        } else {
            return errorEntityReturnController();
        }
    }

    //선택한 할일카드 완료 처리
    @Transactional
    public void clearSchedule(Long scheduleId, UserDetailsImpl userDetails) {
        //작성자 본인인지 확인
        findMySchedule(scheduleId, userDetails);
        //에러가 있는지 확인
        if (result.isEmpty()) {
            //할일 카드 찾아서 완료여부 Y로 변경
            Schedule schedule = findSchedule(scheduleId);
            schedule.setClearYn(true);
        }
        result.clear();
    }


    //입력받은 scheduleId 값으로 해당하는 할일카드 찾기
    private Schedule findSchedule(Long scheduleId) {
        //입력 받은 id 값에 해당되는 댓글을 생성한 Comment Entity에 입력
        return scheduleRepository.findById(scheduleId).orElseThrow(() ->
                new NullPointerException("해당하는 할일카드가 없습니다."));
    }


    //입력받은 id 값으로 해당하는 글의 작성자인지 확인 (userScheduleCheck에 포함되어있음)
    private void findMySchedule(Long scheduleId, UserDetailsImpl userDetails) {
        String loginUsername = userDetails.getUser().getUsername();
        String loginPassword = userDetails.getUser().getPassword();
        String scheduleUsername = findUser(scheduleId).getUsername();
        String schedulePassword = findUser(scheduleId).getPassword();
        if (!(loginUsername.equals(scheduleUsername) && loginPassword.equals(schedulePassword))) {
            c1 = Code.FAIL_405;
            result.add(c1.getStatusComment());

        }
    }

    //입력받은 id 값으로 해당하는 작성자 찾기 (isMySchedule 포함되어있음)
    private User findUser(Long scheduleId) {
        //입력 받은 id 값에 해당되는 할일카드를 생성한 Schedule Entity에 입력
        //해당하는 Entity 없으면 Exception 발생
        Schedule schedule = findSchedule(scheduleId);
        return schedule.getUser();
    }

    //에러 발생시 첫번째 result가지고 HTTP 상태코드와 맞는 String 전달
    private ResponseEntity<?> errorEntityReturnController() {
        ResponseEntity<?> response = new ResponseEntity<>(result.get(0), HttpStatusCode.valueOf(400));
        result.clear();
        return response;
    }
}

