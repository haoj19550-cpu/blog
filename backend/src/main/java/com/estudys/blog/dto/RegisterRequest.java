package com.estudys.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "username is required")
    @Size(min = 4, max = 20, message = "username length must be 4-20")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "email format invalid")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 32, message = "password length must be 6-32")
    private String password;

    @NotBlank(message = "nickname is required")
    @Size(max = 20, message = "nickname length must <= 20")
    private String nickname;
}

