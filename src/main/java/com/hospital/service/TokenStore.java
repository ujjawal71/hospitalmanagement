package com.hospital.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

    private final Map<String, TokenInfo> tokens = new ConcurrentHashMap<>();

    public String createToken(String role, Long userId) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new TokenInfo(role, userId));
        return token;
    }

    public TokenInfo getTokenInfo(String token) {
        return token == null ? null : tokens.get(token);
    }

    public void removeToken(String token) {
        if (token != null) tokens.remove(token);
    }

    public record TokenInfo(String role, Long userId) {}
}
