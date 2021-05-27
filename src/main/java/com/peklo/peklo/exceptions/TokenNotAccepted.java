package com.peklo.peklo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TokenNotAccepted extends RuntimeException{
    public TokenNotAccepted() {
    }

    public TokenNotAccepted(String message) {
        super(message);
    }
}
