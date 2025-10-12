package com.example.NewsInsight.config;

import com.example.NewsInsight.interceptor.SubscriptionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
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
}