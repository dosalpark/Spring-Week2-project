package org.example.schedule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.schedule.entity.User;
import org.example.schedule.repository.ScheduleRepository;
import org.example.schedule.repository.UserRepository;
import org.example.schedule.service.ScheduleService;
import org.example.schedule.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
public class AddUser {
    @PersistenceContext
    EntityManager em;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("회원가입 테스트")
    void test1() {
        User user = new User();
        user.setUsername("do");
        user.setUsername("12333");

        Optional<User> usernameCheck = userRepository.findByUsername(user.getUsername());
        if (usernameCheck.isPresent()){
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        //유저추가
        User user1 = new User(user.getUsername(), user.getPassword());
        userRepository.save(user1);
    }

}
