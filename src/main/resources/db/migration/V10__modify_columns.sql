ALTER TABLE `coachcoach`.`completed_categories`
    DROP FOREIGN KEY `fk_completed_categories_user_id`;

ALTER TABLE `coachcoach`.`completed_categories`
    DROP COLUMN `user_id`;

ALTER TABLE `coachcoach`.`completed_categories`
    DROP FOREIGN KEY `fk_completed_categories_routine_category_id`;

ALTER TABLE `coachcoach`.`completed_categories`
    ADD CONSTRAINT `fk_completed_categories_routine_category_id`
        FOREIGN KEY (`routine_category_id`)
        REFERENCES `coachcoach`.`routine_categories` (`routine_category_id`)
        ON DELETE CASCADE;

