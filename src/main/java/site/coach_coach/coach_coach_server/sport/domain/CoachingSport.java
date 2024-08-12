package site.coach_coach.coach_coach_server.sport.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.coach_coach.coach_coach_server.coach.domain.Coach;

@Entity
@Table(name = "coaching_sports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachingSport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coaching_sport_id")
	private Long coachingSportId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "coach_id")
	private Coach coach;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "sport_id")
	private Sport sport;

	@NotNull
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@NotNull
	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();
}
