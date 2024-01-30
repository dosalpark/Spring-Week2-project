package org.example.schedule.service;

import org.example.schedule.dto.UserRequestDto;
import org.example.schedule.entity.User;
import org.example.schedule.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void addUser(UserRequestDto userRequestDto) {
        //입력된 정보에서 username, password 확인
        String username = userRequestDto.getUsername();
        String password = userRequestDto.getPassword();

        //중복사용자 확인
        Optional<User> usernameCheck = userRepository.findByUsername(username);
        if (usernameCheck.isPresent()){
           throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        //유저추가
        User user = new User(username, password);
        userRepository.save(user);
    }
}


