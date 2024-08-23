package site.coach_coach.coach_coach_server.userrecord.domain;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.user.domain.User;

@Table(name = "user_records")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecord extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_record_id")
	private Long userRecordId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "weight")
	private Double weight;

	@Column(name = "skeletal_muscle")
	private Double skeletalMuscle;

	@Column(name = "fat_percentage")
	private Double fatPercentage;

	@Column(name = "bmi")
	private Double bmi;

	@Temporal(TemporalType.DATE)
	@Column(name = "record_date")
	private Date recordDate;

	public void updateRecordData(Double weight, Double skeletalMuscle, Double fatPercentage, Double bmi) {
		this.weight = weight;
		this.skeletalMuscle = skeletalMuscle;
		this.fatPercentage = fatPercentage;
		this.bmi = bmi;
	}
}