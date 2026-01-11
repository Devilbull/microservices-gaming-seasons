package com.vuckoapp.gamingservice.security;

import com.vuckoapp.gamingservice.dto.JwtUserPrincipal;
import com.vuckoapp.gamingservice.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie c : request.getCookies()) {
            if ("jwt".equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && jwtUtil.isValid(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String email = jwtUtil.extractEmail(token);
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);
            String userId = jwtUtil.extractUserId(token);

            JwtUserPrincipal  jwtUserPrincipal = new JwtUserPrincipal(userId, username, email, role);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            jwtUserPrincipal,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );
            auth.setDetails(email);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }



        filterChain.doFilter(request, response);
    }
}