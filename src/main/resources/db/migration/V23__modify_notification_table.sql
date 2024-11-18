ALTER TABLE `coachcoach`.`notifications`
    ADD COLUMN `is_read` BIT(1) NOT NULL DEFAULT b'0' AFTER `relation_function`,
CHANGE COLUMN `message` `message` VARCHAR(200) NOT NULL ,
CHANGE COLUMN `relation_function` `relation_function` ENUM('ask', 'like', 'review', 'match', 'refusal', 'cancel', 'routine', 'request') NOT NULL ;

ALTER TABLE `coachcoach`.`notifications`
    ADD COLUMN `coach_id` BIGINT NOT NULL AFTER `user_id`;
ALTER TABLE `coachcoach`.`notifications`
    ADD INDEX `fk_notifications_coach_id_idx` (`coach_id` ASC) VISIBLE;
ALTER TABLE `coachcoach`.`notifications`
    ADD CONSTRAINT `fk_notifications_coach_id`
        FOREIGN KEY (`coach_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION;
