package org.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    // For this example, we'll hardcode the valid API keys and their owners.
    // In a real application, this would come from a database or a secure config.
    private static final String VALID_API_KEY_SALAH = "user-salah-key-12345";
    private static final String VALID_API_KEY_GUEST = "user-guest-key-67890";
    private static final String HEADER_API_KEY = "X-API-Key";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get the API key from the request header.
        String apiKey = request.getHeader(HEADER_API_KEY);

        // 2. Validate the key and create an Authentication object.
        if (VALID_API_KEY_SALAH.equals(apiKey)) {
            // Key is valid for user "salah".
            // We create an "Authentication" object representing this user.
            var authentication = new UsernamePasswordAuthenticationToken(
                    "salah", // The principal (username)
                    null, // No credentials needed
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // The user's roles/permissions
            );
            // 3. Set the Authentication in the SecurityContext.
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else if (VALID_API_KEY_GUEST.equals(apiKey)) {
            // Key is valid for user "guest".
            var authentication = new UsernamePasswordAuthenticationToken(
                    "guest",
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 4. Continue the filter chain.
        // If the key was invalid, the SecurityContext remains empty, and access will be denied later.
        filterChain.doFilter(request, response);
    }
}
