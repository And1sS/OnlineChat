package com.and1ss.onlinechat.exceptions;

public class InvalidLoginCredentialsException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Incorrect login or password";
    }
}
