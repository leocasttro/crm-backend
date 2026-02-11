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
@RequiredArgsConstructor
public class UserRepositoryJpaAdapter implements UserRepository {

  private final SpringDataUserJpaRepository springDataRepository;

  @Override
  public User save(User user) {
    return springDataRepository.save(user);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return springDataRepository.findById(id);
  }

  @Override
  public Optional<User> findByEmail(Email email) {
    return springDataRepository.findByEmailValue(email.getValue());
  }

  @Override
  public List<User> findAll() {
    return springDataRepository.findAll();
  }

  @Override
  public List<User> findByRole(String role) {
    return springDataRepository.findByRole(org.br.ltec.crmbackend.crm.auth.domain.valueObject.Role.valueOf(role));
  }

  @Override
  public boolean existsByEmail(Email email) {
    return springDataRepository.existsByEmailValue(email.getValue());
  }

  @Override
  public void deleteById(UUID id) {
    springDataRepository.deleteById(id);
  }

  @Override
  public void delete(User user) {
    springDataRepository.delete(user);
  }
}