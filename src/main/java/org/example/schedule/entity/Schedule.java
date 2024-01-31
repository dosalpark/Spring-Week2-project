package org.example.schedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.schedule.dto.AddScheduleRequestDto;
import org.example.schedule.dto.UpdateScheduleRequestDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Schedule extends Timestemped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String titleSchedule;

    @Column(nullable = false, length = 500)
    private String bodySchedule;

    //1:N 단방향
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //1:N 양방향
    @OneToMany(mappedBy = "schedule")
    private List<Comment> commentList = new ArrayList<>();

    @Column(nullable = false, length = 500)
    private Boolean clearYn = false;



    //일정 입력시 생성자로 유저 입력정보 넘겨줌
    public Schedule (AddScheduleRequestDto addScheduleRequestDto, User user){
        this.titleSchedule = addScheduleRequestDto.getTitleSchedule();
        this.bodySchedule = addScheduleRequestDto.getBodySchedule();
        this.user = user;

    }
    //일정 수정시 입력된 값으로 일정을 업데이트
    public void update(UpdateScheduleRequestDto updateScheduleRequestDto){
        this.titleSchedule = updateScheduleRequestDto.getTitleSchedule();
        this.bodySchedule = updateScheduleRequestDto.getBodySchedule();
    }


}
