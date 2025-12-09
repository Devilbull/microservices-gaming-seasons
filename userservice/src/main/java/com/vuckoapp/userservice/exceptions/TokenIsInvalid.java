package com.vuckoapp.userservice.exceptions;

public class TokenIsInvalid extends RuntimeException {
    public TokenIsInvalid() {
        super("The provided token is invalid.");
    }
}
