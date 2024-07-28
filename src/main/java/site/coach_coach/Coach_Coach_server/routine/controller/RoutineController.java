package site.coach_coach.Coach_Coach_server.routine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import site.coach_coach.Coach_Coach_server.routine.dto.RoutineDTO;
import site.coach_coach.Coach_Coach_server.routine.domain.Routine;
import site.coach_coach.Coach_Coach_server.routine.services.RoutineServices;

@Controller
public class RoutineController {
    @Autowired
    private RoutineServices routineServices;

    @ResponseBody
    @PostMapping("/routines")
    public ResponseEntity addRoutine(@RequestBody RoutineDTO routineDto){
        // userId의 경우 임시로 body로 받게 함
		System.out.println("입력 받은 body값 1" + routineDto);
        Routine checkAddRoutine = routineServices.addRoutineService(routineDto);
		System.out.println("DB에 추가 후 , routine_id를 반환하는가? : "+checkAddRoutine.getId());
        if(checkAddRoutine.getId() != null){
            return ResponseEntity.status(HttpStatus.CREATED).body("Add Routine Success");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Add Routine Fail");
        }

    }
}
