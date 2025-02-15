package com.app.Echohub.security.Abstract;

public interface TokenService {

    String generateToken(String username);

    String extractUserName(String token);

    boolean isTokenExpired(String token);

    boolean validateToken(String token, String username);

}
