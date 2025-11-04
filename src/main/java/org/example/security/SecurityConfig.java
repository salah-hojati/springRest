package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF, not needed for stateless REST APIs
                .csrf(csrf -> csrf.disable())

                // 2. Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Add our custom API key filter before the standard authentication filter
                .addFilterBefore(new ApiKeyAuthFilter(), UsernamePasswordAuthenticationFilter.class)

                // 4. Define authorization rules for endpoints
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to Swagger UI and API docs
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // Allow public access to our simple book endpoints for demonstration
                        .requestMatchers("/books/**", "/api/parameters/**").permitAll()
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
