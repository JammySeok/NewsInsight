package com.example.NewsInsight.security;

import com.example.NewsInsight.service.impl.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
                .requestMatchers("/", "/auth/**", "/css/**", "/js/**", "/images/**", "/error/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login") // 👈 React가 호출할 로그인 처리 URL
                .usernameParameter("userid")
                // 👇 (중요) 로그인 성공 시: 302 리디렉션 대신 200 OK + JSON 응답
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    // (필요 시) 응답 바디에 사용자 정보(JSON)를 실어 보낼 수 있습니다.
                    // 예: new ObjectMapper().writeValue(response.getWriter(), authentication.getPrincipal());
                    response.getWriter().write("{\"message\": \"로그인 성공\"}");
                })
                // 👇 (중요) 로그인 실패 시: 302 리디렉션 대신 401 Unauthorized + JSON 응답
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write("{\"message\": \"아이디 또는 비밀번호가 올바르지 않습니다.\"}");
                })
                .permitAll()
        );

        // --- 1. OAuth2 로그인 (oauth2Login) 설정 수정 ---
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/auth/login")
                // 👇 (중요) 성공 시 React 앱 주소로 리디렉션
                .defaultSuccessUrl("http://localhost:5173", true)
                // 👇 (중요) 실패 시 React 앱의 로그인 페이지로 리디렉션
                .failureUrl("http://localhost:5173/login?error=oauth")
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                )
        );

        // --- (CSRF 설정은 이전 답변과 동일하게 유지) ---
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/payment/verify", "/api/**", "/auth/login", "/logout")
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                // 👇 (중요) 로그아웃 성공 시: 302 리디렉션 대신 200 OK 응답
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        );

        http.exceptionHandling(exception -> exception
                .accessDeniedPage("/error/forbidden")
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}