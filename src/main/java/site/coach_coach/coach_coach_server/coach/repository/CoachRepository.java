package site.coach_coach.coach_coach_server.coach.repository;

import org.springframework.data.jpa.repository.*;
import site.coach_coach.coach_coach_server.coach.domain.*;

import java.util.*;

public interface CoachRepository extends JpaRepository<Coach, Long> {
	List<Coach> findTop3ByOrderByLikesDesc();
}
