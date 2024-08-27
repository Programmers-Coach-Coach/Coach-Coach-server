UPDATE `coachcoach`.`users`
SET `is_coach` = 0
WHERE `is_coach` IS NULL;

ALTER TABLE `coachcoach`.`users`
    MODIFY COLUMN `is_coach` TINYINT NOT NULL DEFAULT 0;

ALTER TABLE `coachcoach`.`users`
    MODIFY COLUMN `introduction` VARCHAR(1000) NULL DEFAULT NULL;

ALTER TABLE `coachcoach`.`reviews`
    MODIFY COLUMN `contents` VARCHAR(1000) NOT NULL;

ALTER TABLE `coachcoach`.`coaches`
    MODIFY COLUMN `coach_introduction` VARCHAR(1000) NOT NULL;

ALTER TABLE `coachcoach`.`notifications`
    MODIFY COLUMN `relation_function` ENUM ('ask', 'like', 'review', 'match') NOT NULL;
