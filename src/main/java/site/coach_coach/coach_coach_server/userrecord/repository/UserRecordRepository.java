package site.coach_coach.coach_coach_server.userrecord.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
	boolean existsByRecordDateAndUser_UserId(LocalDate recordDate, Long userId);
}
