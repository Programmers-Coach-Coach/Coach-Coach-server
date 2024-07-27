package site.coach_coach.Coach_Coach_server.routine.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.Coach_Coach_server.routine.domain.Routine;

@Getter
@NoArgsConstructor
public class RoutineDTO {

    private Integer userId;
    private Integer coachId;
    private Integer sportsId;
    private String routineName;

    @Builder
    public RoutineDTO(Integer userId, Integer coachId, Integer sportsId, String routineName){
        this.userId = userId;
        this.coachId = coachId;
        this.sportsId = sportsId;
        this.routineName = routineName;
    }

    public Routine toEntity(){
        return Routine.builder()
                .userId(userId)
                .coachId(coachId)
                .sportsId(sportsId)
                .routineName(routineName)
                .build();
    }

    public RoutineDTO toDTO(Routine routine){
        return RoutineDTO.builder()
                .userId(userId)
                .coachId(coachId)
                .sportsId(sportsId)
                .routineName(routineName)
                .build();
    }

}
