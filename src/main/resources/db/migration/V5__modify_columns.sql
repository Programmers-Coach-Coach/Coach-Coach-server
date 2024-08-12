ALTER TABLE `coachcoach`.`coaching_sports`
    CHANGE COLUMN `updated_at` `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ;

ALTER TABLE `coachcoach`.`coaches`
    CHANGE COLUMN `is_open` `is_open` TINYINT NOT NULL DEFAULT 1 ;

ALTER TABLE `coachcoach`.`user_coach_likes`
    CHANGE COLUMN `updated_at` `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ;
