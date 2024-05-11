package com.raymundo.librarium.internal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class InternalAuthFilter extends OncePerRequestFilter {

    private final String apiKeyHeaderName;
    private final AuthenticationManager authManager;
    private final SecurityContextHolderStrategy contextHolderStrategy;

    public InternalAuthFilter(String apiKeyHeaderName,
                              AuthenticationManager authManager,
                              SecurityContextHolderStrategy contextHolderStrategy) {
        this.apiKeyHeaderName = apiKeyHeaderName;
        this.authManager = authManager;
        this.contextHolderStrategy = contextHolderStrategy;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var receivedApiKey = response.getHeader(apiKeyHeaderName);
        var authToken = InternalAuthToken.unauthenticated(receivedApiKey);
        try {
            var authentication = authManager.authenticate(authToken);
            var securityContext = contextHolderStrategy.createEmptyContext();
            securityContext.setAuthentication(authentication);
            contextHolderStrategy.setContext(securityContext);
        } catch (AuthenticationException e) {
            logger.info(e.getMessage(), e);
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
