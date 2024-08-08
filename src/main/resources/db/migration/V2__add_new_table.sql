CREATE TABLE IF NOT EXISTS `refresh_tokens`
(
    `refresh_token_id` bigint                           NOT NULL,
    `user_id`          bigint                           NOT NULL,
    `expire_date`      timestamp                        NOT NULL,
    `refresh_token`    varchar(200) COLLATE utf8mb4_bin NOT NULL,
    `created_at`       timestamp                        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`refresh_token_id`),
    KEY `fk_refresh_tokens_user_id_idx` (`user_id`),
    CONSTRAINT `fk_refresh_tokens_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
