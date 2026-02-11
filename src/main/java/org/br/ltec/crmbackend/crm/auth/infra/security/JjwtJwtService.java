package org.br.ltec.crmbackend.crm.auth.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.br.ltec.crmbackend.crm.auth.application.useCase.JwtService;
import org.br.ltec.crmbackend.crm.auth.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JjwtJwtService implements JwtService {

  @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
  private String secretKey;

  private long jwtExpiration = 86400000L; // Valor padrão: 24 horas

  private long refreshExpiration = 604800000L; // Valor padrão: 7 dias

  @Value("${jwt.expiration:#{86400000}}")
  public void setJwtExpiration(String value) {
    this.jwtExpiration = parseLongSafely(value, 86400000L);
  }

  @Value("${jwt.refresh-expiration:#{604800000}}")
  public void setRefreshExpiration(String value) {
    this.refreshExpiration = parseLongSafely(value, 604800000L);
  }

  private long parseLongSafely(String value, long defaultValue) {
    if (value == null) {
      return defaultValue;
    }

    try {
      // Remove qualquer texto não numérico
      String cleanValue = value.replaceAll("[^0-9]", "");
      if (cleanValue.isEmpty()) {
        return defaultValue;
      }
      return Long.parseLong(cleanValue);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  @Override
  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getRole().name());
    claims.put("userId", user.getId().toString());
    claims.put("nome", user.getNome());

    return generateToken(claims, user);
  }

  private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  @Override
  public boolean isTokenValid(String token) {
    try {
      return !isTokenExpired(token);
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}