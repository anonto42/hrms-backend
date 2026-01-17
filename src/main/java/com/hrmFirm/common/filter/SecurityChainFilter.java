package com.hrmFirm.common.filter;

import com.hrmFirm.common.config.CorsConfig;
import com.hrmFirm.common.enums.UserRole;
import com.hrmFirm.common.config.AuthenticationProviderConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityChainFilter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfig corsConfig;
    private final AuthenticationProviderConfig authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfig.corsConfiguration()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/v1/auth/sign-in",
                                "/api/v1/auth/refresh",
                                "/api/v1/auth/forgot-password",
                                "/api/v1/auth/verify-otp",
                                "/api/v1/auth/reset-password",
                                "/api/v1/auth/verify-account/**",
                                "/api/v1/auth/signup",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/health"
                        ).permitAll()

                        // Role-based access
                        .requestMatchers("/api/super-admin/**").hasRole(
                                UserRole.SUPER_ADMIN.name()
                        ).requestMatchers("/api/admin/**").hasAnyRole(
                                UserRole.SUPER_ADMIN.name(),
                                UserRole.ADMIN.name()
                        )
                        .requestMatchers("/api/manager/**").hasAnyRole(
                                UserRole.MANAGER.name(),
                                UserRole.ADMIN.name(),
                                UserRole.SUPER_ADMIN.name()
                        )
                        .requestMatchers("/api/employee/**").hasAnyRole(
                                UserRole.EMPLOYEE.name(),
                                UserRole.MANAGER.name(),
                                UserRole.ADMIN.name(),
                                UserRole.SUPER_ADMIN.name()
                        )

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider.authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}