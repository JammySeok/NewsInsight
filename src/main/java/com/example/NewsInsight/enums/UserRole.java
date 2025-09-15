package com.example.NewsInsight.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    ADMIN(0, "ROLE_ADMIN", "관리자"),
    GUEST(1, "ROLE_GUEST", "게스트"),
    USER(2, "ROLE_USER", "유저");

    private final Integer code;
    private final String key;
    private final String description;

}
