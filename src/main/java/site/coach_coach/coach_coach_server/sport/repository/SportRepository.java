package site.coach_coach.coach_coach_server.sport.repository;

import org.springframework.data.jpa.repository.*;
import site.coach_coach.coach_coach_server.sport.domain.*;

public interface SportRepository extends JpaRepository<Sport, Long> {
}
