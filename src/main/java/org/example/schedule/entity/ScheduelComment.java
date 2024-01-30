package org.example.schedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.schedule.controller.ScheduleController;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "Schedule_comment")
public class ScheduelComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    //생성될때 각 schedule, comment의 id값 입력해서 생성
    public ScheduelComment(Schedule schedule, Comment comment){
        this.schedule = schedule;
        this.comment = comment;
    }
}
