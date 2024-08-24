package site.coach_coach.coach_coach_server.userrecord.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
	boolean existsByRecordDateAndUser_UserId(LocalDate recordDate, Long userId);

	@Query("SELECT ur FROM UserRecord ur "
		+ "LEFT JOIN FETCH ur.completedCategories cc "
		+ "WHERE ur.user.userId = :userId AND ur.recordDate BETWEEN :startDate AND :endDate")
	List<UserRecord> findByUser_UserIdAndRecordDateBetweenWithCompletedCategories(
		@Param("userId") Long userId,
		@Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate);

	Optional<UserRecord> findByRecordDateAndUser_UserId(LocalDate recordDate, Long userId);
}
