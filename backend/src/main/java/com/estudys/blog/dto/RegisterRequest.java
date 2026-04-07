package com.estudys.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "用户注册请求")
public class RegisterRequest {
    @NotBlank(message = "username is required")
    @Size(min = 4, max = 20, message = "username length must be 4-20")
    @Schema(description = "用户名", example = "estudys")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "email format invalid")
    @Schema(description = "邮箱", example = "estudys@example.com")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 32, message = "password length must be 6-32")
    @Schema(description = "登录密码(明文传输，后端加密存储)", example = "123456")
    private String password;

    @NotBlank(message = "nickname is required")
    @Size(max = 20, message = "nickname length must <= 20")
    @Schema(description = "昵称", example = "学习者")
    private String nickname;
}

