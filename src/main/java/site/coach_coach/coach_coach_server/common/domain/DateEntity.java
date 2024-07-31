package site.coach_coach.coach_coach_server.common.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

@Getter
@MappedSuperclass
public class DateEntity {
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void onPrePersist() {
		createdAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onPreUpdate() {
		createdAt = LocalDateTime.now();
	}
}
