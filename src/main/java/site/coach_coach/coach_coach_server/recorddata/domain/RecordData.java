package site.coach_coach.coach_coach_server.recorddata.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.user.domain.User;

@Table(name = "record_data")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordData extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "record_data_id")
	private Long recordDataId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "weight")
	private Integer weight;

	@Column(name = "skeletal_muscle")
	private Integer skeletalMuscle;

	@Column(name = "fat_percentage")
	private Integer fatPercentage;

	@Column(name = "bmi")
	private Double bmi;

	@Column(name = "record_date")
	private Date recordDate;

	public void updateRecordData(Integer weight, Integer skeletalMuscle, Integer fatPercentage, Double bmi) {
		this.weight = weight;
		this.skeletalMuscle = skeletalMuscle;
		this.fatPercentage = fatPercentage;
		this.bmi = bmi;
	}
}
