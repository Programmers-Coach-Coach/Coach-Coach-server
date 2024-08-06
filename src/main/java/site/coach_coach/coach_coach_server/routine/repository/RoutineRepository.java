package site.coach_coach.coach_coach_server.routine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
	@Query(value =
		"SELECT r.routine_id AS routineId, r.routine_name AS routineName, s.sport_name AS sportName FROM routines r "
			+ "LEFT JOIN sports s ON r.sport_id = s.sport_id"
			+ " WHERE r.user_id = :userId AND r.coach_id IS NULL", nativeQuery = true)
	List<RoutineForListDto> findRoutineListByMyself(@Param("userId") Long userId);

	@Query(value =
		"SELECT r.routine_id AS routineId, r.routine_name AS routineName, s.sport_name AS sportName FROM routines r "
			+ "LEFT JOIN sports s ON r.sport_id = s.sport_id"
			+ " WHERE r.user_id = :userId AND r.coach_id = :coachId", nativeQuery = true)
	List<RoutineForListDto> findRoutineListByCoach(@Param("userId") Long userId, @Param("coachId") Long coachId);

}
