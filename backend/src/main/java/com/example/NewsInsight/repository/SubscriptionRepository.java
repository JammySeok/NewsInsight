package com.example.NewsInsight.repository;

import com.example.NewsInsight.entity.SubscriptionEntity;
import com.example.NewsInsight.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Integer> {
    Optional<SubscriptionEntity> findByUserIdAndIsActiveTrue(Integer userId);
    Optional<SubscriptionEntity> findByUserAndIsActiveTrue(UserEntity user);
}
