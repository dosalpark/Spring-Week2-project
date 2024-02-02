package org.example.schedule.controller;

import org.example.schedule.dto.AddCommentRequestDto;
import org.example.schedule.dto.UpdateCommentRequestDto;
import org.example.schedule.security.UserDetailsImpl;
import org.example.schedule.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule/{scheduleId}/comment")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //댓글 작성
    @PostMapping
    public ResponseEntity<?> createComment(@PathVariable Long scheduleId,
                                           @RequestBody AddCommentRequestDto addCommentRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(scheduleId, addCommentRequestDto, userDetails);
    }

    //댓글 수정
    @PostMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long scheduleId,
                                           @PathVariable Long commentId,
                                           @RequestBody UpdateCommentRequestDto updateCommentRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(scheduleId, commentId, updateCommentRequestDto, userDetails);
    }


    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long scheduleId,
                                           @PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(scheduleId, commentId, userDetails);
    }

}
