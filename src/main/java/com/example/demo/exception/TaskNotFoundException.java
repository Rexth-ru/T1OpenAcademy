package com.example.demo.exception;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(final String message) {
        super(message);
    }
}
