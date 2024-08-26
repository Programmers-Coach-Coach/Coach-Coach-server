ALTER TABLE `coachcoach`.`users`
ADD COLUMN `is_coach` TINYINT NOT NULL DEFAULT 0 AFTER `introduction`;

ALTER TABLE `coachcoach`.`actions`
DROP COLUMN `count_or_minutes`,
ADD COLUMN `counts` INT NULL AFTER `sets`,
ADD COLUMN `minutes` INT NULL AFTER `counts`;
