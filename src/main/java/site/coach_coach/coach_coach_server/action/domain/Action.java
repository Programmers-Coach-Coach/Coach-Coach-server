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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;

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

	@NotNull
	@Size(max = 45)
	@Column(name = "action_name")
	private String actionName;

	@Size(max = 45)
	@Column(name = "sets")
	private String sets;

	@Size(max = 45)
	@Column(name = "count_or_minutes")
	private String countOrMinutes;

	@Size(max = 200)
	@Column(name = "description")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_category_id")
	private Category category;
}
