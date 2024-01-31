package org.example.schedule.dto;

import lombok.Getter;
import org.example.schedule.entity.Comment;

@Getter
public class UpdateCommentResponsetDto {
    private String bodyComment;

    public UpdateCommentResponsetDto(Comment commnet){
        this.bodyComment = commnet.getBodyComment();
    }
}
