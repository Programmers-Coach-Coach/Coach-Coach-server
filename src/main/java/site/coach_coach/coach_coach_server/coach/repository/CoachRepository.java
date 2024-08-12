package site.coach_coach.coach_coach_server.coach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.coach.domain.Coach;

import java.util.*;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
	List<Coach> findByOrderByLikesDesc();
}
