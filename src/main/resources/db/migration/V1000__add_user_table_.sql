CREATE TABLE `users`
(
    `id`       bigint       NOT NULL AUTO_INCREMENT,
    `username` varchar(255) NULL NULL,
    `password` varchar(255) NULL NULL,
    `email`    varchar(255) NULL NULL,
    `role`     varchar(255) NULL NULL,
    `active`   bit(1)       NULL NULL,
    PRIMARY KEY (`id`)
);