package com.example.taskmanager.exceptions;

public class LoginFailed extends RuntimeException {
    public LoginFailed(String message) {
        super(message);
    }
}
