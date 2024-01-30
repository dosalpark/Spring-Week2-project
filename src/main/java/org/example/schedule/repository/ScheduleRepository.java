package org.example.schedule.repository;

import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    //모든 할일카드를 작성일 역순으로 정렬 및 리스트로 생성
    List<Schedule> findAllByOrderByCreatedAtDesc();

}
