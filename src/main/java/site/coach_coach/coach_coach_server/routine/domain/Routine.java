package site.coach_coach.coach_coach_server.routine.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.sport.domain.Sport;

@Table(name = "routines")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Routine extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotBlank
	@Column(name = "routine_id")
	private Long routineId;

	@NotBlank
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "coach_id")
	private Long coachId;

	@NotBlank
	@Size(max = 45)
	@Column(name = "routine_Name")
	private String routineName;

	@NotBlank
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sport_id")
	private Sport sport;

}


