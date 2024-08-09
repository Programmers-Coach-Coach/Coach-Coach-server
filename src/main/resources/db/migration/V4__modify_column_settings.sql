ALTER TABLE `coachcoach`.`users`
    ADD COLUMN `local_address_detail` VARCHAR(100) NULL DEFAULT NULL AFTER `local_address`,
    CHANGE COLUMN `profile_image_url` `profile_image_url` VARCHAR(500) NULL DEFAULT NULL ,
    CHANGE COLUMN `local_info` `local_address` VARCHAR(100) NULL DEFAULT NULL ;
ALTER TABLE `coachcoach`.`sports`
    CHANGE COLUMN `sport_image_url` `sport_image_url` VARCHAR(500) NOT NULL ;

ALTER TABLE `coachcoach`.`sports`
    CHANGE COLUMN `sport_image_url` `sport_image_url` VARCHAR(500) NOT NULL ;

ALTER TABLE `coachcoach`.`notifications`
    DROP COLUMN `is_reading`;

ALTER TABLE `coachcoach`.`coaches`
    DROP COLUMN `active_hours_off`,
    ADD COLUMN `active_center_detail` VARCHAR(100) NULL AFTER `active_center`,
    CHANGE COLUMN `active_center` `active_center` VARCHAR(100) NULL ,
    CHANGE COLUMN `active_hours_on` `active_hours` VARCHAR(100) NOT NULL ;
