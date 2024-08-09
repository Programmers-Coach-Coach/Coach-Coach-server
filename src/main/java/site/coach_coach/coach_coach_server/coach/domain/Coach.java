package site.coach_coach.coach_coach_server.coach.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.user.domain.User;

@Table(name = "coaches")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Coach {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotBlank
	@Column(name = "coach_id")
	private Long coachId;

	@Size(max = 255)
	@Column(name = "coach_introduction")
	private String coachIntroduction;

	@NotBlank
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Size(max = 255)
	@Column(name = "active_center")
	private String activeCenter;

	@NotBlank
	@Size(max = 50)
	@Column(name = "active_hours")
	private String activeHours;

	@Size(max = 255)
	@Column(name = "chatting_url")
	private String chattingUrl;

	@Column(name = "is_open")
	private Boolean isOpen;

	@OneToOne
	@JoinColumn(name = "userId")
	private User user;
}
