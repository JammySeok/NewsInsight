package com.example.NewsInsight.service.impl;

import com.example.NewsInsight.dto.OAuthAttributes;
import com.example.NewsInsight.entity.UserEntity;
import com.example.NewsInsight.repository.UserRepository;
import com.example.NewsInsight.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // OAuth2UserService를 사용하여 OAuth2User 정보를 가져옴
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 등록 ID(google, naver...)와 사용자 이름 속성을 가져옴
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 소셜 정보로 DB에서 유저 찾기
        UserEntity userEntity = saveOrUpdate(attributes);

        // CustomUserDetails 객체를 생성하여 반환 (UserDetails와 OAuth2User를 모두 구현)
        // 여기서는 CustomUserDetails가 User를 상속하므로 바로 생성 가능
        return new CustomUserDetails(userEntity);
    }


    // DB에 사용자가 있으면 업데이트, 없으면 새로 생성
    private UserEntity saveOrUpdate(OAuthAttributes attributes) {
        Optional<UserEntity> userOptional = userRepository.findByProviderAndProviderId(attributes.getProvider(), attributes.getProviderId());

        UserEntity userEntity;
        if (userOptional.isPresent()) {
            // 이미 가입된 경우
            userEntity = userOptional.get();
            // 필요하다면 여기서 사용자 정보 업데이트 (예: 이름, 프로필 사진)
            // userEntity.setNickname(attributes.getName());
            // userRepository.save(userEntity);

        } else {
            // 처음 로그인하는 경우
            userEntity = attributes.toEntity();
            // 이메일이 중복될 경우의 처리 로직이 필요할 수 있음
            // 예: Optional<UserEntity> existingEmailUser = userRepository.findByEmail(attributes.getEmail());
            // if(existingEmailUser.isPresent()) { ... 처리 ... }
            userRepository.save(userEntity);
        }

        return userEntity;
    }
}