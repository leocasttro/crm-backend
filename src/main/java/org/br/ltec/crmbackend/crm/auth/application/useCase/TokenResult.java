package org.br.ltec.crmbackend.crm.auth.application.useCase;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TokenResult {
  private final String accessToken;
  private final String tokenType;
  private final LocalDateTime issuedAt;
  private final LocalDateTime expiresAt;
  private final String username;
  private final String role;

  public static TokenResult fromToken(String token, String username, String role) {
    return TokenResult.builder()
            .accessToken(token)
            .tokenType("Bearer")
            .issuedAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusHours(24)) // Ajuste conforme sua configuração
            .username(username)
            .role(role)
            .build();
  }
}