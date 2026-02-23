package org.br.ltec.crmbackend.crm.auth.infra.persistence.jpa;

import org.br.ltec.crmbackend.crm.auth.domain.model.User;

public final class UserJpaMapper {

  private UserJpaMapper() {}

  public static User toDomain(UserJpaEntity e) {
    return User.reconstituir(
            e.getId(),
            e.getNome(),
            e.getEmail(),
            e.getSenhaHash(),
            e.getRole(),
            e.isAtivo(),
            e.getCriadoEm(),
            e.getAtualizadoEm()
    );
  }

  public static UserJpaEntity toEntity(User u) {
    var e = new UserJpaEntity();
    e.setId(u.getId());
    e.setNome(u.getNome());
    e.setEmail(u.getEmail());
    e.setSenhaHash(u.getSenhaHash());
    e.setRole(u.getRole());
    e.setAtivo(u.isAtivo());
    e.setCriadoEm(u.getCriadoEm());
    e.setAtualizadoEm(u.getAtualizadoEm());
    return e;
  }
}
