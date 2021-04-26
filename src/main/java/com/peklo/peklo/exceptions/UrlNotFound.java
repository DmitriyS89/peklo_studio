package com.peklo.peklo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UrlNotFound extends RuntimeException {

    public UrlNotFound() {
    }
}
