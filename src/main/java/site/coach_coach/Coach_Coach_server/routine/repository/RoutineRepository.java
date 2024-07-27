package site.coach_coach.Coach_Coach_server.routine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.coach_coach.Coach_Coach_server.routine.domain.Routine;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Integer> {

}
