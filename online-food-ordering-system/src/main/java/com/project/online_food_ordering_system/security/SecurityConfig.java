package com.project.online_food_ordering_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity 
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/auth/**").permitAll()
	            .requestMatchers(HttpMethod.GET, "/menu/**").permitAll() 

	            .requestMatchers(HttpMethod.POST, "/menu/**").hasRole("ADMIN")
	            .requestMatchers(HttpMethod.PUT, "/menu/**").hasRole("ADMIN")
	            .requestMatchers(HttpMethod.DELETE, "/menu/**").hasRole("ADMIN")

	            .requestMatchers("/cart/**", "/api/payments/**", "/orders/**").permitAll() 

	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form.permitAll());

	    return http.build();
	}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
