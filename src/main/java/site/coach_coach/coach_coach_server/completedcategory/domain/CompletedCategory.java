package site.coach_coach.coach_coach_server.completedcategory.domain;

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
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;

@Table(name = "completed_categories")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedCategory extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "completed_category_id")
	private Long completedCategoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_record_id")
	private UserRecord userRecord;

	@OneToOne
	@JoinColumn(name = "routine_category_id")
	private Category category;

}
