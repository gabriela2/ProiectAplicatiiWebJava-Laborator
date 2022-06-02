package com.example.railwaystationapprest.exception;

public class InvalidUpdateException extends RuntimeException{
    public InvalidUpdateException() {
        super("The id of the object does not match the id from the path");
    }
}

