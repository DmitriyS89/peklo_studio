package com.peklo.peklo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TelegramTokenNotCreated extends RuntimeException {

    public TelegramTokenNotCreated() {
        super();
    }
}
