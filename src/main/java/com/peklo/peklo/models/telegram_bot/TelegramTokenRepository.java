package com.peklo.peklo.models.telegram_bot;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramTokenRepository extends CrudRepository<TelegramToken, Long> {
    Optional<TelegramToken> findByToken(String token);
}
