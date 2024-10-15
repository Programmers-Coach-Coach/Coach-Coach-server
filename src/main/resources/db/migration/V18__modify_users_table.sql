ALTER TABLE `coachcoach`.`users`
    ADD COLUMN `role`      ENUM ('ROLE_USER', 'ROLE_GUEST') NOT NULL DEFAULT 'ROLE_USER' AFTER `is_coach`,
    ADD COLUMN `is_social` BIT(1)                           NOT NULL DEFAULT b'0' AFTER `role`,
    ADD COLUMN `username`  VARCHAR(500)                     NULL AFTER `is_social`,
    CHANGE COLUMN `password` `password` VARCHAR(200) NULL;
