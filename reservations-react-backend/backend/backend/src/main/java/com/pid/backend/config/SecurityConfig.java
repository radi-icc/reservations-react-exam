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
                        .requestMatchers("/api/affiliate/shows", "/api/affiliate/plans").permitAll()
                        .requestMatchers("/api/rss/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reviews/shows/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/shows/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/locations/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/localities/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/representations/**").permitAll()

                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/affiliate/me/**").hasRole("AFFILIATE")
                        .requestMatchers("/api/producer/**").hasRole("PRODUCER")
                        .requestMatchers("/api/critic/**").hasRole("CRITIC")
                        .requestMatchers("/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/reservations").hasRole("ADMIN")
                        .requestMatchers("/api/reservations/me", "/api/reservations/*", "/api/reservations/*/cancel").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/reservations").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/reviews").hasRole("ADMIN")
                        .requestMatchers("/api/reviews/me", "/api/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/reviews").authenticated()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")
                        .requestMatchers("/api/artists/**").hasRole("ADMIN")
                        .requestMatchers("/api/artist-types/**").hasRole("ADMIN")
                        .requestMatchers("/api/artist-type-assignments/**").hasRole("ADMIN")
                        .requestMatchers("/api/collaborations/**").hasRole("ADMIN")
                        .requestMatchers("/api/prices/**").hasRole("ADMIN")
                        .requestMatchers("/api/affiliate-plans/**").hasRole("ADMIN")
                        .requestMatchers("/api/api-keys/**").hasRole("ADMIN")
                        .requestMatchers("/api/statistics/shows/*/sales").hasAnyRole("ADMIN", "PRODUCER")
                        .requestMatchers("/api/statistics/**").hasRole("ADMIN")
                        .requestMatchers("/api/shows/**", "/api/locations/**", "/api/localities/**", "/api/representations/**").hasRole("ADMIN")
                        .anyRequest().denyAll()
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
