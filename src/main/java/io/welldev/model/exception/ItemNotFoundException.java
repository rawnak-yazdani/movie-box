package io.welldev.model.exception;

import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
