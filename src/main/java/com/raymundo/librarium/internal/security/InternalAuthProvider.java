package com.raymundo.librarium.internal.security;

import com.raymundo.librarium.internal.service.JwtService;
import com.raymundo.librarium.internal.util.ServiceType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class InternalAuthProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    public InternalAuthProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (supports(authentication.getClass())) {
            var internalAuthToken = (InternalAuthToken) authentication;
            var receivedApiKey = (String) internalAuthToken.getPrincipal();
            if (jwtService.isTokenValid(receivedApiKey)) {
                var serviceName = jwtService.getServiceName(receivedApiKey);
                var serviceType = ServiceType.getServiceType(serviceName);
                return InternalAuthToken.authenticated(serviceType);
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(InternalAuthToken.class);
    }
}
