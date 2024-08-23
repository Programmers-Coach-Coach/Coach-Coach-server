package site.coach_coach.coach_coach_server.userrecord.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.InvalidInputException;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordCreateRequest;
import site.coach_coach.coach_coach_server.userrecord.repository.UserRecordRepository;

@Service
@RequiredArgsConstructor
public class UserRecordService {
	private final UserRecordRepository userRecordRepository;
	private final UserRepository userRepository;

	@Transactional
	public Long addBodyInfoToUserRecord(Long userId, UserRecordCreateRequest userRecordCreateRequest) {
		String date = userRecordCreateRequest.recordDate();

		if (!isValidDate(date)) {
			throw new InvalidInputException(ErrorMessage.INVALID_REQUEST);
		}

		Date recordDate = convertToDate(date);
		User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);
		UserRecord userRecord = null;
		if (!userRecordRepository.existsByRecordDateAndUser_UserId(recordDate, userId)) {
			userRecord = UserRecord.builder()
				.user(user)
				.weight(userRecordCreateRequest.weight())
				.skeletalMuscle(userRecordCreateRequest.skeletalMuscle())
				.fatPercentage(userRecordCreateRequest.fatPercentage())
				.bmi(userRecordCreateRequest.bmi())
				.recordDate(recordDate)
				.build();
			userRecordRepository.save(userRecord);
		}
		return Objects.requireNonNull(userRecord).getUserRecordId();
	}

	private boolean isValidDate(String date) {
		try {
			LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	private Date convertToDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			throw new InvalidInputException(ErrorMessage.INVALID_REQUEST);
		}
	}
}
