package com.estudys.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "用户登录请求")
public class LoginRequest {
    @NotBlank(message = "username is required")
    @Schema(description = "用户名", example = "estudys")
    private String username;

    @NotBlank(message = "password is required")
    @Schema(description = "登录密码", example = "123456")
    private String password;
}

