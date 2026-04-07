package com.estudys.blog.controller;

import com.estudys.blog.common.Result;
import com.estudys.blog.dto.AuthResponse;
import com.estudys.blog.dto.LoginRequest;
import com.estudys.blog.dto.RegisterRequest;
import com.estudys.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证模块", description = "注册与登录接口")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "用户注册", description = "注册成功后返回 token 和用户信息")
    @PostMapping("/register")
    public Result<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.ok(authService.register(request));
    }

    @Operation(summary = "用户登录", description = "登录成功后返回 token 和用户信息")
    @PostMapping("/login")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.ok(authService.login(request));
    }
}

