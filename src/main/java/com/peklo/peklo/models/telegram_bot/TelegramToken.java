package com.peklo.peklo.models.telegram_bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "telegramtokens")
public class TelegramToken {

    @Id
    private Long userId;

    private String token;
}
