package org.example.schedule.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.schedule.dto.UserRequestDto;
import org.example.schedule.jwt.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 생성 및 로그인")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    //JWT 생성하기위해서 JwtUtil 주입
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        //로그인 URL 설정
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //입력받은 정보로 UsernamePasswordAuthenticationToken 생성
        try {
            //ObjectMapper로 들어온 유저정보를 Json데이터를 LoginRequestDto로 변환
            UserRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserRequestDto.class);
            //입력받은 username,password를 Token에 담아서 AuthenticationManager 에게 전달
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null //권한을 사용하지않아서 null로 전달
                    )
            );

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        //username으로 JWT토큰 생성
        // Authentication에 들어있는 pericipal(유저정보) 에서 username을 가져옴
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        // jwtUtil에 토큰생성 메소드 통해서 생성
        String token = jwtUtil.createToken(username);
        //헤더의 키값과 토큰을 reponse로 전달
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //에러 코드를 리턴
        response.setStatus(400);
        log.error(String.valueOf(response.getStatus()));
    }


}
