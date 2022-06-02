package com.example.railwaystationapprest.exception;

public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException() {
        super("The credentials used are not correct");
    }
}