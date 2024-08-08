package site.coach_coach.coach_coach_server.sport.domain;

import jakarta.persistence.*;
import lombok.*;
import site.coach_coach.coach_coach_server.coach.domain.*;

import java.time.*;

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
	private Long coachingSportId;

	@ManyToOne
	@JoinColumn(name = "coach_id", nullable = false)
	private Coach coach;

	@ManyToOne
	@JoinColumn(name = "sport_id", nullable = false)
	private Sport sport;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();

}
