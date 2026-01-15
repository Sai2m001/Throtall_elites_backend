package com.bca6th.project.motorbikebackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");

        logger.info("JWT Filter - Path: {}, Auth Header: {}", path, authHeader);

        // Skip JWT validation ONLY for truly public paths
        if (isPublicPath(path)) {
            logger.info("Skipping JWT for public path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        // For protected paths: process the token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("No valid Bearer token for protected path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        logger.info("Extracted JWT: {}...", jwt.substring(0, 20));

        try {
            if (jwtUtils.validateToken(jwt)) {
                String email = jwtUtils.getEmailFromToken(jwt);
                logger.info("Valid token for user: {}", email);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Authentication set for user: {}", email);
                }
            } else {
                logger.warn("Invalid JWT token for path: {}", path);
            }
        } catch (Exception e) {
            logger.error("JWT processing failed for path: {}", path, e);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        // IMPORTANT CHANGE: Removed "/api/products/**" from public paths
        // Security chain already handles GET /api/products/** as permitAll
        // POST/PUT/DELETE/PATCH must be validated here
        List<String> publicPatterns = List.of(
                "/api/auth/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**"
                // Do NOT add /api/products/** here anymore
        );

        return publicPatterns.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        System.out.println("Authorization Header: " + header);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("JWT Token extracted: " + token.substring(0, 20) + "...");
            return token;
        }

        System.out.println("No valid Authorization header found");
        return null;
    }
}