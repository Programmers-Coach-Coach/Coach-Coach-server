package site.coach_coach.coach_coach_server.routine.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;

@Table(name = "routines")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Routine extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "routine_id")
	private Long routineId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "coach_id")
	private Long coachId;

	@Column(name = "sport_id")
	private Long sportId;

	@Column(name = "routine_Name")
	private String routineName;

}


