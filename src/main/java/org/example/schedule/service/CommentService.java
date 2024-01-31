package org.example.schedule.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.schedule.dto.AddCommentRequestDto;
import org.example.schedule.dto.AddCommentResponseDto;
import org.example.schedule.dto.UpdateCommentRequestDto;
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

import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final JwtUtil jwtUtil;

    public CommentService(CommentRepository commentRepository, ScheduleRepository scheduleRepository, JwtUtil jwtUtil) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
        this.jwtUtil = jwtUtil;
    }


    // 댓글 생성
    @Transactional
    public ResponseEntity<?> createComment(Long scheduleId,
                                          HttpServletRequest httpServletRequest,
                                          AddCommentRequestDto addCommentRequestDto,
                                          UserDetailsImpl userDetails) {
        // 사용자의 토큰 유효한지 확인
        if (!jwtUtil.validateToken(jwtUtil.getJwtFromHeader(httpServletRequest))) {
            return new ResponseEntity<>("유효하지않은 토큰입니다", HttpStatusCode.valueOf(400));
        }
        // 할일카드가 없을수도있으니 Optional<Schedule>로 설정 후 비어있으면 400Error, String 전달
        Optional<Schedule> scheduleCheck = scheduleRepository.findById(scheduleId);
        if (scheduleCheck.isEmpty()){
            return new ResponseEntity<>("해당하는 할일카드가 없습니다.", HttpStatusCode.valueOf(400));
        }
        // 댓글 내용 저장
        Comment addComment = commentRepository.save(new Comment(userDetails.getUser(),scheduleCheck.get(), addCommentRequestDto));
        // ResponseEntity에 commentResponsetDto 넣어서 comtroller로 전달
        AddCommentResponseDto addCommentResponseDto = new AddCommentResponseDto(addComment);
        return new ResponseEntity<>(addCommentResponseDto,HttpStatusCode.valueOf(200));
    }

    // 댓글 업데이트
    @Transactional
    public ResponseEntity<?> updateComment(Long scheduleId,
                                           Long commentId,
                                           HttpServletRequest httpServletRequest,
                                           UpdateCommentRequestDto updateCommentRequestDto,
                                           UserDetailsImpl userDetails) {
        // 사용자의 토큰 유효한지 확인
        if (!jwtUtil.validateToken(jwtUtil.getJwtFromHeader(httpServletRequest))) {
            return new ResponseEntity<>("유효하지않은 토큰입니다", HttpStatusCode.valueOf(400));
        }
        // 할일카드가 없을수도있으니 Optional<Schedule>로 설정 후 비어있으면 400Error, String 전달
        Optional<Schedule> scheduleCheck = scheduleRepository.findById(scheduleId);
        if (scheduleCheck.isEmpty()){
            return new ResponseEntity<>("해당하는 할일카드가 없습니다.", HttpStatusCode.valueOf(400));
        }
        // 본인이 쓴 댓글인지 확인
        if(!findMyComment(commentId,userDetails)){
            return new ResponseEntity<>("작성자만 삭제/수정할 수 있습니다.", HttpStatusCode.valueOf(400));
        }
        // 댓글 가져와서 수정
        Comment comment = findComment(commentId);
        comment.update(updateCommentRequestDto);
        // ResponseEntity에 commentResponsetDto 넣어서 comtroller로 전달
        Comment updateComment = commentRepository.save(comment);
        return new ResponseEntity<>(updateComment,HttpStatusCode.valueOf(200));

    }



    //입력받은 commentId 값으로 해당하는 작성자 찾기
    private User findUser(Long commentId) {
        //입력 받은 id 값에 해당되는 댓글를 생성한 comment Entity에 입력
        //해당하는 Entity 없으면 Exception 발생
        Comment comment = findComment(commentId);
        return comment.getUser();
    }

    //입력받은 commentId 값으로 해당하는 댓글 찾기
    private Comment findComment(Long commentId) {
        //입력 받은 id 값에 해당되는 댓글을 생성한 Comment Entity에 입력
        //해당하는 Entity 없으면 Exception 발생
        return commentRepository.findById(commentId).
                orElseThrow(() ->
                new IllegalArgumentException("해당 댓글은 등록되어있지 않습니다."));
    }

    //입력받은 commentId 값으로 해당하는 글의 작성자인지 확인
    private boolean findMyComment(Long id, UserDetailsImpl userDetails) {
        String loginUsername = userDetails.getUser().getUsername();
        String loginPassword = userDetails.getUser().getPassword();
        String scheduleUsername = findUser(id).getUsername();
        String schedulePassword = findUser(id).getPassword();
        return loginUsername.equals(scheduleUsername) && loginPassword.equals(schedulePassword);
    }
}
