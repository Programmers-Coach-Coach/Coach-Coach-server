DROP DATABASE IF EXISTS `coachcoach`;
CREATE DATABASE `coachcoach` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin*/ /*!80016 DEFAULT ENCRYPTION = 'N' */;
USE `coachcoach`;

-- 테이블 생성
CREATE TABLE `users`
(
    `user_id`           bigint       NOT NULL AUTO_INCREMENT,
    `nickname`          varchar(45)  NOT NULL,
    `email`             varchar(45)  NOT NULL,
    `password`          varchar(200) NOT NULL,
    `profile_image_url` varchar(400)          DEFAULT NULL,
    `gender`            enum ('M','W')        DEFAULT NULL,
    `local_info`        varchar(200)          DEFAULT NULL,
    `introduction`      text,
    `created_at`        timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `sports`
(
    `sport_id`        bigint       NOT NULL AUTO_INCREMENT,
    `sport_name`      varchar(45)  NOT NULL,
    `sport_image_url` varchar(400) NOT NULL,
    `created_at`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`sport_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `coaches`
(
    `coach_id`           bigint       NOT NULL AUTO_INCREMENT,
    `user_id`            bigint       NOT NULL,
    `coach_introduction` text         NOT NULL,
    `active_center`      varchar(200) NOT NULL,
    `active_hours_on`    int          NOT NULL,
    `active_hours_off`   int          NOT NULL,
    `chatting_url`       varchar(400) NOT NULL,
    `is_open`            tinyint      NOT NULL DEFAULT '1',
    `created_at`         timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`         timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`coach_id`),
    KEY `coaches_user_id_idx` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `routines`
(
    `routine_id`   bigint      NOT NULL AUTO_INCREMENT,
    `user_id`      bigint      NOT NULL,
    `coach_id`     bigint               DEFAULT NULL,
    `sport_id`     bigint      NOT NULL,
    `routine_name` varchar(45) NOT NULL,
    `created_at`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`routine_id`),
    KEY `routines_user_id_idx` (`user_id`),
    KEY `routines_sport_id_idx` (`sport_id`),
    KEY `routines_coach_id_idx` (`coach_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `routine_categories`
(
    `routine_category_id` bigint      NOT NULL AUTO_INCREMENT,
    `routine_id`          bigint      NOT NULL,
    `category_name`       varchar(45) NOT NULL,
    `created_at`          timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`routine_category_id`),
    KEY `routine_id_idx` (`routine_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `actions`
(
    `action_id`           bigint      NOT NULL AUTO_INCREMENT,
    `routine_category_id` bigint      NOT NULL,
    `action_name`         varchar(45) NOT NULL,
    `count_or_minutes`    varchar(45)          DEFAULT NULL,
    `set`                 int                  DEFAULT NULL,
    `description`         varchar(200)         DEFAULT NULL,
    `created_at`          timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`action_id`),
    KEY `routine_category_id_idx` (`routine_category_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='운동 actions';

CREATE TABLE `user_records`
(
    `user_record_id`  bigint    NOT NULL AUTO_INCREMENT,
    `user_id`         bigint    NOT NULL,
    `weight`          int                DEFAULT NULL,
    `skeletal_muscle` int                DEFAULT NULL,
    `fat_percentage`  int                DEFAULT NULL,
    `bmi`             double             DEFAULT NULL,
    `created_at`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_record_id`),
    KEY `user_records_user_id_idx` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `notifications`
(
    `notification_id`   bigint                       NOT NULL AUTO_INCREMENT,
    `user_id`           bigint                       NOT NULL,
    `message`           varchar(100)                 NOT NULL,
    `is_reading`        tinyint                      NOT NULL DEFAULT '0',
    `relation_function` enum ('review','ask','like') NOT NULL,
    `created_at`        timestamp                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        timestamp                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`notification_id`),
    KEY `notifications_user_id_idx` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `reviews`
(
    `review_id`  bigint    NOT NULL AUTO_INCREMENT,
    `user_id`    bigint    NOT NULL,
    `coach_id`   bigint    NOT NULL,
    `contents`   text      NOT NULL,
    `stars`      int       NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`review_id`),
    KEY `reviews_user_id_idx` (`user_id`),
    KEY `reviews_coach_id_idx` (`coach_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `interested_sports`
(
    `interested_sport_id` bigint    NOT NULL AUTO_INCREMENT,
    `user_id`             bigint    NOT NULL,
    `sport_id`            bigint    NOT NULL,
    `created_at`          timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`interested_sport_id`),
    KEY `interested_sports_user_id_idx` (`user_id`),
    KEY `interested_sports_sport_id_idx` (`sport_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `user_coach_likes`
(
    `user_coach_like_id` bigint    NOT NULL AUTO_INCREMENT,
    `user_id`            bigint    NOT NULL,
    `coach_id`           bigint    NOT NULL,
    `created_at`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_coach_like_id`),
    KEY `user_coach_likes_user_id_idx` (`user_id`),
    KEY `user_coach_likes_coach_id_idx` (`coach_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `user_coach_matching`
(
    `user_coach_matching_id` bigint    NOT NULL AUTO_INCREMENT,
    `user_id`                bigint    NOT NULL,
    `coach_id`               bigint    NOT NULL,
    `is_matching`            tinyint   NOT NULL DEFAULT '0',
    `created_at`             timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_coach_matching_id`),
    KEY `user_coach_matching_user_id_idx` (`user_id`),
    KEY `user_coach_matching_coach_id_idx` (`coach_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `coaching_sports`
(
    `coaching_sport_id` bigint    NOT NULL AUTO_INCREMENT,
    `coach_id`          bigint    NOT NULL,
    `sport_id`          bigint    NOT NULL,
    `created_at`        timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`coaching_sport_id`),
    KEY `coaching_sports_coach_id_idx` (`coach_id`),
    KEY `coaching_sports_sport_id_idx` (`sport_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

CREATE TABLE `completed_categories`
(
    `completed_category_id` bigint    NOT NULL AUTO_INCREMENT,
    `user_record_id`        bigint    NOT NULL,
    `routine_category_id`   bigint    NOT NULL,
    `created_at`            timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`completed_category_id`),
    KEY `completed_categories_idx` (`user_record_id`),
    KEY `completed_categories_routine_category_id_idx` (`routine_category_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

-- 외래 키 제약 조건 추가
ALTER TABLE `actions`
    ADD CONSTRAINT `fk_actions_routine_category_id` FOREIGN KEY (`routine_category_id`) REFERENCES `routine_categories` (`routine_category_id`);

ALTER TABLE `coaches`
    ADD CONSTRAINT `coaches_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `routines`
    ADD CONSTRAINT `fk_routines_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
    ADD CONSTRAINT `fk_routines_sport_id` FOREIGN KEY (`sport_id`) REFERENCES `sports` (`sport_id`),
    ADD CONSTRAINT `fk_routines_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `routine_categories`
    ADD CONSTRAINT `fk_routine_categories_routine_id` FOREIGN KEY (`routine_id`) REFERENCES `routines` (`routine_id`);

ALTER TABLE `user_records`
    ADD CONSTRAINT `fk_user_records_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `notifications`
    ADD CONSTRAINT `fk_notifications_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `reviews`
    ADD CONSTRAINT `fk_reviews_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
    ADD CONSTRAINT `fk_reviews_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `interested_sports`
    ADD CONSTRAINT `fk_interested_sports_sport_id` FOREIGN KEY (`sport_id`) REFERENCES `sports` (`sport_id`),
    ADD CONSTRAINT `fk_interested_sports_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `user_coach_likes`
    ADD CONSTRAINT `fk_user_coach_likes_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
    ADD CONSTRAINT `fk_user_coach_likes_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `user_coach_matching`
    ADD CONSTRAINT `fk_user_coach_matching_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
    ADD CONSTRAINT `fk_user_coach_matching_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `coaching_sports`
    ADD CONSTRAINT `fk_coaching_sports_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
    ADD CONSTRAINT `fk_coaching_sports_sport_id` FOREIGN KEY (`sport_id`) REFERENCES `sports` (`sport_id`);

ALTER TABLE `completed_categories`
    ADD CONSTRAINT `fk_completed_categories_id` FOREIGN KEY (`user_record_id`) REFERENCES `user_records` (`user_record_id`),
    ADD CONSTRAINT `fk_completed_categories_routine_category_id` FOREIGN KEY (`routine_category_id`) REFERENCES `routine_categories` (`routine_category_id`);
