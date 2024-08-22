ALTER TABLE `coachcoach`.`completed_categories`
    ADD COLUMN `is_completed` TINYINT NOT NULL DEFAULT 1 AFTER `routine_category_id`;
ALTER TABLE `coachcoach`.`users`
    DROP COLUMN `local_info`;
ALTER TABLE `coachcoach`.`coaching_sports`
    CHANGE COLUMN `updated_at` `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE `coachcoach`.`user_coach_likes`
    CHANGE COLUMN `updated_at` `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE `coachcoach`.`user_coach_matching`
    CHANGE COLUMN `updated_at` `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE `coachcoach`.`interested_sports`
    CHANGE COLUMN `updated_at` `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE `coachcoach`.`coaches`
    CHANGE COLUMN `is_open` `is_open` TINYINT NOT NULL DEFAULT 1;
