DROP TABLE IF EXISTS `coachcoach`.`record_data`;

CREATE TABLE IF NOT EXISTS `coachcoach`.`user_records`
(
    `user_record_id`  bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`         bigint(20) NOT NULL,
    `weight`          double              DEFAULT NULL,
    `skeletal_muscle` double              DEFAULT NULL,
    `fat_percentage`  double              DEFAULT NULL,
    `bmi`             double              DEFAULT NULL,
    `record_date`     date       NOT NULL,
    `created_at`      timestamp  NOT NULL DEFAULT current_timestamp(),
    `updated_at`      timestamp  NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`user_record_id`),
    KEY `user_records_user_id_idx` (`user_id`),
    CONSTRAINT `fk_user_records_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

ALTER TABLE `coachcoach`.`user_records`
    DROP FOREIGN KEY `fk_user_records_user_id`;
ALTER TABLE `coachcoach`.`user_records`
    ADD CONSTRAINT `fk_user_records_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE;

ALTER TABLE `coachcoach`.`completed_categories`
    DROP FOREIGN KEY `fk_completed_categories_user_id`;

ALTER TABLE `coachcoach`.`completed_categories`
    DROP COLUMN `user_id`,
    ADD COLUMN `user_record_id` BIGINT(20) NOT NULL AFTER `completed_category_id`,
    CHANGE COLUMN `record_date` `record_date` DATE NULL DEFAULT NULL AFTER `routine_category_id`,
    DROP INDEX `fk_completed_categories_user_id_idx`;

ALTER TABLE `coachcoach`.`completed_categories`
    ADD CONSTRAINT `fk_completed_categories_user_record_id`
        FOREIGN KEY (`user_record_id`)
            REFERENCES `coachcoach`.`user_records` (`user_record_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;
