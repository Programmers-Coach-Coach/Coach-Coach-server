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
import site.coach_coach.coach_coach_server.user.domain.User;

@Entity
@Table(name = "interested_sports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedSport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "interested_sport_id")
	private Long interestedSportId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "sport_id", nullable = false)
	private Sport sport;

	@NotNull
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@NotNull
	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();

}
