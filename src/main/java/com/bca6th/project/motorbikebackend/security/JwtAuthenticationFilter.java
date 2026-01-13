package com.bca6th.project.motorbikebackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Skip JWT check for known public paths AND all GET requests to products
        if (isPublicPath(path) || isPublicProductsGet(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Normal JWT processing only for protected paths
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

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return pathMatcher.match("/api/auth/**", path) ||
                pathMatcher.match("/v3/api-docs/**", path) ||
                pathMatcher.match("/swagger-ui/**", path) ||
                pathMatcher.match("/swagger-ui.html", path) ||
                pathMatcher.match("/swagger-resources/**", path) ||
                pathMatcher.match("/webjars/**", path);
    }

    private boolean isPublicProductsGet(String path, String method) {
        return "GET".equalsIgnoreCase(method) &&
                pathMatcher.match("/api/products/**", path);
    }

    private String parseJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7).trim();
            if (!jwt.isEmpty() && !jwt.equals("null") && !jwt.equals("undefined")) {
                return jwt;
            }
        }
        return null;
    }
}