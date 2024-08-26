package site.coach_coach.coach_coach_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.sentry.Sentry;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class CoachCoachServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoachCoachServerApplication.class, args);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> Sentry.captureMessage(ErrorMessage.SERVER_SHUTDOWN)));
	}
}
