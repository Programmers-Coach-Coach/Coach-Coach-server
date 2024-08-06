package site.coach_coach.coach_coach_server.coach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
	@Query(value =
		"SELECT c.coach_id AS coachId, u.nickname AS nickname, u.profile_image_url AS profileImageUrl FROM coaches c "
			+ "LEFT JOIN users u "
			+ "ON c.user_id = u.user_id WHERE c.coach_id = :coachId", nativeQuery = true)
	RoutineListCoachInfoDto findRoutineListCoachInfo(@Param("coachId") Long coachId);
}

