package site.coach_coach.Coach_Coach_server.routine.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

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

    @Column(name="sport_id")
    private Integer sportId;

    @Column(name="routine_Name")
    private String routineName;

    @Builder
    public Routine(Integer userId, Integer coachId, Integer sportId, String routineName){
        this.userId = userId;
        this.coachId = coachId;
        this.sportId = sportId;
        this.routineName = routineName;
    }
}
