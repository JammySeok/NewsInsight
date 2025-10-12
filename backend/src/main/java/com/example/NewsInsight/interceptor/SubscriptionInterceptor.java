package com.example.NewsInsight.interceptor;

import com.example.NewsInsight.enums.SubscriptionTier;
import com.example.NewsInsight.security.CustomUserDetails;
import com.example.NewsInsight.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SubscriptionInterceptor implements HandlerInterceptor {

    private final SubscriptionService subscriptionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. SecurityContext에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. 로그인한 사용자 정보가 없거나, CustomUserDetails 타입이 아니면 통과 (인증 처리는 SecurityConfig가 담당)
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return true;
        }

        // 3. CustomUserDetails에서 사용자 ID 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        // 만약 사용자가 ADMIN이면, 구독 등급과 상관없이 무조건 통과시킵니다.
        if (isAdmin) {
            return true;
        }

        Integer userId = userDetails.getId();
        // 4. 서비스 레이어를 통해 활성 구독 정보 조회
        boolean isPremiumUser = subscriptionService.findActiveSubscriptionByUserId(userId)
                .map(subscription -> subscription.getSubscriptionType() == SubscriptionTier.PREMIUM)
                .orElse(false); // 구독 정보가 없으면 false

        // 5. 프리미엄 유저가 아니면 (STANDARD 등급이거나 구독 정보가 없으면) 결제 페이지로 리다이렉트
        if (!isPremiumUser) {
            response.sendRedirect("/subscription/pay"); // 리다이렉트할 결제 페이지 경로
            return false; // 컨트롤러로 요청이 더 이상 진행되지 않도록 false 반환
        }

        // 6. 프리미엄 유저이면 요청을 그대로 진행
        return true;
    }
}