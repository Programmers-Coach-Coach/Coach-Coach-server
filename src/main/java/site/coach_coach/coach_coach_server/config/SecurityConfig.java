package site.coach_coach.coach_coach_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.JwtExceptionFilter;
import site.coach_coach.coach_coach_server.auth.jwt.TokenFilter;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.oauth.CustomOAuth2Handler;
import site.coach_coach.coach_coach_server.auth.oauth.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final TokenProvider tokenProvider;
	private final JwtExceptionFilter jwtExceptionFilter;
	private final ExceptionHandlerConfig exceptionHandlerConfig;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOAuth2Handler customOAuth2Handler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.addFilterBefore(new TokenFilter(tokenProvider),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtExceptionFilter, TokenFilter.class)
			.oauth2Login((oauth2) ->
				oauth2.userInfoEndpoint(
						userInfoEndpointConfig ->
							userInfoEndpointConfig.userService(customOAuth2UserService))
					.successHandler(customOAuth2Handler)
			)
			.authorizeHttpRequests((authorizeRequests) ->
				authorizeRequests
					.requestMatchers("/api/v1/auth/login", "/api/v1/auth/signup", "/api/v1/test",
						"/api/v1/auth/check-email", "/api/v1/auth/check-nickname", "/api/v1/auth/reissue",
						"/api/v1/auth", "/oauth2/", "/login/oauth2/", "/ws/**", "/users/reset-password").permitAll()
					.anyRequest()
					.authenticated()
			)
			.exceptionHandling(exceptionHandlerConfig)
			.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.cors((cors) -> cors.configurationSource(corsConfigurationSource()));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOrigin("https://coach-coach.site");
		configuration.addAllowedOrigin("http://localhost:5173");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(1800L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
