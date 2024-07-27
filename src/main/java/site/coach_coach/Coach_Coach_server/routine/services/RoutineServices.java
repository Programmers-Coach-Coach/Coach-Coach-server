package site.coach_coach.Coach_Coach_server.routine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.coach_coach.Coach_Coach_server.routine.dto.RoutineDTO;
import site.coach_coach.Coach_Coach_server.routine.domain.Routine;
import site.coach_coach.Coach_Coach_server.routine.repository.RoutineRepository;

@Service
public class RoutineServices {
    @Autowired
    private RoutineRepository routineRepository;

    public Routine addRoutine(RoutineDTO routineDto){
        Routine routine = routineDto.toEntity();
        return routineRepository.save(routine);
    }
}
