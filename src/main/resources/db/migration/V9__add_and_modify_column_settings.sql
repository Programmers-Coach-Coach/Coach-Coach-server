ALTER TABLE `coachcoach`.`record_data`
    CHANGE COLUMN `weight` `weight` DOUBLE NULL DEFAULT NULL,
    CHANGE COLUMN `skeletal_muscle` `skeletal_muscle` DOUBLE NULL DEFAULT NULL,
    CHANGE COLUMN `fat_percentage` `fat_percentage` DOUBLE NULL DEFAULT NULL,
    CHANGE COLUMN `record_date` `record_date` DATE NOT NULL;

ALTER TABLE `coachcoach`.`completed_categories`
    CHANGE COLUMN `record_date` `record_date` DATE NOT NULL;

DROP TABLE IF EXISTS `coachcoach`.`user_records`;
