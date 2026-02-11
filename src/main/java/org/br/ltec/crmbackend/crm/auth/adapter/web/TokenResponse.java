package org.br.ltec.crmbackend.crm.auth.adapter.web;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
  private String accessToken;
  private String tokenType;
}