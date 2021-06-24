CREATE TABLE `telegramtokens`
(
    `user_id` bigint NOT NULL,
    `token`   varchar(255) DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci