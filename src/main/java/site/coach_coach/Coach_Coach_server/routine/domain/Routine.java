package site.coach_coach.Coach_Coach_server.routine.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="coach_id")
    private Integer coachId;

    @Column(name="sports_id")
    private Integer sportsId;

    @Column(name="routine_Name")
    private String routineName;

    @Builder
    public Routine(Integer userId, Integer coachId, Integer sportsId, String routineName){
        this.userId = userId;
        this.coachId = coachId;
        this.sportsId = sportsId;
        this.routineName = routineName;
    }
}
