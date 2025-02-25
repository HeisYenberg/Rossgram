package com.heisyenberg.usersservice.services;

import com.heisyenberg.usersservice.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private static final long EXPIRATION_TIME = 100000 * 60 * 24;

  @Value("${token.signing.key}")
  private String jwtSigningKey;

  public String extractUsername(final String token) {
    return extractClaims(token, Claims::getSubject);
  }

  public String generateToken(final UserDetails userDetails) {
    final Map<String, Object> claims = new HashMap<>();
    if (userDetails instanceof User user) {
      claims.put("id", user.getId());
      claims.put("username", user.getUsername());
    }
    return generateToken(claims, userDetails);
  }

  public boolean isTokenValid(final String token, final UserDetails userDetails) {
    final String user = extractUsername(token);
    return user.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private String generateToken(final Map<String, Object> claims, final UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getSigningKey())
        .compact();
  }

  private boolean isTokenExpired(String token) {
    return extractExpirationTime(token).before(new Date());
  }

  private Date extractExpirationTime(String token) {
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
