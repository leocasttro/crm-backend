package org.br.ltec.crmbackend.crm.auth.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginCommand {
  private final String email;
  private final String senha;

  public LoginCommand(String email, String senha) {
    this.email = email;
    this.senha = senha;
  }
}