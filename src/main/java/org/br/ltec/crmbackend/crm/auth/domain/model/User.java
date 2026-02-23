package org.br.ltec.crmbackend.crm.auth.domain.model;

import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Email;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Role;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class User {
  private final UUID id;
  private final String nome;
  private final Email email;

  // idealmente "senha" no domínio é um hash (não a senha em texto)
  private final String senhaHash;

  private final Role role;
  private final boolean ativo;

  private final LocalDateTime criadoEm;
  private final LocalDateTime atualizadoEm;

  private User(
          UUID id,
          String nome,
          Email email,
          String senhaHash,
          Role role,
          boolean ativo,
          LocalDateTime criadoEm,
          LocalDateTime atualizadoEm
  ) {
    this.id = Objects.requireNonNull(id, "id");
    this.nome = Objects.requireNonNull(nome, "nome");
    this.email = Objects.requireNonNull(email, "email");
    this.senhaHash = Objects.requireNonNull(senhaHash, "senhaHash");
    this.role = Objects.requireNonNull(role, "role");
    this.ativo = ativo;
    this.criadoEm = Objects.requireNonNull(criadoEm, "criadoEm");
    this.atualizadoEm = atualizadoEm; // pode ser null em criação
  }

  public static User novo(String nome, Email email, String senhaHash, Role role) {
    Objects.requireNonNull(nome, "nome");
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(senhaHash, "senhaHash");
    Objects.requireNonNull(role, "role");

    var agora = LocalDateTime.now();
    return new User(UUID.randomUUID(), nome, email, senhaHash, role, true, agora, null);
  }

  public static User reconstituir(
          UUID id,
          String nome,
          Email email,
          String senhaHash,
          Role role,
          boolean ativo,
          LocalDateTime criadoEm,
          LocalDateTime atualizadoEm
  ) {
    return new User(id, nome, email, senhaHash, role, ativo, criadoEm, atualizadoEm);
  }

  public User desativar() {
    if (!ativo) return this;
    return new User(id, nome, email, senhaHash, role, false, criadoEm, LocalDateTime.now());
  }

  public User ativar() {
    if (ativo) return this;
    return new User(id, nome, email, senhaHash, role, true, criadoEm, LocalDateTime.now());
  }

  public UUID getId() { return id; }
  public String getNome() { return nome; }
  public Email getEmail() { return email; }
  public String getSenhaHash() { return senhaHash; }
  public Role getRole() { return role; }
  public boolean isAtivo() { return ativo; }
  public LocalDateTime getCriadoEm() { return criadoEm; }
  public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}
