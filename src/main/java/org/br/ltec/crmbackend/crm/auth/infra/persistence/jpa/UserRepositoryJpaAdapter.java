package org.br.ltec.crmbackend.crm.auth.infra.persistence.jpa;

import lombok.RequiredArgsConstructor;
import org.br.ltec.crmbackend.crm.auth.domain.model.User;
import org.br.ltec.crmbackend.crm.auth.domain.port.UserRepository;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Email;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryJpaAdapter implements UserRepository {

  private final SpringDataUserJpaRepository jpa;

  public UserRepositoryJpaAdapter(SpringDataUserJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public User save(User user) {
    var saved = jpa.save(UserJpaMapper.toEntity(user));
    return UserJpaMapper.toDomain(saved);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return jpa.findById(id).map(UserJpaMapper::toDomain);
  }

  @Override
  public Optional<User> findByEmail(Email email) {
    return jpa.findByEmail(email).map(UserJpaMapper::toDomain);
  }
}