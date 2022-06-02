package com.example.railwaystationapprest.exception;

public class ObjectAlreadyExistsException extends RuntimeException{
    public ObjectAlreadyExistsException(String message) {
        super(message + " already exists in the database");
    }
}