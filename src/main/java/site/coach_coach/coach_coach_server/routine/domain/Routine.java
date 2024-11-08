package site.coach_coach.coach_coach_server.routine.domain;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.action.domain.Action;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.routine.converter.DayOfWeekSetConverter;
import site.coach_coach.coach_coach_server.routine.dto.UpdateRoutineInfoRequest;
import site.coach_coach.coach_coach_server.sport.domain.Sport;
import site.coach_coach.coach_coach_server.user.domain.User;

@Table(name = "routines")
@Entity
@Getter
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE routines SET is_deleted = true WHERE routine_id=?")
@Where(clause = "is_deleted=false")
public class Routine extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "routine_id")
	private Long routineId;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coach_id")
	private Coach coach;

	@NotNull
	@Size(max = 45)
	@Column(name = "routine_Name", nullable = false, length = 45)
	private String routineName;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sport_id")
	private Sport sport;

	@NotNull
	@Column(name = "is_completed")
	private Boolean isCompleted;

	@NotNull
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@OneToMany(mappedBy = "routine")
	private List<Action> actions;

	@NotNull
	@Column(name = "repeat_date")
	@Convert(converter = DayOfWeekSetConverter.class)
	private Set<DayOfWeek> repeats;

	public void updateRoutineInfo(UpdateRoutineInfoRequest updateRoutineInfoRequest, Sport sport) {
		this.routineName = updateRoutineInfoRequest.routineName();
		this.sport = sport;
		this.repeats = updateRoutineInfoRequest.repeats();
	}

	public void changeIsCompleted() {
		this.isCompleted = !this.isCompleted;
	}
}


