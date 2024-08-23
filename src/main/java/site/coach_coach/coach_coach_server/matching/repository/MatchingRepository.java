package site.coach_coach.coach_coach_server.matching.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.matching.domain.Matching;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
	Optional<Matching> findByUser_UserIdAndCoach_CoachId(Long userId, Long coachId);

	boolean existsByUserUserIdAndCoachCoachId(Long userId, Long coachId);

	List<Matching> findByCoach_CoachId(Long coachId);
}
