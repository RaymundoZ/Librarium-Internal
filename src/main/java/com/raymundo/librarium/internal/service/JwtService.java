package com.raymundo.librarium.internal.service;

import com.raymundo.librarium.internal.util.ServiceType;

public interface JwtService {

    String generateToken(ServiceType from, ServiceType to);

    boolean isTokenValid(String token);

    String getServiceName(String token);

    String getServiceAuthority(String token);
}
