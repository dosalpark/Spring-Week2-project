package org.example.schedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.schedule.dto.AddCommentRequestDto;
import org.example.schedule.dto.UpdateCommentRequestDto;

@Entity
@NoArgsConstructor
@Getter
@Table
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String bodyComment;

    //1:N 단방향
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    //댓글 추가 할 때 사용
    public Comment(User user, Schedule schedule, AddCommentRequestDto addCommentRequestDto) {
        this.user = user;
        this.schedule = schedule;
        this.bodyComment = addCommentRequestDto.getBodyComment();
    }

    //댓글 수정 할 때 사용
    public void update(UpdateCommentRequestDto updateCommentRequestDto) {
        this.bodyComment = updateCommentRequestDto.getBodyComment();
    }
}
