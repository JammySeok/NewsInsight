package com.example.NewsInsight.service;

import com.example.NewsInsight.entity.SubscriptionEntity;
import java.util.Optional;

public interface SubscriptionService {
    Optional<SubscriptionEntity> findActiveSubscriptionByUserId(Integer userId);
    void createOrUpdateSubscription(Integer userId);
}
