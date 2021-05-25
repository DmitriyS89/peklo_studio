package com.peklo.peklo.models.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Integer makeToken() {
        return new Random().nextInt(900000) + 100000;
    }
}
