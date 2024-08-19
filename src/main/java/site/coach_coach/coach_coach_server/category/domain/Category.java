package site.coach_coach.coach_coach_server.category.domain;

import java.util.List;

import jakarta.persistence.Column;
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
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

@Table(name = "routine_categories")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "routine_category_id")
	private Long categoryId;

	@NotNull
	@Size(max = 45)
	@Column(name = "category_name")
	private String categoryName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_id")
	private Routine routine;

	@OneToMany(mappedBy = "category")
	private List<Action> actionList;

}
