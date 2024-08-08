package site.coach_coach.coach_coach_server.sport.domain;


import jakarta.persistence.*;
import lombok.*;
import site.coach_coach.coach_coach_server.user.domain.*;

import java.time.*;

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
	private Long interestedSportId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "sport_id", nullable = false)
	private Sport sport;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();

}
