package site.coach_coach.coach_coach_server.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

	@Bean
	public FlywayMigrationStrategy cleanMigrateStrategy() {
		return flyway -> {
			flyway.repair();
			flyway.migrate();
		};
	}
}
