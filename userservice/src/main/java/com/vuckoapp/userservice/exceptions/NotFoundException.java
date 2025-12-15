package com.vuckoapp.userservice.exceptions;

public class NotFoundException  extends RuntimeException {
    public NotFoundException() {
        super("Page not found");
    }
}
