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
