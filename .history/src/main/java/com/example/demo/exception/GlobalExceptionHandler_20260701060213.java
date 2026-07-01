package com.example.demo.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.api.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleRuntime(RuntimeException ex) {
        return ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<?> handleDuplicate(DataIntegrityViolationException ex) {
        return ApiResponse.builder()
                .success(false)
                .message("Email already exists")
                .status(HttpStatus.CONFLICT.value())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleGeneral(Exception ex) {

        ex.printStackTrace(); // ✅ IMPORTANT (see real error in console)

        return ApiResponse.builder()
                .success(false)
                .message(ex.getMessage()) // ✅ show real error
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }

}
