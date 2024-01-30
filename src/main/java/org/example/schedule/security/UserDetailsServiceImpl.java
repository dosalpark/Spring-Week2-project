package org.example.schedule.security;

import lombok.extern.slf4j.Slf4j;
import org.example.schedule.entity.User;
import org.example.schedule.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Slf4j(topic = "이용자 정보 일치 확인")
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    //유저정보 찾아오기위해서 userRepository 주입
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //유저정보가 맞지 않으면 exception 설정
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("일치하는 이용자가 없습니다."));
        return new UserDetailsImpl(user);
    }
}
