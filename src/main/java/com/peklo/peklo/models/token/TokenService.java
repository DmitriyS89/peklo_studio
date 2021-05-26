package com.peklo.peklo.models.token;

import com.peklo.peklo.exceptions.TokenNotFound;
import com.peklo.peklo.models.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Integer makeToken() {
        return new Random().nextInt(900000) + 100000;
    }

    public Boolean findToken(Integer token){
        return tokenRepository.existsByToken(token);
    }

    public Token getToken(Integer token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(TokenNotFound::new);
    }

    public Token saveToken(User dto, Integer tokenCode) {
        Token token = Token.builder()
                .time(new Date(System.currentTimeMillis()))
                .token(tokenCode)
                .userId(dto)
                .build();
        tokenRepository.save(token);
        return token;
    }

    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }
}
