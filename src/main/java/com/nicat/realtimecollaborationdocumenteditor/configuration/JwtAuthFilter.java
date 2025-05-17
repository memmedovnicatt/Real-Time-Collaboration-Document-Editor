package com.nicat.realtimecollaborationdocumenteditor.configuration;

import com.nicat.realtimecollaborationdocumenteditor.dao.document.User;
import com.nicat.realtimecollaborationdocumenteditor.dao.repository.TokenRepository;
import com.nicat.realtimecollaborationdocumenteditor.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    JwtUtil jwtUtil;
    CustomUserDetailsService customUserDetailsService;
    TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        log.info("EYVAZ{}",username);
        log.info("NICAT{}",token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User userDetails = (User) customUserDetailsService.loadUserByUsername(username);
            log.info("partladi??");
            var isValid = tokenRepository.findByAccessToken(token).map(t -> !t.getIsLoggedOut()).orElse(false);

            if (jwtUtil.isTokenValid(token, userDetails) && isValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, "", userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}