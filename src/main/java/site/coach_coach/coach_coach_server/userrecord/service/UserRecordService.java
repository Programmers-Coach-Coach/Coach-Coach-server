package site.coach_coach.coach_coach_server.userrecord.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.common.exception.InvalidInputException;
import site.coach_coach.coach_coach_server.completedcategory.repository.CompletedCategoryRepository;
import site.coach_coach.coach_coach_server.notification.exception.NotFoundException;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;
import site.coach_coach.coach_coach_server.userrecord.dto.RecordResponse;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordCreateRequest;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordResponse;
import site.coach_coach.coach_coach_server.userrecord.dto.UserRecordUpdateRequest;
import site.coach_coach.coach_coach_server.userrecord.exception.DuplicateRecordException;
import site.coach_coach.coach_coach_server.userrecord.repository.UserRecordRepository;

@Service
@RequiredArgsConstructor
public class UserRecordService {
	private final UserRecordRepository userRecordRepository;
	private final UserRepository userRepository;
	private final CompletedCategoryRepository completedCategoryRepository;

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
	public void updateBodyInfoToUserRecord(Long userId, Long recordId,
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

	@Transactional(readOnly = true)
	public UserRecordResponse getUserRecordsByUserAndPeriod(Long userId, Integer year, Integer month) {
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

		List<UserRecord> userRecords =
			userRecordRepository.findByUser_UserIdAndRecordDateBetweenWithCompletedCategories(
				userId, startDate, endDate
			);

		List<RecordResponse> records = userRecords.stream()
			.map(record -> new RecordResponse(
				record.getUserRecordId(),
				record.getRecordDate(),
				!record.getCompletedCategories().isEmpty()
			))
			.collect(Collectors.toList());

		return new UserRecordResponse(records);
	}

	public UserRecord getUserRecordForCompleteCategory(Long userId) {
		LocalDate recordDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate();
		return userRecordRepository.findByRecordDateAndUser_UserId(recordDate, userId)
			.orElseGet(() -> {
				User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);
				UserRecord userRecord = UserRecord.builder()
					.user(user)
					.recordDate(recordDate)
					.build();
				return userRecordRepository.save(userRecord);
			});
	}

	private LocalDate validateAndConvertToLocalDate(String date) {
		try {
			return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (DateTimeParseException e) {
			throw new InvalidInputException(ErrorMessage.INVALID_VALUE);
		}
	}
}
