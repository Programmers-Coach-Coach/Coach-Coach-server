package site.coach_coach.Coach_Coach_server.routine.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import site.coach_coach.Coach_Coach_server.routine.DTO.RoutineDTO;
import site.coach_coach.Coach_Coach_server.routine.Domain.Routine;
import site.coach_coach.Coach_Coach_server.routine.Services.RoutineServices;

import java.util.Optional;

@Controller
public class RoutineController {
    @Autowired
    private RoutineServices routineServices;

    @ResponseBody
    @PostMapping("/routines")
    public ResponseEntity addRoutine(@RequestBody Integer userId, Integer coachId, Integer sportId, String routineName){
        // userId의 경우 임시로 body로 받게 함
        RoutineDTO routineDto = new RoutineDTO(userId, coachId, sportId, routineName);
        Routine checkAddRoutine = routineServices.addRoutine(routineDto);
        if(checkAddRoutine.getId() != null){
            return ResponseEntity.status(HttpStatus.CREATED).body("루틴 추가 성공");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("루틴 추가 실패");
        }

    }
}
