package com.example.NewsInsight.dto;

import com.example.NewsInsight.enums.ProviderType;
import com.example.NewsInsight.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Integer id;
    private String userid;
    private String email;
    private String nickname;
    private ProviderType provider;
    private UserRole role;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
