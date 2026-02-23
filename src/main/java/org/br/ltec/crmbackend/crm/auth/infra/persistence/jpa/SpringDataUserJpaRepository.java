package org.br.ltec.crmbackend.crm.auth.infra.persistence.jpa;

import org.br.ltec.crmbackend.crm.auth.domain.model.User;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Email;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
  Optional<UserJpaEntity> findByEmail(Email email);
}