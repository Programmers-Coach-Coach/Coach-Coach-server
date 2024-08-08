package site.coach_coach.coach_coach_server.sport.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

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
    private Long sportId;

    @Column(nullable = false, length = 45)
    private String sportName;

    @Column(name = "sport_image_url", nullable = false, length = 400)
    private String sportImageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

	public Sport(long l, String soccer, String soccerImageUrl) {
	}
}
