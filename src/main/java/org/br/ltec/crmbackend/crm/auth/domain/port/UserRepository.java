package org.br.ltec.crmbackend.crm.auth.domain.port;

import org.br.ltec.crmbackend.crm.auth.domain.model.User;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Email;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  User save(User user);
  Optional<User> findById(UUID id);
  Optional<User> findByEmail(Email email);
}