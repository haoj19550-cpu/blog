package com.estudys.blog.service;

import com.estudys.blog.dto.AuthResponse;
import com.estudys.blog.dto.LoginRequest;
import com.estudys.blog.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}

