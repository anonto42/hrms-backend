package com.hrmf.hrmf_project.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrmf.hrmf_project.constants.SecurityConstants;
import com.hrmf.hrmf_project.exception.AccessDeniedException;
import com.hrmf.hrmf_project.exception.AuthenticationException;
import com.hrmf.hrmf_project.service.JwtService;
import com.hrmf.hrmf_project.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtContextHolder jwtContextHolder;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {

            log.debug("Processing request for URI: {}", request.getRequestURI());

            if (SecurityConstants.isPublicEndpoint(request.getRequestURI())) {
                log.debug("Skipping JWT filter for public endpoint: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header for URI: {}", request.getRequestURI());
                throw new AuthenticationException("Missing or invalid Authorization header");
            }

            final String jwt = authHeader.substring(7);
            log.debug("JWT token extracted: {}", jwt.substring(0, Math.min(10, jwt.length())) + "...");

            final String userEmail = jwtService.extractEmail(jwt);
            log.debug("Extracted email from JWT: {}", userEmail);

            if (userEmail == null) {
                log.warn("Unable to extract username from JWT");
                throw new AuthenticationException("Invalid token: unable to extract user information");
            } else {
                jwtContextHolder.setCurrentUserEmail(userEmail);
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                log.debug("User details loaded for: {}", userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Successfully authenticated user: {}", userEmail);
                } else {
                    log.warn("JWT token validation failed for user: {}", userEmail);
                    throw new AuthenticationException("Invalid or expired token");
                }
            } else {
                log.debug("Authentication already exists in context");
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {

            int status;
            String code = "";

            if (ex instanceof UsernameNotFoundException) {
                status = HttpServletResponse.SC_NOT_FOUND;
                code = "AUTH_USER_NOT_FOUND";
            }
            else if (ex instanceof AuthenticationException) {
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "AUTH_FAILED";
            }
            else if (ex instanceof AccessDeniedException ||
                    ex instanceof org.springframework.security.access.AccessDeniedException) {
                status = HttpServletResponse.SC_FORBIDDEN;
            }
            else {
                status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                code = "AUTH_UNKNOWN_ERROR";
            }

            response.setStatus(status);
            response.setContentType("application/json");

            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getClass().getSimpleName());
            error.put("message", ex.getMessage());
            error.put("code", code);

            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(error)
            );
        }

    }
}