package org.example.schedule.security;

import org.example.schedule.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    //사용자 권한 목록(따로권한 설정안할거라 null)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    //User 가져오기
    public User getUser(){
        return user;
    }

    //사용자 패스워드 확인
    public String getPassword(){
        return user.getPassword();
    }
    //사용자 이름 확인
    public String getUsername() {
        return user.getUsername();
    }
    //사용자계정 유효기간 만료여부
    @Override
    public boolean isAccountNonExpired(){
        return true;
    };
    //사용자계정 잠김여부
    @Override
    public boolean isAccountNonLocked(){
        return true;
    };
    //사용자계정 패스워드 만료여부
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    };
    //사용자계정 활성화여부
    @Override
    public boolean isEnabled(){
        return true;
    };
}
