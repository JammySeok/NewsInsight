package com.example.NewsInsight.dto;

import com.example.NewsInsight.entity.UserEntity;
import com.example.NewsInsight.enums.ProviderType;
import com.example.NewsInsight.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private ProviderType provider;
    private String providerId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, ProviderType provider, String providerId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }

    // registrationId(google, naver...)에 따라 다른 메소드를 호출하여 OAuthAttributes 객체 생성
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    // Google 사용자 정보 처리
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .provider(ProviderType.GOOGLE)
                .providerId((String) attributes.get("sub"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // Naver 사용자 정보 처리 (Naver는 'response' 객체 안에 정보가 있음)
    @SuppressWarnings("unchecked")
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("nickname"))
                .email((String) response.get("email"))
                .provider(ProviderType.NAVER)
                .providerId((String) response.get("id"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // OAuthAttributes 정보를 바탕으로 UserEntity 생성
    public UserEntity toEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setNickname(name);
        userEntity.setEmail(email);
        userEntity.setRole(UserRole.USER); // 기본 역할을 USER로 설정
        userEntity.setProvider(provider);
        userEntity.setProviderId(providerId);

        // 소셜 로그인 사용자는 userid와 password가 필수가 아닐 수 있음
        // nullable=false 제약조건에 걸리지 않도록 임시값 또는 고유값 생성
        userEntity.setUserid(provider.getRegistrationId() + "_" + providerId);
        userEntity.setPassword(UUID.randomUUID().toString()); // 임의의 비밀번호 설정

        return userEntity;
    }
}