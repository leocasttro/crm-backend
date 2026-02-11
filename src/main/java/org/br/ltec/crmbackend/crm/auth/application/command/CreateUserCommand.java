package org.br.ltec.crmbackend.crm.auth.application.command;

import lombok.Builder;
import lombok.Getter;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Role;

@Getter
@Builder
public class CreateUserCommand {
  private final String nome;
  private final String email;
  private final String senha;
  private final Role role;
  private final Boolean ativo;

  public CreateUserCommand(String nome, String email, String senha, Role role, Boolean ativo) {
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.role = role;
    this.ativo = ativo;
  }
}