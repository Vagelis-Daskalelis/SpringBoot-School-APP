package com.vaggelis.SpringSchool.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJWTService {
    String extractUsername(String token);

    String extractUserName(String token);

    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
}
