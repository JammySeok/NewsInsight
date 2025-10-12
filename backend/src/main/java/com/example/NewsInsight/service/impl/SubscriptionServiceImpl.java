package com.example.NewsInsight.service.impl;

import com.example.NewsInsight.entity.SubscriptionEntity;
import com.example.NewsInsight.entity.UserEntity;
import com.example.NewsInsight.enums.SubscriptionTier;
import com.example.NewsInsight.repository.SubscriptionRepository;
import com.example.NewsInsight.repository.UserRepository;
import com.example.NewsInsight.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionEntity> findActiveSubscriptionByUserId(Integer userId) {
        return subscriptionRepository.findByUserIdAndIsActiveTrue(userId);
    }

    @Override
    @Transactional
    public void createOrUpdateSubscription(Integer userId) {
        // 1. 사용자 엔티티를 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. id=" + userId));

        // 2. 새로운 구독 정보 생성
        SubscriptionEntity newSubscription = new SubscriptionEntity();
        newSubscription.setUser(user);
        newSubscription.setSubscriptionType(SubscriptionTier.PREMIUM); // 프리미엄 등급
        newSubscription.setStartDate(LocalDateTime.now());
        newSubscription.setEndDate(LocalDateTime.now().plusMonths(1)); // 1개월 구독
        newSubscription.setIsActive(true);

        // TODO: 기존에 활성 구독이 있다면 비활성화 처리하는 로직 추가 가능

        // 3. DB에 저장
        subscriptionRepository.save(newSubscription);
    }
}
