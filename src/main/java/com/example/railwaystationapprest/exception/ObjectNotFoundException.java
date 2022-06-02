package com.example.railwaystationapprest.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String message) {
        super(message + " does not exist");
    }
}

