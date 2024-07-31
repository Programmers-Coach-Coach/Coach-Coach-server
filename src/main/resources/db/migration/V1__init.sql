DROP DATABASE IF EXISTS `coachcoach`;
CREATE DATABASE `coachcoach` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin*/ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `coachcoach`;

-- 테이블 생성
CREATE TABLE `users` (
                         `user_id` int NOT NULL AUTO_INCREMENT,
                         `nickname` varchar(45) NOT NULL,
                         `email` varchar(45) NOT NULL,
                         `password` varchar(128) NOT NULL,
                         `profile_image_url` varchar(200) DEFAULT NULL,
                         `gender` enum('M','W') DEFAULT NULL,
                         `local_info` varchar(100) DEFAULT NULL,
                         `introduction` text,
                         PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sports` (
                          `sports_id` int NOT NULL AUTO_INCREMENT,
                          `sports_name` varchar(45) NOT NULL,
                          PRIMARY KEY (`sports_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='운동 종목';

CREATE TABLE `coaches` (
                           `coach_id` int NOT NULL AUTO_INCREMENT,
                           `user_id` int NOT NULL,
                           `coach_introduction` text,
                           `created_at` timestamp NOT NULL,
                           `active_center` varchar(100) DEFAULT NULL,
                           `active_hours_on` int DEFAULT NULL,
                           `active_hours_off` int DEFAULT NULL,
                           `chatting_url` varchar(100) DEFAULT NULL,
                           `is_open` tinyint DEFAULT '1',
                           PRIMARY KEY (`coach_id`),
                           KEY `coaches_user_id_idx` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='코치 정보';

CREATE TABLE `routines` (
                            `routine_id` int NOT NULL AUTO_INCREMENT,
                            `user_id` int NOT NULL,
                            `coach_id` int DEFAULT NULL,
                            `sports_id` int NOT NULL,
                            `routine_name` varchar(45) NOT NULL,
                            PRIMARY KEY (`routine_id`),
                            KEY `routines_user_id_idx` (`user_id`),
                            KEY `routines_sports_id_idx` (`sports_id`),
                            KEY `routines_coach_id_idx` (`coach_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='운동 루틴';

CREATE TABLE `routine_categories` (
                                      `routine_category_id` int NOT NULL AUTO_INCREMENT,
                                      `routine_id` int NOT NULL,
                                      `category_name` varchar(45) NOT NULL,
                                      PRIMARY KEY (`routine_category_id`),
                                      KEY `routine_id_idx` (`routine_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='운동 루틴 카테고리';

CREATE TABLE `actions` (
                           `action_id` int NOT NULL AUTO_INCREMENT,
                           `routine_category_id` int NOT NULL,
                           `action_name` varchar(45) NOT NULL,
                           `count_or_minutes` varchar(45) DEFAULT NULL,
                           `set` int DEFAULT NULL,
                           `description` varchar(200) DEFAULT NULL,
                           PRIMARY KEY (`action_id`),
                           KEY `routine_category_id_idx` (`routine_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='운동 actions';

CREATE TABLE `coaching_sports` (
                                   `coaching_sports_id` int NOT NULL AUTO_INCREMENT,
                                   `coach_id` int NOT NULL,
                                   `sports_id` int NOT NULL,
                                   PRIMARY KEY (`coaching_sports_id`),
                                   KEY `coaching_sports_coach_id_idx` (`coach_id`),
                                   KEY `coaching_sports_sports_id_idx` (`sports_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='코칭 종목';

CREATE TABLE `completed_categories` (
                                        `completed_category_id` int NOT NULL AUTO_INCREMENT,
                                        `user_record_id` int NOT NULL,
                                        `routine_category_id` int NOT NULL,
                                        PRIMARY KEY (`completed_category_id`),
                                        KEY `completed_categories_idx` (`user_record_id`),
                                        KEY `completed_categories_routine_category_id_idx` (`routine_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='완료 카테고리';

CREATE TABLE `interested_sports` (
                                     `interested_sports_id` int NOT NULL AUTO_INCREMENT,
                                     `user_id` int NOT NULL,
                                     `sports_id` int NOT NULL,
                                     PRIMARY KEY (`interested_sports_id`),
                                     KEY `interested_sports_user_id_idx` (`user_id`),
                                     KEY `interested_sports_sports_id_idx` (`sports_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='관심 운동';

CREATE TABLE `notifications` (
                                 `notification_id` int NOT NULL AUTO_INCREMENT,
                                 `user_id` int NOT NULL,
                                 `message` varchar(100) NOT NULL,
                                 `is_reading` tinyint NOT NULL DEFAULT '0',
                                 `relation_function` varchar(45) NOT NULL,
                                 `created_at` timestamp NOT NULL,
                                 PRIMARY KEY (`notification_id`),
                                 KEY `notifications_user_id_idx` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='알림';

CREATE TABLE `reviews` (
                           `review_id` int NOT NULL AUTO_INCREMENT,
                           `user_id` int NOT NULL,
                           `coach_id` int NOT NULL,
                           `contents` text NOT NULL,
                           `stars` int NOT NULL,
                           `created_at` timestamp NOT NULL,
                           PRIMARY KEY (`review_id`),
                           KEY `reviews_user_id_idx` (`user_id`),
                           KEY `reviews_coach_id_idx` (`coach_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='리뷰';

CREATE TABLE `user_coach_likes` (
                                    `user_coach_like_id` int NOT NULL AUTO_INCREMENT,
                                    `user_id` int NOT NULL,
                                    `coach_id` int NOT NULL,
                                    PRIMARY KEY (`user_coach_like_id`),
                                    KEY `user_coach_likes_user_id_idx` (`user_id`),
                                    KEY `user_coach_likes_coach_id_idx` (`coach_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='관심 코치';

CREATE TABLE `user_coach_matching` (
                                       `user_coach_matching_id` int NOT NULL AUTO_INCREMENT,
                                       `user_id` int NOT NULL,
                                       `coach_id` int NOT NULL,
                                       `is_matching` tinyint NOT NULL DEFAULT '0',
                                       PRIMARY KEY (`user_coach_matching_id`),
                                       KEY `user_coach_matching_user_id_idx` (`user_id`),
                                       KEY `user_coach_matching_coach_id_idx` (`coach_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='매칭 회원';

CREATE TABLE `user_records` (
                                `user_record_id` int NOT NULL AUTO_INCREMENT,
                                `user_id` int NOT NULL,
                                `weight` int DEFAULT NULL,
                                `skeletal_muscle` int DEFAULT NULL,
                                `fat_percentage` int DEFAULT NULL,
                                `bmi` double DEFAULT NULL,
                                `created_at` date NOT NULL,
                                PRIMARY KEY (`user_record_id`),
                                KEY `user_records_user_id_idx` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='유저 기록';

-- 외래 키 제약 조건 추가
ALTER TABLE `actions`
    ADD CONSTRAINT `fk_actions_routine_category_id` FOREIGN KEY (`routine_category_id`) REFERENCES `routine_categories` (`routine_category_id`);

ALTER TABLE `coaches`
    ADD CONSTRAINT `coaches_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `coaching_sports`
    ADD CONSTRAINT `fk_coaching_sports_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
    ADD CONSTRAINT `fk_coaching_sports_sports_id` FOREIGN KEY (`sports_id`) REFERENCES `sports` (`sports_id`);

ALTER TABLE `completed_categories`
    ADD CONSTRAINT `fk_completed_categories_id` FOREIGN KEY (`user_record_id`) REFERENCES `user_records` (`user_record_id`),
    ADD CONSTRAINT `fk_completed_categories_routine_category_id` FOREIGN KEY (`routine_category_id`) REFERENCES `routine_categories` (`routine_category_id`);

ALTER TABLE `interested_sports`
    ADD CONSTRAINT `fk_interested_sports_sports_id` FOREIGN KEY (`sports_id`) REFERENCES `sports` (`sports_id`),
    ADD CONSTRAINT `fk_interested_sports_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `notifications`
    ADD CONSTRAINT `fk_notifications_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `reviews`
    ADD CONSTRAINT `fk_reviews_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
    ADD CONSTRAINT `fk_reviews_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `routine_categories`
    ADD CONSTRAINT `fk_routine_categories_routine_id` FOREIGN KEY (`routine_id`) REFERENCES `routines` (`routine_id`);

ALTER TABLE `routines`
    ADD CONSTRAINT `fk_routines_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
    ADD CONSTRAINT `fk_routines_sports_id` FOREIGN KEY (`sports_id`) REFERENCES `sports` (`sports_id`),
    ADD CONSTRAINT `fk_routines_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `user_coach_likes`
    ADD CONSTRAINT `fk_user_coach_likes_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
    ADD CONSTRAINT `fk_user_coach_likes_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `user_coach_matching`
    ADD CONSTRAINT `fk_user_coach_matching_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
    ADD CONSTRAINT `fk_user_coach_matching_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `user_records`
    ADD CONSTRAINT `fk_user_records_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
