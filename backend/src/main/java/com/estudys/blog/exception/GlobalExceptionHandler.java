package com.estudys.blog.exception;

import com.estudys.blog.common.Result;
import jakarta.validation.ConstraintViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException ex, HttpServletRequest request) {
        log.warn("Business exception on {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return Result.fail(ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public Result<Void> handleValidation(Exception ex, HttpServletRequest request) {
        log.warn("Validation exception on {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return Result.fail("request params invalid");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleOther(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception on {} {}", request.getMethod(), request.getRequestURI(), ex);
        return Result.fail("server internal error");
    }
}

