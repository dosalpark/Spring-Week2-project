package org.example.schedule.dto;

import lombok.Getter;
import org.example.schedule.entity.Comment;

@Getter
public class AddCommentResponseDto {
    private String bodyComment;

    public AddCommentResponseDto(Comment commnet){
        this.bodyComment = commnet.getBodyComment();
    }
}
