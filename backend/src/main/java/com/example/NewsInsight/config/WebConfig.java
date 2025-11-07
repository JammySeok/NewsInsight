package com.example.NewsInsight.config;

import com.example.NewsInsight.interceptor.SubscriptionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SubscriptionInterceptor subscriptionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(subscriptionInterceptor)
                .addPathPatterns("/subscription/**") // '/subscription/'으로 시작하는 모든 경로를 감시하지만,
                .excludePathPatterns("/subscription/pay"); // '/subscription/pay' 경로는 감시에서 제외합니다.
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 1. 모든 경로에 대해
                .allowedOrigins("http://localhost:5173") // 2. React 앱 주소(http://localhost:5173)의 요청을 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 3. 허용할 HTTP 메서드
                .allowCredentials(true) // 4. (중요) 쿠키/세션(JSESSIONID)을 주고받을 수 있도록 허용
                .maxAge(3600);
    }
}