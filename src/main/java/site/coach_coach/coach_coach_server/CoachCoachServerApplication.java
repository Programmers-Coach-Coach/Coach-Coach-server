package site.coach_coach.coach_coach_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CoachCoachServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoachCoachServerApplication.class, args);
	}

}
