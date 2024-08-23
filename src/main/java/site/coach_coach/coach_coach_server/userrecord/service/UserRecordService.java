package site.coach_coach.coach_coach_server.userrecord.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.common.exception.InvalidInputException;
import site.coach_coach.coach_coach_server.notification.exception.NotFoundException;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordCreateRequest;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordUpdateRequest;
import site.coach_coach.coach_coach_server.userrecord.exception.DuplicateRecordException;
import site.coach_coach.coach_coach_server.userrecord.repository.UserRecordRepository;

@Service
@RequiredArgsConstructor
public class UserRecordService {
	private final UserRecordRepository userRecordRepository;
	private final UserRepository userRepository;

	@Transactional
	public Long addBodyInfoToUserRecord(Long userId, UserRecordCreateRequest userRecordCreateRequest) {
		String date = userRecordCreateRequest.recordDate();

		LocalDate recordDate = validateAndConvertToLocalDate(date);
		User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);

		if (userRecordRepository.existsByRecordDateAndUser_UserId(recordDate, userId)) {
			throw new DuplicateRecordException(ErrorMessage.DUPLICATE_RECORD);
		}

		UserRecord userRecord = UserRecord.builder()
			.user(user)
			.weight(userRecordCreateRequest.weight())
			.skeletalMuscle(userRecordCreateRequest.skeletalMuscle())
			.fatPercentage(userRecordCreateRequest.fatPercentage())
			.bmi(userRecordCreateRequest.bmi())
			.recordDate(recordDate)
			.build();
		userRecordRepository.save(userRecord);

		return userRecord.getUserRecordId();
	}

	@Transactional
	public void updateBodyInfoToUserRecord(Long recordId, Long userId,
		UserRecordUpdateRequest userRecordUpdateRequest) {
		UserRecord userRecord = userRecordRepository.findById(recordId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_RECORD));

		if (!userRecord.getUser().getUserId().equals(userId)) {
			throw new AccessDeniedException();
		}
		
		userRecord.updateBodyInfo(
			userRecordUpdateRequest.weight(),
			userRecordUpdateRequest.skeletalMuscle(),
			userRecordUpdateRequest.fatPercentage(),
			userRecordUpdateRequest.bmi()
		);
	}

	private LocalDate validateAndConvertToLocalDate(String date) {
		try {
			return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (DateTimeParseException e) {
			throw new InvalidInputException(ErrorMessage.INVALID_VALUE);
		}
	}
}
