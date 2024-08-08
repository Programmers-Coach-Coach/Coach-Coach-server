package site.coach_coach.coach_coach_server.coach.domain;


import jakarta.persistence.*;
import lombok.*;
import site.coach_coach.coach_coach_server.sport.domain.*;
import site.coach_coach.coach_coach_server.user.domain.*;

import java.time.*;
import java.util.*;

@Entity
@Table(name = "coaches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coach {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coach_id")
	private Long coachId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Lob
	@Column(name = "coach_introduction", nullable = false)
	private String coachIntroduction;

	@Column(name = "active_center", nullable = false)
	private String activeCenter;

	@Column(name = "active_hours_on", nullable = false)
	private int activeHoursOn;

	@Column(name = "active_hours_off", nullable = false)
	private int activeHoursOff;

	@Column(name = "chatting_url", nullable = false)
	private String chattingUrl;

	@Column(name = "is_open", nullable = false)
	private boolean isOpen;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt = LocalDateTime.now();

	@OneToMany(mappedBy = "coach")
	private List<CoachingSport> coachingSports;

	private int likes;
}
