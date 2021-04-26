package com.peklo.peklo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UrlNotCorrect extends RuntimeException {
    public UrlNotCorrect() {
    }

    public UrlNotCorrect(String message) {
        super(message);
    }
}
