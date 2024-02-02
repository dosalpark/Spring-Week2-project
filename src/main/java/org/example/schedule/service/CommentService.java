package org.example.schedule.service;

import org.example.schedule.dto.AddCommentRequestDto;
import org.example.schedule.dto.AddCommentResponseDto;
import org.example.schedule.dto.UpdateCommentRequestDto;
import org.example.schedule.dto.UpdateCommentResponsetDto;
import org.example.schedule.entity.Code;
import org.example.schedule.entity.Comment;
import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.User;
import org.example.schedule.jwt.JwtUtil;
import org.example.schedule.repository.CommentRepository;
import org.example.schedule.repository.ScheduleRepository;
import org.example.schedule.security.UserDetailsImpl;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    //에러코드담기
    private final List<String> result = new ArrayList<>();
    private Code c1;

    public CommentService(CommentRepository commentRepository, ScheduleRepository scheduleRepository, JwtUtil jwtUtil) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
    }


    // 댓글 생성
    @Transactional
    public ResponseEntity<?> createComment(Long scheduleId,
                                           AddCommentRequestDto addCommentRequestDto,
                                           UserDetailsImpl userDetails) {
        Schedule schedule = findSchedule(scheduleId); //댓글 달 스케쥴 찾기
        if (result.isEmpty()) { //에러가 있는지 확인
            Comment addComment = commentRepository.save(new Comment(userDetails.getUser(), schedule, addCommentRequestDto));
            // ResponseEntity에 commentResponsetDto 넣어서 comtroller로 전달
            AddCommentResponseDto addCommentResponseDto = new AddCommentResponseDto(addComment);
            return new ResponseEntity<>(addCommentResponseDto, HttpStatusCode.valueOf(200));
        } else {
            return errorEntityReturnController();
        }
    }

    // 댓글 업데이트
    @Transactional
    public ResponseEntity<?> updateComment(Long scheduleId,
                                           Long commentId,
                                           UpdateCommentRequestDto updateCommentRequestDto,
                                           UserDetailsImpl userDetails) {
        Schedule schedule = findSchedule(scheduleId); //댓글 달 할일카드 찾기
        findMyComment(commentId, userDetails); // 본인이 쓴 댓글인지 확인
        Comment comment = findComment(commentId); // 댓글 가져오면서 검증하기
        if (result.isEmpty()) { //에러가 있는지 확인
            // 댓글 가져와서 업데이트 후 controller 리턴
            comment.update(updateCommentRequestDto);
            // ResponseEntity에 commentResponsetDto 넣어서 comtroller로 전달
            Comment updateComment = commentRepository.save(comment);
            UpdateCommentResponsetDto updateCommentResponsetDto = new UpdateCommentResponsetDto(updateComment);
            return new ResponseEntity<>(updateCommentResponsetDto, HttpStatusCode.valueOf(200));
        } else {
            return errorEntityReturnController();
        }
    }

    // 댓글 삭제
    @Transactional
    public ResponseEntity<?> deleteComment(Long scheduleId,
                                           Long commentId,
                                           UserDetailsImpl userDetails) {
        Schedule schedule = findSchedule(scheduleId); //댓글 달 할일카드 찾기
        findMyComment(commentId, userDetails); // 본인이 쓴 댓글인지 확인
        Comment comment = findComment(commentId); // 댓글 가져오면서 검증하기
        if (result.isEmpty()) {
            commentRepository.delete(comment);
            c1 = Code.SUCCESS_202;
            return new ResponseEntity<>(c1.getStatusComment(), HttpStatusCode.valueOf(200));
        } else {
            return errorEntityReturnController();
        }
    }

    //입력받은 scheduleId 값으로 해당하는 댓글 찾기
    private Schedule findSchedule(Long scheduleId) {
        //입력 받은 id 값에 해당되는 댓글을 생성한 Comment Entity에 입력
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if (schedule.isEmpty()) {
            c1 = Code.FAIL_406;
            result.add(c1.getStatusComment());
        }
        return schedule.get();
    }

    //입력받은 commentId 값으로 해당하는 댓글 찾기
    private Comment findComment(Long commentId) {
        //입력 받은 id 값에 해당되는 댓글을 생성한 Comment Entity에 입력
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NullPointerException("해당하는 할일카드가 없습니다."));

    }

    //입력받은 commentId 값으로 해당하는 글의 작성자인지 확인
    private void findMyComment(Long id, UserDetailsImpl userDetails) {
        String loginUsername = userDetails.getUser().getUsername();
        String loginPassword = userDetails.getUser().getPassword();
        String scheduleUsername = findUser(id).getUsername();
        String schedulePassword = findUser(id).getPassword();
        if (!(loginUsername.equals(scheduleUsername) && loginPassword.equals(schedulePassword))) {
            c1 = Code.FAIL_405;
            result.add(c1.getStatusComment());
        }
    }

    //입력받은 commentId 값으로 해당하는 작성자 찾기
    private User findUser(Long commentId) {
        //입력 받은 id 값에 해당되는 댓글를 생성한 comment Entity에 입력
        //해당하는 Entity 없으면 Exception 발생
        Comment comment = findComment(commentId);
        return comment.getUser();
    }

    //에러 발생시 첫번째 result가지고 HTTP 상태코드와 맞는 String 전달
    private ResponseEntity<?> errorEntityReturnController() {
        ResponseEntity<?> response = new ResponseEntity<>(result.get(0), HttpStatusCode.valueOf(400));
        result.clear();
        return response;
    }
}
