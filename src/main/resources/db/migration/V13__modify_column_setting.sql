ALTER TABLE `coachcoach`.`users`
    CHANGE COLUMN `is_coach` `is_coach` TINYINT NOT NULL DEFAULT 0;

ALTER TABLE `coachcoach`.`users`
    CHANGE COLUMN `introduction` `introduction` VARCHAR(1000) NULL DEFAULT NULL;

ALTER TABLE `coachcoach`.`reviews`
    CHANGE COLUMN `contents` `contents` VARCHAR(1000) NOT NULL;

ALTER TABLE `coachcoach`.`coaches`
    CHANGE COLUMN `coach_introduction` `coach_introduction` VARCHAR(1000) NOT NULL;

ALTER TABLE notifications
    MODIFY COLUMN relation_function ENUM ('ask', 'like', 'review', 'match');
