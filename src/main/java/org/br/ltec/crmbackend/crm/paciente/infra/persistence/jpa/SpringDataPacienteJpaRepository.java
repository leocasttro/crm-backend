package org.br.ltec.crmbackend.crm.paciente.infra.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataPacienteJpaRepository extends JpaRepository<PacienteJpaEntity, UUID> {

  Optional<PacienteJpaEntity> findByDocumentoNumero(String documentoNumero);

  Optional<PacienteJpaEntity> findByEmail(String email);

  boolean existsByDocumentoNumero(String documentoNumero);

  boolean existsByDocumentoNumeroAndIdNot(String documentoNumero, UUID id);

  boolean existsByEmail(String email);

  boolean existsByEmailAndIdNot(String email, UUID id);

  @Query("SELECT p FROM PacienteJpaEntity p WHERE LOWER(p.nomeCompleto) LIKE LOWER(CONCAT('%', :nome, '%'))")
  java.util.List<PacienteJpaEntity> findByNomeContainingIgnoreCase(@Param("nome") String nome);

  @Query("SELECT COUNT(p) FROM PacienteJpaEntity p WHERE LOWER(p.nomeCompleto) LIKE LOWER(CONCAT('%', :nome, '%'))")
  long countByNomeContainingIgnoreCase(@Param("nome") String nome);
}