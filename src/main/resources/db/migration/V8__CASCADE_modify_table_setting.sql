# user_record 테이블 삭제
DROP TABLE `coachcoach`.`user_records`;

# record_data 테이블 생성
CREATE TABLE `coachcoach`.`record_data`
(
    `record_data_id`  BIGINT    NOT NULL AUTO_INCREMENT,
    `user_id`         BIGINT    NOT NULL,
    `weight`          INT       NULL,
    `skeletal_muscle` INT       NULL,
    `fat_percentage`  INT       NULL,
    `bmi`             DOUBLE    NULL,
    `record_date`     DATE      NULL,
    `created_at`      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`record_data_id`),
    INDEX `fk_record_data_user_id_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_record_data_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4;


# CASCADE 추가
ALTER TABLE `coachcoach`.`coaches`
    DROP FOREIGN KEY `coaches_user_id`;
ALTER TABLE `coachcoach`.`coaches`
    ADD CONSTRAINT `coaches_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`refresh_tokens`
    DROP FOREIGN KEY `fk_refresh_tokens_user_id`;
ALTER TABLE `coachcoach`.`refresh_tokens`
    ADD CONSTRAINT `fk_refresh_tokens_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`user_coach_matching`
    DROP FOREIGN KEY `fk_user_coach_matching_user_id`;
ALTER TABLE `coachcoach`.`user_coach_matching`
    ADD CONSTRAINT `fk_user_coach_matching_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`user_coach_likes`
    DROP FOREIGN KEY `fk_user_coach_likes_user_id`;
ALTER TABLE `coachcoach`.`user_coach_likes`
    ADD CONSTRAINT `fk_user_coach_likes_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`notifications`
    DROP FOREIGN KEY `fk_notifications_user_id`;
ALTER TABLE `coachcoach`.`notifications`
    ADD CONSTRAINT `fk_notifications_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`interested_sports`
    DROP FOREIGN KEY `fk_interested_sports_user_id`;
ALTER TABLE `coachcoach`.`interested_sports`
    ADD CONSTRAINT `fk_interested_sports_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`routines`
    DROP FOREIGN KEY `fk_routines_user_id`;
ALTER TABLE `coachcoach`.`routines`
    ADD CONSTRAINT `fk_routines_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`routine_categories`
    DROP FOREIGN KEY `fk_routine_categories_routine_id`;
ALTER TABLE `coachcoach`.`routine_categories`
    ADD CONSTRAINT `fk_routine_categories_routine_id`
        FOREIGN KEY (`routine_id`)
            REFERENCES `coachcoach`.`routines` (`routine_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`actions`
    DROP FOREIGN KEY `fk_actions_routine_category_id`;
ALTER TABLE `coachcoach`.`actions`
    ADD CONSTRAINT `fk_actions_routine_category_id`
        FOREIGN KEY (`routine_category_id`)
            REFERENCES `coachcoach`.`routine_categories` (`routine_category_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`coaching_sports`
    DROP FOREIGN KEY `fk_coaching_sports_coach_id`;
ALTER TABLE `coachcoach`.`coaching_sports`
    ADD CONSTRAINT `fk_coaching_sports_coach_id`
        FOREIGN KEY (`coach_id`)
            REFERENCES `coachcoach`.`coaches` (`coach_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`completed_categories`
    DROP FOREIGN KEY `fk_completed_categories_id`;
ALTER TABLE `coachcoach`.`completed_categories`
    DROP COLUMN `user_record_id`,
    ADD COLUMN `user_id`     BIGINT NOT NULL AFTER `completed_category_id`, #user_id 추가
    ADD COLUMN `record_date` DATE   NULL AFTER `updated_at`, #record_date 추가
    ADD INDEX `fk_completed_categories_user_id_idx` (`user_id` ASC) VISIBLE,
    DROP INDEX `completed_categories_idx`;

ALTER TABLE `coachcoach`.`completed_categories`
    ADD CONSTRAINT `fk_completed_categories_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION;

# user_id null값으로 변경
ALTER TABLE `coachcoach`.`reviews`
    DROP FOREIGN KEY `fk_reviews_user_id`;
ALTER TABLE `coachcoach`.`reviews`
    CHANGE COLUMN `user_id` `user_id` BIGINT NULL;
ALTER TABLE `coachcoach`.`reviews`
    ADD CONSTRAINT `fk_reviews_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`);
