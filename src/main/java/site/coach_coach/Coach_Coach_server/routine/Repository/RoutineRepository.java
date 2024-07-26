package site.coach_coach.Coach_Coach_server.routine.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.coach_coach.Coach_Coach_server.routine.Domain.Routine;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Integer> {

}
