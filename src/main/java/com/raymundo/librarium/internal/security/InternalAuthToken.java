package com.raymundo.librarium.internal.security;

import com.raymundo.librarium.internal.util.ServiceType;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

public class InternalAuthToken extends AbstractAuthenticationToken {

    private final String apiKey;
    private final ServiceType serviceType;

    private InternalAuthToken(String apiKey) {
        super(List.of());
        this.apiKey = apiKey;
        this.serviceType = null;
        setAuthenticated(false);
    }

    private InternalAuthToken(ServiceType serviceType) {
        super(List.of(serviceType.getAuthority()));
        this.apiKey = null;
        this.serviceType = serviceType;
        setAuthenticated(true);
    }

    public static InternalAuthToken unauthenticated(String apiKey) {
        return new InternalAuthToken(apiKey);
    }

    public static InternalAuthToken authenticated(ServiceType serviceType) {
        return new InternalAuthToken(serviceType);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKey != null ? apiKey : serviceType;
    }
}
