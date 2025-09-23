package com.example.NewsInsight.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupDTO {

    @NotBlank(message = "{blank.id}")
    @Size(min = 4, max = 20, message = "{signup.id.error.length}")
    @Pattern(regexp = "^[a-z0-9]+$", message = "{signup.id.error.format}")
    private String userid;

    @NotBlank(message = "{blank.password}")
    @Size(min = 4, max = 16, message = "{signup.password.error.length}")
    private String password;

    @NotBlank(message = "{blank.password.confirm}")
    private String confirm;

    @NotBlank(message = "{blank.email}")
    @Email(message = "{signup.email.form}")
    private String email;

    @NotBlank(message = "{blank.nickname}")
    private String nickname;
}
