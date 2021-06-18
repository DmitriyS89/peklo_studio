CREATE TABLE `tool_1_items`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `css_path`   varchar(255) DEFAULT NULL,
    `from_url`   varchar(255) DEFAULT NULL,
    `html_value` longtext,
    `time`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci