ALTER TABLE `coachcoach`.`completed_categories`
    DROP COLUMN `is_completed`;
ALTER TABLE `coachcoach`.`routine_categories`
    ADD COLUMN `is_completed` TINYINT NOT NULL DEFAULT 0 AFTER `category_name`;
ALTER TABLE `coachcoach`.`actions`
    CHANGE COLUMN `set` `sets` INT NULL DEFAULT NULL;
