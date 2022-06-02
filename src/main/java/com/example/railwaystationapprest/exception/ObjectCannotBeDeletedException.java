package com.example.railwaystationapprest.exception;

public class ObjectCannotBeDeletedException extends RuntimeException{
    public ObjectCannotBeDeletedException(String message) {
        super(message +" cannot be deleted");
    }
}
