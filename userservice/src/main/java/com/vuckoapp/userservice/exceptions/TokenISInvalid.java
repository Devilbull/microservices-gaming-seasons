package com.vuckoapp.userservice.exceptions;

public class TokenISInvalid extends RuntimeException {
    public TokenISInvalid() {
        super("The provided token is invalid.");
    }
}
