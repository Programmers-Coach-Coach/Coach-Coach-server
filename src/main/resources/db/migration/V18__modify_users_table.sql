ALTER TABLE `coachcoach`.`users`
    ADD COLUMN `is_social` BIT(1)       NOT NULL DEFAULT b'0' AFTER `is_coach`,
    ADD COLUMN `username`  VARCHAR(500) NULL AFTER `is_social`,
    CHANGE COLUMN `password` `password` VARCHAR(200) NULL;
