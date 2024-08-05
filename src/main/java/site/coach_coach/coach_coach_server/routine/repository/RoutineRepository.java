package site.coach_coach.coach_coach_server.routine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
	@Query(value = "SELECT m.is_matching FROM user_coach_matching m "
		+ "WHERE m.user_id = :userId AND m.coach_id = :coachId", nativeQuery = true)
	Optional<Integer> findMatchingValue(@Param("userId") Long userId, @Param("coachId") Optional<Long> coachId);

	@Query(value =
		"SELECT r.routine_id AS routineId, r.routine_name AS routineName, s.sport_name AS sportName FROM routines r "
			+ "LEFT JOIN sports s "
			+ "ON r.sport_id = s.sport_id WHERE r.user_id = :userId AND r.coach_id IS NULL", nativeQuery = true)
	List<RoutineForListDto> findMyRoutines(@Param("userId") Long userId);

	@Query(value =
		"SELECT r.routine_id AS routineId, r.routine_name AS routineName, s.sport_name AS sportName FROM routines r "
			+ "LEFT JOIN sports s "
			+ "ON r.sport_id = s.sport_id WHERE r.user_id = :userId AND r.coach_id = :coachId", nativeQuery = true)
	List<RoutineForListDto> findRoutinesByCoach(@Param("userId") Long userId, @Param("coachId") Optional<Long> coachId);

	@Query(value =
		"SELECT c.coach_id AS coachId, u.nickname AS coachName, u.profile_image_url AS profileImageUrl FROM coaches c "
			+ "LEFT JOIN users u "
			+ "ON c.user_id = u.user_id WHERE c.coach_id = :coachId", nativeQuery = true)
	Optional<RoutineListCoachInfoDto> findRoutineListCoachInfo(@Param("coachId") Optional<Long> coachId);
}
