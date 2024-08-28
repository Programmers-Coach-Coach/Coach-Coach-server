ALTER TABLE `coachcoach`.`routine_categories`
DROP FOREIGN KEY `fk_routine_categories_routine_id`;
ALTER TABLE `coachcoach`.`routine_categories`
    CHANGE COLUMN `routine_id` `routine_id` BIGINT NULL ;
ALTER TABLE `coachcoach`.`routine_categories`
    ADD CONSTRAINT `fk_routine_categories_routine_id`
        FOREIGN KEY (`routine_id`)
            REFERENCES `coachcoach`.`routines` (`routine_id`)
            ON DELETE SET NULL;

ALTER TABLE `coachcoach`.`completed_categories`
DROP FOREIGN KEY `fk_completed_categories_routine_category_id`;
ALTER TABLE `coachcoach`.`completed_categories`
    ADD CONSTRAINT `fk_completed_categories_routine_category_id`
        FOREIGN KEY (`routine_category_id`)
            REFERENCES `coachcoach`.`routine_categories` (`routine_category_id`)
            ON DELETE RESTRICT;

ALTER TABLE `coachcoach`.`routines`
DROP FOREIGN KEY `fk_routines_coach_id`;
ALTER TABLE `coachcoach`.`routines`
    ADD CONSTRAINT `fk_routines_coach_id`
        FOREIGN KEY (`coach_id`)
            REFERENCES `coachcoach`.`coaches` (`coach_id`)
            ON DELETE SET NULL;

ALTER TABLE `coachcoach`.`reviews`
DROP FOREIGN KEY `fk_reviews_coach_id`;
ALTER TABLE `coachcoach`.`reviews`
    ADD CONSTRAINT `fk_reviews_coach_id`
        FOREIGN KEY (`coach_id`)
            REFERENCES `coachcoach`.`coaches` (`coach_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`routines`
DROP FOREIGN KEY `fk_routines_coach_id`;
ALTER TABLE `coachcoach`.`routines`
    ADD CONSTRAINT `fk_routines_coach_id`
        FOREIGN KEY (`coach_id`)
            REFERENCES `coachcoach`.`coaches` (`coach_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`user_coach_likes`
DROP FOREIGN KEY `fk_user_coach_likes_coach_id`;
ALTER TABLE `coachcoach`.`user_coach_likes`
    ADD CONSTRAINT `fk_user_coach_likes_coach_id`
        FOREIGN KEY (`coach_id`)
            REFERENCES `coachcoach`.`coaches` (`coach_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`user_coach_matching`
DROP FOREIGN KEY `fk_user_coach_matching_coach_id`;
ALTER TABLE `coachcoach`.`user_coach_matching`
    ADD CONSTRAINT `fk_user_coach_matching_coach_id`
        FOREIGN KEY (`coach_id`)
            REFERENCES `coachcoach`.`coaches` (`coach_id`)
            ON DELETE CASCADE;


