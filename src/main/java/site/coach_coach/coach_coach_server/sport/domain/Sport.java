package site.coach_coach.coach_coach_server.sport.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "sports")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Sport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotBlank
	@Column(name = "sport_id")
	private Long sportId;

	@NotBlank
	@Size(max = 45)
	@Column(name = "sport_name")
	private String sportName;

	@NotBlank
	@Size(max = 400)
	@Column(name = "sport_image_url")
	private String sportImageUrl;

	@NotBlank
	@Column(name = "created_at")
	private LocalDateTime createdAt;
}
