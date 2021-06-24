package com.peklo.peklo.models.telegram_bot;

import com.peklo.peklo.exceptions.TelegramTokenNotCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelegramTokenService {

    private final TelegramTokenRepository telegramTokenRepository;

    public String findTgToken(Long userId) {
        Optional<TelegramToken> tokenUserEmail = getTokenWithUserEmail(userId);
        if(tokenUserEmail.isEmpty()){
            return saveToken(userId, newToken());
        }else{
            return tokenUserEmail.get().getToken();
        }
    }

    public String saveToken(Long userId, String newToken) {
        TelegramToken telegramToken = new TelegramToken(userId, newToken);
        telegramTokenRepository.save(telegramToken);
        if(getTokenWithUserEmail(userId).isPresent()) {
            return telegramToken.getToken();
        }else {
            throw new TelegramTokenNotCreated();
        }
    }


    public String newToken() {
        return UUID.randomUUID().toString();
    }

    public Optional<TelegramToken> getTokenWithUserEmail(Long userId) {
        return telegramTokenRepository.findById(userId);
    }

    public Optional<TelegramToken> getTokenWithUserToken(String token) {
        return telegramTokenRepository.findByToken(token);
    }


    public Optional<Long> closeToken(String token) {
        Optional<TelegramToken> tokenWithUserToken = getTokenWithUserToken(token);
        if(tokenWithUserToken.isPresent()){
            TelegramToken telegramToken = tokenWithUserToken.get();
            telegramTokenRepository.delete(telegramToken);
            return Optional.of(telegramToken.getUserId());
        }
        return Optional.empty();
    }
}
