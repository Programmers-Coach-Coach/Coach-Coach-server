package site.coach_coach.coach_coach_server.common.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.routine.repository.RoutineRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {
	private final RoutineRepository routineRepository;

	@Transactional
	@Scheduled(cron = "0 0 15 * * *", zone = "UTC")
	public void resetIsCompleted() {
		log.info("Start to reset All 'IsCompleted'.");
		routineRepository.resetIsCompleted();
		log.info("All 'IsCompleted' has been reset.");
	}
}
