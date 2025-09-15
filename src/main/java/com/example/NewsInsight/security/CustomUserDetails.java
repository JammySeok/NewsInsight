package com.example.NewsInsight.security;

import com.example.NewsInsight.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Map;

@Getter
public class CustomUserDetails extends User implements OAuth2User {

    private final Integer id;
    private Map<String, Object> attributes; // OAuth2 사용자 정보를 담을 필드

    // 1. 일반 로그인용 생성자
    public CustomUserDetails(UserEntity userEntity) {
        super(
                userEntity.getUserid(),
                userEntity.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().getKey()))
        );
        this.id = userEntity.getId();
    }

    // 2. OAuth2 로그인용 생성자
    public CustomUserDetails(UserEntity userEntity, Map<String, Object> attributes) {
        super(
                userEntity.getUserid(),
                userEntity.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().getKey()))
        );
        this.id = userEntity.getId();
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        // OAuth2 공급자로부터 받은 사용자의 고유 ID를 반환합니다.
        // attributes 맵에서 주 키(primary key)에 해당하는 값을 반환해야 하지만,
        // 여기서는 User 클래스의 getUsername()을 반환해도 무방합니다.
        // 이것이 SecurityContext에서 principal의 이름으로 사용됩니다.
        return super.getUsername();
    }
}