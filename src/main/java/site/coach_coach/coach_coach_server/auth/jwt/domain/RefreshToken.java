package site.coach_coach.coach_coach_server.auth.jwt.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.user.domain.User;

@Table(name = "refresh_tokens")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refresh_token_id")
	private Long refreshTokenId;

	@NotNull
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "expire_date")
	private LocalDateTime expireDate;

	@NotBlank
	@Size(max = 200)
	@Column(name = "refresh_token")
	private String refreshToken;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;
}
