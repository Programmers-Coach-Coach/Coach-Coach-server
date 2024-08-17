package site.coach_coach.coach_coach_server.config;

import org.springframework.context.annotation.Configuration;

import io.sentry.spring.jakarta.EnableSentry;

@EnableSentry(dsn = "${sentry.dsn}")
@Configuration
class SentryConfiguration {
}
