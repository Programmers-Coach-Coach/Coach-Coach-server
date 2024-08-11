package site.coach_coach.coach_coach_server.coach.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.user.domain.User;

@Table(name = "coaches")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Coach extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotBlank
	@Column(name = "coach_id")
	private Long coachId;

	@Lob
	@Column(name = "coach_introduction", nullable = false)
	private String coachIntroduction;

	@Size(max = 100)
	@Column(name = "active_center")
	private String activeCenter;

	@Size(max = 100)
	@Column(name = "active_center_detail")
	private String activeCenterDetail;

	@NotBlank
	@Size(max = 100)
	@Column(name = "active_hours", nullable = false, length = 100)
	private String activeHours;

	@Size(max = 400)
	@Column(name = "chatting_url", nullable = false, length = 400)
	private String chattingUrl;

	@Column(name = "is_open")
	private Boolean isOpen;

	@OneToOne
	@JoinColumn(name = "userId")
	private User user;
}
