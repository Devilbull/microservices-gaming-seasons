package com.vuckoapp.userservice.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException() {
        super("User with this email already exists");
    }
}
