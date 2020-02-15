package com.fredoliveira.javaresttemplate.app.web.handler;

import com.fredoliveira.javaresttemplate.domain.exception.ApiException;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.fredoliveira.javaresttemplate.domain.exception.ApiException.ApiExceptionType.INTERNAL_SERVER_ERROR;
import static com.fredoliveira.javaresttemplate.domain.exception.ApiException.ApiExceptionType.NOT_FOUND;


@Order(Ordered.HIGHEST_PRECEDENCE) public @RestControllerAdvice class GlobalExceptionHandler {

    public @ExceptionHandler(Exception.class) ResponseEntity<ApiExceptionResponse> handleGenericException(Exception e) {
        final var response = ApiExceptionResponse.builder()
                .type(INTERNAL_SERVER_ERROR.toString())
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }


    public @ExceptionHandler(ApiException.class) ResponseEntity<ApiExceptionResponse> handleApiException(ApiException e) {
        final var response = ApiExceptionResponse.builder()
                .type(e.type.toString())
                .message(e.message)
                .build();

        if (e.type.equals(NOT_FOUND)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(response);
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @Data @Builder private static class ApiExceptionResponse {
        private String type;
        private String message;
    }

}
