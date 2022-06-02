package com.example.railwaystationapprest.exception;

public class ObjectCannotBeUpdatedException extends RuntimeException{
    public ObjectCannotBeUpdatedException(String message) {
        super("The " + message + " cannot be changed");
    }
}