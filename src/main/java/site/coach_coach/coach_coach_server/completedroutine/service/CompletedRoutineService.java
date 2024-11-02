package site.coach_coach.coach_coach_server.completedroutine.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.common.exception.DuplicateValueException;
import site.coach_coach.coach_coach_server.common.exception.NotFoundException;
import site.coach_coach.coach_coach_server.completedroutine.domain.CompletedRoutine;
import site.coach_coach.coach_coach_server.completedroutine.repository.CompletedRoutineRepository;
import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.routine.repository.RoutineRepository;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;
import site.coach_coach.coach_coach_server.userrecord.service.UserRecordService;

@Service
@RequiredArgsConstructor
public class CompletedRoutineService {
	private final CompletedRoutineRepository completedRoutineRepository;
	private final UserRecordService userRecordService;
	private final RoutineRepository routineRepository;

	private Routine validateAccessToRoutine(Long routineId, Long userIdByJwt) {
		Routine routine = routineRepository.findExistRoutine(routineId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_ROUTINE));

		if (!routine.getUser().getUserId().equals(userIdByJwt)) {
			throw new AccessDeniedException();
		}
		return routine;
	}

	@Transactional
	public Long createCompletedRoutine(Long routineId, Long userIdByJwt) {
		Routine routine = validateAccessToRoutine(routineId, userIdByJwt);

		UserRecord userRecord = userRecordService.getUserRecordForCompleteRoutine(userIdByJwt);
		completedRoutineRepository.findByUserRecord_RecordDateAndRoutine(
				userRecord.getRecordDate(), routine)
			.ifPresent(completedRoutine -> {
				throw new DuplicateValueException(ErrorMessage.DUPLICATE_COMPLETED_ROUTINE);
			});

		routine.changeIsCompleted();

		CompletedRoutine completedRoutine = CompletedRoutine.builder()
			.userRecord(userRecord)
			.routine(routine)
			.recordDate(userRecord.getRecordDate())
			.build();
		return completedRoutineRepository.save(completedRoutine).getCompletedRoutineId();
	}

	@Transactional
	public void deleteCompletedRoutine(Long routineId, Long userIdByJwt) {
		Routine routine = validateAccessToRoutine(routineId, userIdByJwt);
		UserRecord userRecord = userRecordService.getUserRecordForCompleteRoutine(userIdByJwt);

		int deletedCount = completedRoutineRepository.deleteByUserRecord_UserRecordIdAndRoutine_RoutineId(
			userRecord.getUserRecordId(),
			routine.getRoutineId());
		if (deletedCount == 0) {
			throw new NotFoundException(ErrorMessage.NOT_FOUND_COMPLETED_ROUTINE);
		} else {
			routine.changeIsCompleted();
		}
	}
}
