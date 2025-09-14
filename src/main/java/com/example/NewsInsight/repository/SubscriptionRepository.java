package com.example.NewsInsight.repository;

import com.example.NewsInsight.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Integer> {
}
