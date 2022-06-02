package com.example.railwaystationapprest.exception;

public class BalanceUpdateException extends RuntimeException{
    public BalanceUpdateException() {
        super("The balance of the account cannot be less than 0");
    }
}

