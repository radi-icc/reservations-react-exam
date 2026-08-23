package com.pid.backend.config;

import com.pid.backend.security.CustomUserDetailsService;
import com.pid.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        /*
                         * PUBLIC ENDPOINTS
                         * No JWT required.
                         */
                        .requestMatchers("/api/auth/signup", "/api/auth/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/admin/external-shows/**").permitAll()
                        .requestMatchers("/api/admin/csv/**").permitAll()
                        .requestMatchers("/api/affiliate/**").permitAll()
                        .requestMatchers("/api/rss/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reviews/shows/*").permitAll()

                        /*
                         * PUBLIC CRUD / TESTING ENDPOINTS
                         * No JWT required for now.
                         */
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/roles/**").permitAll()
                        .requestMatchers("/api/localities/**").permitAll()
                        .requestMatchers("/api/locations/**").permitAll()
                        .requestMatchers("/api/artists/**").permitAll()
                        .requestMatchers("/api/artist-types/**").permitAll()
                        .requestMatchers("/api/artist-type-assignments/**").permitAll()
                        .requestMatchers("/api/collaborations/**").permitAll()
                        .requestMatchers("/api/prices/**").permitAll()
                        .requestMatchers("/api/shows/**").permitAll()
                        .requestMatchers("/api/representations/**").permitAll()
                        .requestMatchers("/api/affiliate-plans/**").permitAll()
                        .requestMatchers("/api/api-keys/**").permitAll()
                        .requestMatchers("/api/statistics/**").permitAll()

                        /*
                         * JWT REQUIRED ENDPOINTS
                         * These endpoints use CurrentUserService, so JWT must be present.
                         */
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/reservations/**").authenticated()
                        .requestMatchers("/api/reviews/**").authenticated()

                        /*
                         * DEFAULT
                         * Public for now during development.
                         */
                        .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider())

                /*
                 * JWT filter is enabled.
                 * It will read Authorization: Bearer TOKEN and set SecurityContext.
                 */
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(passwordEncoder());

        authenticationProvider.setUserDetailsService(customUserDetailsService);

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:3000"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "X-API-KEY"
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
