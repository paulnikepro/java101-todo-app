package org.paulnikepro.hw4.todoapp.exception;

public class ErrorResponseDto {
    private final String message;
    private final String code;

    public ErrorResponseDto(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
