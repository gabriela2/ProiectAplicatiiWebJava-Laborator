package com.example.railwaystationapprest.exception;

public class ObjectCannotBeCreatedException extends RuntimeException{
    public ObjectCannotBeCreatedException(String message) {
        super(message + " cannot be created");
    }
}
