package site.coach_coach.coach_coach_server.matching.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_coach_matching")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Matching {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotBlank
	@Column(name = "user_coach_matching_id")
	private Long userCoachMatchingId;

	@NotBlank
	@Column(name = "user_id")
	private Long userId;

	@NotBlank
	@Column(name = "coach_id")
	private Long coachId;

	@Column(name = "is_matching")
	private Boolean isMatching;

	@NotBlank
	@Column(name = "created_at")
	private LocalDateTime createdAt;
}
