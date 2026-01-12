package com.example.taskmanager.exceptions;

public class EmailDoesntExist extends RuntimeException {
    public EmailDoesntExist(String message) {
        super(message);
    }
}
