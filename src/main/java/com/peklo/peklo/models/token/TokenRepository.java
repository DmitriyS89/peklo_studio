package com.peklo.peklo.models.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Boolean existsByToken(Integer token);
    Optional<Token> findByToken(Integer token);
}
