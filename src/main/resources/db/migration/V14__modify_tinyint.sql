ALTER TABLE `coachcoach`.`users`
    CHANGE COLUMN `is_coach` `is_coach` BIT(1) NOT NULL DEFAULT b'0' ;

ALTER TABLE `coachcoach`.`coaches`
    CHANGE COLUMN `is_open` `is_open` BIT(1) NOT NULL DEFAULT b'1' ;

ALTER TABLE `coachcoach`.`user_coach_matching`
    CHANGE COLUMN `is_matching` `is_matching` BIT(1) NOT NULL DEFAULT b'0' ;

ALTER TABLE `coachcoach`.`routine_categories`
    CHANGE COLUMN `is_completed` `is_completed` BIT(1) NOT NULL DEFAULT b'0' ;
