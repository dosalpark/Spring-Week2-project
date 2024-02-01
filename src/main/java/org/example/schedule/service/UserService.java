package org.example.schedule.service;

import org.example.schedule.dto.UserRequestDto;
import org.example.schedule.entity.Code;
import org.example.schedule.entity.User;
import org.example.schedule.repository.UserRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> addUser(UserRequestDto userRequestDto) {
        //입력된 정보에서 username, password 확인
        String username = userRequestDto.getUsername();
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        //중복사용자 확인
        Optional<User> usernameCheck = userRepository.findByUsername(username);
        if (usernameCheck.isPresent()){
            return ResponseEntity.status(400).body(Code.FAIL_401.getStatusComment());
        }
        //유저추가
        User user = new User(username, password);
        userRepository.save(user);
        return ResponseEntity.status(200).body(Code.SUCCESS_200.getStatusComment());
    }

//    public ResponseEntity<String> loginUser(UserRequestDto userRequestDto) {
//        //입력 정보 확인
//        String username = userRequestDto.getUsername();
//        String password = passwordEncoder.encode(userRequestDto.getPassword());
//        //사용자 등록여부 확인
//        Optional<User> usernameCheck = userRepository.findByUsername(username);
//        //동일한 사용자가 없거나, 패스워드가 다를때
//        if(usernameCheck.isPresent()){
//            if(usernameCheck.get().getPassword().equals(password)){
//                System.out.println("확인용 password = " + password);
//                System.out.println("확인용 usernameCheck.get().getPassword() = " + usernameCheck.get().getPassword());
//                return ResponseEntity.status(200).body("테스트중");
//            } else {
//                //이용자는 있는데 패스워드 다를때
//                return ResponseEntity.status(400).body("일치하는 사용자가 없습니다.");
//            }
//        } else {
//            //일치하는 이용자가 없을 때
//            return ResponseEntity.status(400).body("일치하는 사용자가 없습니다.");
//        }
//    }
}


