package com.estudys.blog.security;

import com.estudys.blog.exception.BusinessException;
import com.estudys.blog.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (!needAuth(request)) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException("authorization token is required");
        }
        String token = authorization.substring(7);
        Long userId = jwtUtil.parseUserId(token);
        request.setAttribute(CURRENT_USER_ID, userId);
        return true;
    }

    private boolean needAuth(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (method == null) {
            return false;
        }
        if (!uri.startsWith("/api/articles")) {
            return false;
        }
        return HttpMethod.POST.matches(method)
                || HttpMethod.PUT.matches(method)
                || HttpMethod.DELETE.matches(method);
    }
}

