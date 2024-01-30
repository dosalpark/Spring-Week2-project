package org.example.schedule.repository;

import org.example.schedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //UserDetailsServiceImpl 에서 일치하지 않을때 throw 던져야 해서 Optional 설정
    Optional<User> findByUsername(String username);

}
