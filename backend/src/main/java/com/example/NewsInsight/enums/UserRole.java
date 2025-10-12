package com.example.NewsInsight.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ADMIN("ROLE_ADMIN", "관리자"),
    GUEST("ROLE_GUEST", "게스트"),
    USER("ROLE_USER", "유저");

    private final String key;
    private final String description;
}
