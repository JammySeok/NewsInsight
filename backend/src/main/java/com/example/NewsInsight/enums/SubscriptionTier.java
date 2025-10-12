package com.example.NewsInsight.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionTier {
    STANDARD("STANDARD"),
    PREMIUM("PREMIUM");

    private final String tier;
}
