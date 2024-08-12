package site.coach_coach.coach_coach_server.sport.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@NotNull
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@NotNull
	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();
}
