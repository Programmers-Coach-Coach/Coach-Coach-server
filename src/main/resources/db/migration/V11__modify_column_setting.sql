ALTER TABLE `coachcoach`.`completed_categories`
    CHANGE COLUMN `record_date` `record_date` DATE NOT NULL;

ALTER TABLE `coachcoach`.`user_coach_matching`
    CHANGE COLUMN `is_matching` `is_matching` TINYINT NOT NULL DEFAULT 0;

ALTER TABLE `coachcoach`.`reviews`
    DROP FOREIGN KEY `fk_reviews_user_id`;
ALTER TABLE `coachcoach`.`reviews`
    CHANGE COLUMN `user_id` `user_id` BIGINT NOT NULL;
ALTER TABLE `coachcoach`.`reviews`
    ADD CONSTRAINT `fk_reviews_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`);
