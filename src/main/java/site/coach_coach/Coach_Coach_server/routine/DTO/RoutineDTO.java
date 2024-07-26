package site.coach_coach.Coach_Coach_server.routine.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.Coach_Coach_server.routine.Domain.Routine;

@Getter
@NoArgsConstructor
public class RoutineDTO {

    private Integer userId;
    private Integer coachId;
    private Integer sportId;
    private String routineName;

    @Builder
    public RoutineDTO(Integer userId, Integer coachId, Integer sportId, String routineName){
        this.userId = userId;
        this.coachId = coachId;
        this.sportId = sportId;
        this.routineName = routineName;
    }

    public Routine toEntity(){
        return Routine.builder()
                .userId(userId)
                .coachId(coachId)
                .sportId(sportId)
                .routineName(routineName)
                .build();
    }

    public RoutineDTO toDTO(Routine routine){
        return RoutineDTO.builder()
                .userId(userId)
                .coachId(coachId)
                .sportId(sportId)
                .routineName(routineName)
                .build();
    }

}
