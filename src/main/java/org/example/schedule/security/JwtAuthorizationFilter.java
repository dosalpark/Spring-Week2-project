package org.example.schedule.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.schedule.jwt.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    //JWT 검증하기위해서 JwtUtil 주입
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    //토큰 검증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //보내온 값의 헤더에서 토큰 추출
        String tokenValue = jwtUtil.getJwtFromHeader(request);
        //hasText사용해서 토큰 값 검증 (실패시 token error 로그 기록)
        if (StringUtils.hasText(tokenValue)) {
            //jwtUtil에 validateToken 메소드 검증
            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }
            //문제없으면 토큰에서 이용자 정보만 info 에 저장
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                //저장 정보중에서 username 추출해서 인증 및 인증객체 생성
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                //에러 발생시 로그 기록
                log.error((e.getMessage()));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        //빈 SecurityContext 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        //아래 객체생성 메소드 사용해서 인증 객체 생성
        Authentication authentication = createAuthentication(username);
        //인증정보를 SecurityContext에 넣음
        context.setAuthentication(authentication);
        //SecurityContext를 SecurityContextHolder에 넣음
        SecurityContextHolder.setContext(context);

    }

    //인증 객체 생성
    private Authentication createAuthentication(String username) {
        // username을 불러와서 userDetails에 넣음
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //가져온 정보로 UsernamePasswordAuthenticationToken 생성해서 Authentication에 전달
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
