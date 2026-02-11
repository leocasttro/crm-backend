package org.br.ltec.crmbackend.crm.auth.adapter.web;

import lombok.Builder;
import lombok.Getter;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponse {
  private UUID id;
  private String nome;
  private String email;
  private Role role;
  private Boolean ativo;
  private LocalDateTime criadoEm;
}