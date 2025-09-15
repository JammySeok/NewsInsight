package com.example.NewsInsight.security;

import com.example.NewsInsight.service.impl.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/auth/*", "/css/**", "/js/**", "/images/**", "/error/**").permitAll()
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("userid")
                .failureUrl("/auth/login?error=true")
                .permitAll()
        );

        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/auth/login") // 로그인 페이지는 기존과 동일하게 사용
                .defaultSuccessUrl("/")   // 로그인 성공 후 이동할 페이지
                .failureUrl("/auth/login?error=true") // 로그인 실패 시
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService) // 사용자 정보를 처리할 서비스 등록
                )
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
