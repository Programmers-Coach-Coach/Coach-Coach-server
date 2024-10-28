package site.coach_coach.coach_coach_server.action.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.action.dto.ActionDto;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

@Table(name = "actions")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "action_id")
	private Long actionId;

	@Size(max = 45)
	@Column(name = "action_name")
	private String actionName;

	@Column(name = "sets")
	private Integer sets;

	@Column(name = "counts_or_minutes")
	private Integer countsOrMinutes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_id")
	private Routine routine;

	public static Action of(ActionDto actionDto) {
		return Action.builder()
			.actionName(actionDto.actionName())
			.sets(actionDto.sets())
			.countsOrMinutes(actionDto.countsOrMinutes())
			.build();
	}

	public void updateActionInfo(ActionDto actionDto) {
		this.actionName = actionDto.actionName();
		this.sets = actionDto.sets();
		this.countsOrMinutes = actionDto.countsOrMinutes();
	}

	public void resetActionInfo() {
		this.actionName = null;
		this.sets = null;
		this.countsOrMinutes = null;
	}
}
