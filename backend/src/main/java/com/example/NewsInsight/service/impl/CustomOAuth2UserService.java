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
        // 소셜 서비스 ID로 가입된 사용자인지 확인
        Optional<UserEntity> userOptional = userRepository.findByProviderAndProviderId(attributes.getProvider(), attributes.getProviderId());

        if (userOptional.isPresent()) {
            // 이미 가입된 경우, 정보 업데이트 후 반환 (필요 시)
            return userOptional.get();

        } else {
            // 가입되지 않은 경우, 이메일로 기존 사용자인지 확인
            Optional<UserEntity> existingEmailUser = userRepository.findByEmail(attributes.getEmail());

            if (existingEmailUser.isPresent()) {
                // 이미 동일한 이메일로 가입된 계정이 있는 경우
                // 소셜 로그인을 막고, 사용자에게 로컬 로그인을 유도하기 위해 예외 발생
                throw new OAuth2AuthenticationException("이미 가입된 이메일입니다. 아이디/비밀번호로 로그인해주세요.");
            }

            // 신규 사용자일 경우, DB에 저장
            UserEntity userEntity = attributes.toEntity();
            return userRepository.save(userEntity);
        }
    }
}