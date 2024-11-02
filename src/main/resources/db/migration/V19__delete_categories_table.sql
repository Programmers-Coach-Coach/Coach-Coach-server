-- actions 테이블 컬럼 및 참조 변경 ---
ALTER TABLE `coachcoach`.`actions`
DROP FOREIGN KEY `fk_actions_routine_category_id`;
ALTER TABLE `coachcoach`.`actions`
DROP COLUMN `count_or_minutes`,
DROP COLUMN `minutes`,
DROP COLUMN `description`,
CHANGE COLUMN `routine_category_id` `routine_id` BIGINT NOT NULL ,
CHANGE COLUMN `counts` `counts_or_minutes` INT NULL DEFAULT NULL ,
CHANGE COLUMN `action_name` `action_name` VARCHAR(45) NULL ,
ADD INDEX `fk_actions_routine_id_idx` (`routine_id` ASC) VISIBLE,
DROP INDEX `routine_category_id_idx` ;

ALTER TABLE `coachcoach`.`actions`
ADD CONSTRAINT `fk_actions_routine_id`
  FOREIGN KEY (`routine_id`)
  REFERENCES `coachcoach`.`routines` (`routine_id`)
  ON DELETE CASCADE;

-- routines 테이블 컬럼 변경 ---
ALTER TABLE `coachcoach`.`routines`
ADD COLUMN `is_completed` BIT(1) NOT NULL DEFAULT b'0' AFTER `routine_name`,
ADD COLUMN `is_deleted` BIT(1) NOT NULL DEFAULT b'0' AFTER `is_completed`;

-- completed_categories 테이블 참조 변경 ---
ALTER TABLE `coachcoach`.`completed_categories`
RENAME TO  `coachcoach`.`completed_routines`;
ALTER TABLE `coachcoach`.`completed_routines`
DROP FOREIGN KEY `fk_completed_categories_routine_category_id`,
DROP FOREIGN KEY `fk_completed_categories_user_record_id`;
ALTER TABLE `coachcoach`.`completed_routines`
CHANGE COLUMN `completed_category_id` `completed_routine_id` BIGINT NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `routine_category_id` `routine_id` BIGINT NOT NULL ,
ADD INDEX `fk_completed_routines_routine_id_idx` (`routine_id` ASC) VISIBLE,
DROP INDEX `completed_categories_routine_category_id_idx` ;
ALTER TABLE `coachcoach`.`completed_routines` RENAME INDEX `fk_completed_categories_user_record_id` TO `fk_completed_routines_user_record_id`;
ALTER TABLE `coachcoach`.`completed_routines` ALTER INDEX `fk_completed_routines_user_record_id` VISIBLE, RENAME TO  `coachcoach`.`completed_routines` ;
ALTER TABLE `coachcoach`.`completed_routines`
ADD CONSTRAINT `fk_completed_routines_routine_id`
  FOREIGN KEY (`routine_id`)
  REFERENCES `coachcoach`.`routines` (`routine_id`)
  ON DELETE RESTRICT,
ADD CONSTRAINT `fk_completed_routines_user_record_id`
  FOREIGN KEY (`user_record_id`)
  REFERENCES `coachcoach`.`user_records` (`user_record_id`)
  ON DELETE CASCADE;

  -- routine_categories 테이블 삭제 ---
DROP TABLE 'routine_categories';

 -- repeat_dates 테이블 생성 --
CREATE TABLE `coachcoach`.`repeat_dates` (
  `repeat_date_id` BIGINT NOT NULL,
  `routine_id` BIGINT NOT NULL,
  `date` SET('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`repeat_date_id`),
  INDEX `fk_repeat_dates_routine_id_idx` (`routine_id` ASC) VISIBLE,
  CONSTRAINT `fk_repeat_dates_routine_id`
    FOREIGN KEY (`routine_id`)
    REFERENCES `coachcoach`.`routines` (`routine_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
