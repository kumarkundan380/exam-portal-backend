package com.exam.config;

import com.exam.enums.UserRole;
import com.exam.security.JwtAuthEntryPoint;
import com.exam.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.exam.constants.ExamPortalConstant.PUBLIC_URLS;
import static com.exam.constants.ExamPortalConstant.ROLE_BASE_PAT;
import static com.exam.constants.ExamPortalConstant.USER_BASE_PATH;

@Configuration
@EnableWebMvc
public class SecurityConfig {
	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
    		JwtAuthEntryPoint jwtAuthEntryPoint,
    		JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
		http.csrf()
			.disable()
			.authorizeHttpRequests()
			.antMatchers(PUBLIC_URLS).permitAll()
				.antMatchers(HttpMethod.POST, USER_BASE_PATH).permitAll()
				.antMatchers(ROLE_BASE_PAT).hasAuthority(UserRole.ADMIN.getValue())
			.anyRequest()
			.authenticated()
			.and()
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
