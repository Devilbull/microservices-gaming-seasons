package com.vuckoapp.userservice.exceptions;

public class UserNotActivatedException extends RuntimeException {
    public UserNotActivatedException() {
        super("Please activate your account via e-mail");
    }
}
