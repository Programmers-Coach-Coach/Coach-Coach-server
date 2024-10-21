package site.coach_coach.coach_coach_server.completedroutine.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;

@Table(name = "completed_routines")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedRoutine extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "completed_routine_id")
	private Long completedRoutineId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_record_id")
	private UserRecord userRecord;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_id")
	private Routine routine;

	@Column(name = "record_date")
	private LocalDate recordDate;
}
