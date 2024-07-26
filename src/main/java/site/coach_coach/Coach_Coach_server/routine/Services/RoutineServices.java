package site.coach_coach.Coach_Coach_server.routine.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.coach_coach.Coach_Coach_server.routine.DTO.RoutineDTO;
import site.coach_coach.Coach_Coach_server.routine.Domain.Routine;
import site.coach_coach.Coach_Coach_server.routine.Repository.RoutineRepository;

@Service
public class RoutineServices {
    @Autowired
    private RoutineRepository routineRepository;

    public Routine addRoutine(RoutineDTO routineDto){
        Routine routine = routineDto.toEntity();
        return routineRepository.save(routine);
    }
}
