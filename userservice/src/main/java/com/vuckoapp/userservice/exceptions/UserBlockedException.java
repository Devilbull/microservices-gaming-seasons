package com.vuckoapp.userservice.exceptions;

public class UserBlockedException extends RuntimeException {
    public UserBlockedException() {
        super("Your account is blocked");
    }
}
