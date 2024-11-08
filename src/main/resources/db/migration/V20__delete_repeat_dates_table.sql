-- repeat_dates 테이블 삭제 --
drop table `coachcoach`.`repeat_dates`;

-- routines 테이블에 repeat_date 컬럼 추가 --
alter table `coachcoach`.`routines`
ADD COLUMN `repeat_date` SET('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL AFTER `is_deleted`;
