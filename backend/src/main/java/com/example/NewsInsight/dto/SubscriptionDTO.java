package com.example.NewsInsight.dto;

import com.example.NewsInsight.enums.SubscriptionTier;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class SubscriptionDTO {
    private Integer id; // Integer 타입으로 변경
    private SubscriptionTier subscriptionType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;

    public Long getRemainingDays() {
        if (endDate == null) {
            return null;
        }
        return ChronoUnit.DAYS.between(LocalDateTime.now(), endDate);
    }
}