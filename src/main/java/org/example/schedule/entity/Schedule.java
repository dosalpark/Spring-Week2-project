package org.example.schedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.schedule.dto.ScheduleRequestDto;
import org.example.schedule.dto.UpdateScheduleRequestDto;

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

    @Column(nullable = false, length = 500)
    private String user;

    @Column(nullable = false, length = 500)
    private Boolean clearYn = false;



    //일정 입력시 생성자로 유저 입력정보 넘겨줌
    public Schedule (ScheduleRequestDto scheduleRequestDto){
        this.titleSchedule = scheduleRequestDto.getTitleSchedule();
        this.bodySchedule = scheduleRequestDto.getBodySchedule();
        this.user = scheduleRequestDto.getUser();

    }
    //일정 수정시 입력된 값으로 일정을 업데이트
    public void update(UpdateScheduleRequestDto updateScheduleRequestDto){
        this.titleSchedule = updateScheduleRequestDto.getTitleSchedule();
        this.bodySchedule = updateScheduleRequestDto.getBodySchedule();
//        this.user = updateScheduleRequestDto.getUser();
    }


}
