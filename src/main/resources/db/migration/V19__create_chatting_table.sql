CREATE TABLE `coachcoach`.`chat_rooms`
(
    `chat_room_id` BIGINT    NOT NULL AUTO_INCREMENT,
    `coach_id`     BIGINT    NOT NULL,
    `user_id`      BIGINT    NOT NULL,
    `matching_id`  BIGINT    NOT NULL,
    `created_at`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    `updated_at`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
    PRIMARY KEY (`chat_room_id`)
);

ALTER TABLE `coachcoach`.`chat_rooms`
    ADD INDEX `fk_chat_rooms_coach_id_idx` (`coach_id` ASC) VISIBLE,
    ADD INDEX `fk_chat_rooms_user_id_idx` (`user_id` ASC) VISIBLE;
ALTER TABLE `coachcoach`.`chat_rooms`
    ADD CONSTRAINT `fk_chat_rooms_coach_id`
        FOREIGN KEY (`coach_id`)
            REFERENCES `coachcoach`.`coaches` (`coach_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    ADD CONSTRAINT `fk_chat_rooms_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `coachcoach`.`users` (`user_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;
