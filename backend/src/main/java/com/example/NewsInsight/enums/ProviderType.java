package com.example.NewsInsight.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderType {
    LOCAL("local"),
    GOOGLE("google"),
    NAVER("naver");
    // KAKAO("kakao"), FACEBOOK("facebook"), GITHUB("github") 등 필요에 따라 추가

    private final String registrationId;
}