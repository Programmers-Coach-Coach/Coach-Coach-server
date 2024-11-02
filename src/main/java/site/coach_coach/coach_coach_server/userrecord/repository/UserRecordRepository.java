package site.coach_coach.coach_coach_server.userrecord.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
	boolean existsByRecordDateAndUser_UserId(LocalDate recordDate, Long userId);

	@Query("SELECT ur FROM UserRecord ur "
		+ "LEFT JOIN FETCH ur.completedRoutines cr "
		+ "WHERE ur.user.userId = :userId AND ur.recordDate BETWEEN :startDate AND :endDate")
	List<UserRecord> findByUser_UserIdAndRecordDateBetweenWithCompletedRoutines(
		@Param("userId") Long userId,
		@Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate);

	Optional<UserRecord> findByRecordDateAndUser_UserId(LocalDate recordDate, Long userId);

	@Query("SELECT ur FROM UserRecord ur "
		+ "WHERE ur.user.userId = :userId "
		+ "AND CASE "
		+ "    WHEN :type = 'weight' THEN (ur.weight IS NOT NULL AND ur.weight <> 0) "
		+ "    WHEN :type = 'skeletalMuscle' THEN (ur.skeletalMuscle IS NOT NULL AND ur.skeletalMuscle <> 0) "
		+ "    WHEN :type = 'fatPercentage' THEN (ur.fatPercentage IS NOT NULL AND ur.fatPercentage <> 0) "
		+ "    WHEN :type = 'bmi' THEN (ur.bmi IS NOT NULL AND ur.bmi <> 0) "
		+ "    ELSE false "
		+ "END = true "
		+ "ORDER BY ur.recordDate DESC")
	List<UserRecord> findUserRecordByTypeAndUserId(
		@Param("userId") Long userId,
		@Param("type") String type,
		Pageable pageable
	);
}
