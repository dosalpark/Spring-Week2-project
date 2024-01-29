package org.example.schedule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.schedule.entity.Schedule;
import org.example.schedule.repository.ScheduleRepository;
import org.example.schedule.service.ScheduleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class Pwcheck {
    @PersistenceContext
    EntityManager em;

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    ScheduleService scheduleService;

    @Test
    @Transactional
    @Rollback(value = false) // 테스트 코드에서 @Transactional 를 사용하면 테스트가 완료된 후 롤백하기 때문에 false 옵션 추가
    @DisplayName("메모 생성 성공")
    void test1() {
        Schedule schedule = new Schedule();
        schedule.setTitleSchedule("test");
        schedule.setBodySchedule("@Transactional 테스트 중!");
        schedule.setUser("박승현");
        schedule.setPassword("1234567");

        em.persist(schedule);  // 영속성 컨텍스트에 메모 Entity 객체를 저장합니다.

        String password = "1234567";
        String oldPassword = schedule.getPassword();
        //패스워드 일치하지 않을시 Exception
        if (!oldPassword.equals(password)){
            throw new BadCredentialsException("패스워드가 일치하지 않습니다.");
        }

    }
}
