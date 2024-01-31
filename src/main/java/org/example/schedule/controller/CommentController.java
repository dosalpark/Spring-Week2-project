package org.example.schedule.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.schedule.dto.AddCommentRequestDto;
import org.example.schedule.dto.UpdateCommentRequestDto;
import org.example.schedule.security.UserDetailsImpl;
import org.example.schedule.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //댓글 작성
    @PostMapping("/schedule/{scheduleId}/comment")
    public ResponseEntity<?> createComment(@PathVariable Long scheduleId,
                                           HttpServletRequest httpServletRequest,
                                           @RequestBody AddCommentRequestDto addCommentRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(scheduleId, httpServletRequest, addCommentRequestDto, userDetails);
    }

    //댓글 수정
    @PostMapping("/schedule/{scheduleId}/comment/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long scheduleId,
                                           @PathVariable Long commentId,
                                           HttpServletRequest httpServletRequest,
                                           @RequestBody UpdateCommentRequestDto updateCommentRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.updateComment(scheduleId, commentId, httpServletRequest, updateCommentRequestDto, userDetails);
    }


    //댓글 삭제

}
