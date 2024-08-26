ALTER TABLE `coachcoach`.`completed_categories`
    CHANGE COLUMN `record_date` `record_date` DATE NOT NULL;

ALTER TABLE `coachcoach`.`user_coach_matching`
    CHANGE COLUMN `is_matching` `is_matching` TINYINT NOT NULL DEFAULT 0;
