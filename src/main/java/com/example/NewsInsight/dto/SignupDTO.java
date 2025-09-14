package com.example.NewsInsight.dto;

import lombok.Data;

@Data
public class SignupDTO {

    private String userid;
    private String password;
    private String confirm;
    private String email;
    private String nickname;
}
