package site.coach_coach.coach_coach_server.completedroutine.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.completedroutine.domain.CompletedRoutine;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

@Repository
public interface CompletedRoutineRepository extends JpaRepository<CompletedRoutine, Long> {
	@Query("SELECT cr FROM CompletedRoutine cr "
		+ "LEFT JOIN FETCH cr.routine r "
		+ "LEFT JOIN FETCH r.coach "
		+ "WHERE cr.userRecord.userRecordId = :recordId")
	List<CompletedRoutine> findAllWithDetailsByUserRecordId(@Param("recordId") Long recordId);

	@Query("SELECT cr FROM CompletedRoutine cr "
		+ "LEFT JOIN FETCH cr.routine r "
		+ "LEFT JOIN FETCH r.coach "
		+ "WHERE cr.userRecord.user.userId = :userId "
		+ "AND cr.userRecord.recordDate =: recordDate")
	List<CompletedRoutine> findAllWithDetailsByUserIdAndRecordDate(
		@Param("userId") Long userId,
		@Param("recordDate") LocalDate recordDate
	);

	Optional<CompletedRoutine> findByUserRecord_RecordDateAndRoutine(LocalDate recordDate,
		Routine routine);

	int deleteByUserRecord_UserRecordIdAndRoutine_RoutineId(Long userRecordId, Long routineId);
}
