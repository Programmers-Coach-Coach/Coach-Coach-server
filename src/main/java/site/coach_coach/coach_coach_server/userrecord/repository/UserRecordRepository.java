package site.coach_coach.coach_coach_server.userrecord.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
	boolean existsByRecordDateAndUser_UserId(LocalDate recordDate, Long userId);

	List<UserRecord> findByUser_UserIdAndRecordDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

	Optional<UserRecord> findByRecordDateAndUser_UserId(LocalDate recordDate, Long userId);
}
