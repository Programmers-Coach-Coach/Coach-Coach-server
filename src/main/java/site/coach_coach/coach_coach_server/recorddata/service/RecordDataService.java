package site.coach_coach.coach_coach_server.recorddata.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.InvalidInputException;
import site.coach_coach.coach_coach_server.recorddata.domain.RecordData;
import site.coach_coach.coach_coach_server.recorddata.dto.RecordDataRequest;
import site.coach_coach.coach_coach_server.recorddata.repository.RecordDataRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordDataService {
	private final RecordDataRepository recordDataRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createRecordDate(String date, Long userId, RecordDataRequest recordDataRequest) {
		if (!isValidDate(date)) {
			throw new InvalidInputException(ErrorMessage.INVALID_REQUEST);
		}

		Date recordDate = convertToDate(date);

		User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);

		Optional<RecordData> existingRecord = recordDataRepository.findByRecordDateAndUser_UserId(recordDate, userId);

		if (existingRecord.isPresent()) {
			RecordData recordData = existingRecord.get();
			recordData.updateRecordData(
				recordDataRequest.weight(),
				recordDataRequest.skeletalMuscle(),
				recordDataRequest.fatPercentage(),
				recordDataRequest.bmi()
			);
		} else {
			RecordData newRecordData = RecordData.builder()
				.user(user)
				.recordDate(recordDate)
				.weight(recordDataRequest.weight())
				.skeletalMuscle(recordDataRequest.skeletalMuscle())
				.fatPercentage(recordDataRequest.fatPercentage())
				.bmi(recordDataRequest.bmi())
				.build();

			recordDataRepository.save(newRecordData);
		}
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
