package com.heisyenberg.gatewayservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  public static final String BEARER_PREFIX = "Bearer ";

  @Value("${token.signing.key}")
  private String jwtSigningKey;

  public boolean isTokenExpired(String token) {
    return extractExpirationTime(token).before(new Date());
  }

  public String getUserIdFromAuthHeader(final String authHeader) {
    String token = authHeader.substring(BEARER_PREFIX.length());
    return getUserIdFromToken(token);
  }

  public String getUserIdFromToken(final String token) {
    return extractClaims(token, claims -> claims.get("id", Long.class)).toString();
  }

  private Date extractExpirationTime(final String token) {
    return extractClaims(token, Claims::getExpiration);
  }

  private <T> T extractClaims(final String token, final Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(final String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
