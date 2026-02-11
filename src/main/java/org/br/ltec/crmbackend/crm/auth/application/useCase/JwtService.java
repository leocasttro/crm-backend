package org.br.ltec.crmbackend.crm.auth.application.useCase;

import org.br.ltec.crmbackend.crm.auth.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
  String extractUsername(String token);
  String generateToken(UserDetails userDetails);
  String generateToken(User user);
  boolean isTokenValid(String token, UserDetails userDetails);
  boolean isTokenValid(String token);
  boolean isTokenExpired(String token);
}