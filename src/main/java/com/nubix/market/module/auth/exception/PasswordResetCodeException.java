package com.nubix.market.module.auth.exception;

public class PasswordResetCodeException extends RuntimeException {

    private final String errorCode;

    public PasswordResetCodeException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
