package site.coach_coach.coach_coach_server.routine.exception;

import java.util.NoSuchElementException;

public class NoExistRoutineException extends NoSuchElementException {
	public NoExistRoutineException(String message) {
		super(message);
	}
}
