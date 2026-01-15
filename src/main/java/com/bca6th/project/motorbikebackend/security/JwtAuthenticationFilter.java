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


    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1. Skip the entire JWT validation for known public paths
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Only for protected paths: try to process JWT
        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateToken(jwt)) {
                String email = jwtUtils.getEmailFromToken(jwt);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            // If token is invalid/missing → just continue (no auth set) → security chain will reject if needed
        } catch (Exception e) {
            // Optional: log the error, but do NOT throw — let the request continue to .anyRequest().authenticated()
            logger.warn("JWT processing failed for path: {}, reason: {}", path, e.getMessage());
        }

        // 3. Always continue the chain
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        // Add this debug line
        System.out.println("Authorization Header: " + header);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("JWT Token extracted: " + token.substring(0, 20) + "...");
            return token;
        }

        System.out.println("No valid Authorization header found");
        return null;
    }

    //Helper method to check if the path is public
    private boolean isPublicPath(String path) {
        // List of ant-style patterns for public paths
        List<String> publicPatterns = List.of(
                "/api/auth/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**",
                "/api/products",           // exact match
                "/api/products/**"         // covers /api/products/9, /api/products?page=..., etc.
        );

        PathMatcher pathMatcher = new AntPathMatcher();

        return publicPatterns.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}