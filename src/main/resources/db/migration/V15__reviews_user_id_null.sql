ALTER TABLE `coachcoach`.`reviews`
DROP FOREIGN KEY `fk_reviews_user_id`;
ALTER TABLE `coachcoach`.`reviews`
    CHANGE COLUMN `user_id` `user_id` BIGINT NULL;
ALTER TABLE `coachcoach`.`reviews`
    ADD CONSTRAINT `fk_reviews_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE SET NULL;
