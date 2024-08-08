package site.coach_coach.coach_coach_server.sport.repository;

import org.springframework.data.jpa.repository.*;
import site.coach_coach.coach_coach_server.coach.domain.*;
import site.coach_coach.coach_coach_server.sport.domain.*;

import java.util.*;

public interface CoachingSportRepository extends JpaRepository<CoachingSport, Long> {
	List<CoachingSport> findByCoach(Coach coach);
}
