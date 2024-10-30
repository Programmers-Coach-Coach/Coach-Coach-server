package site.coach_coach.coach_coach_server.repeatdate.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.common.domain.RepeatDateEnum;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

@Table(name = "repeat_dates")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepeatDate extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "repeat_date_id")
	private Long repeatDateId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_id")
	private Routine routine;

	@NotNull
	@Column(name = "date")
	@Enumerated(EnumType.STRING)
	private RepeatDateEnum repeatDate;
}
