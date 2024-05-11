package com.raymundo.librarium.internal.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum ServiceType {
    AUTH_SERVICE("auth-service", new SimpleGrantedAuthority("AUTH_SERVICE_AUTHORITY"));

    private final String serviceName;
    private final GrantedAuthority authority;

    private static final Map<String, ServiceType> serviceNames = new HashMap<>();

    static {
        Arrays.stream(ServiceType.values())
                .forEach(service -> serviceNames.put(service.serviceName, service));
    }

    ServiceType(String serviceName, GrantedAuthority authority) {
        this.serviceName = serviceName;
        this.authority = authority;
    }

    public String getServiceName() {
        return serviceName;
    }

    public GrantedAuthority getAuthority() {
        return authority;
    }

    public static ServiceType getServiceType(String serviceName) {
        return serviceNames.get(serviceName);
    }
}