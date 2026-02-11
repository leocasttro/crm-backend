package org.br.ltec.crmbackend.crm.auth.infra.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Email;
import org.br.ltec.crmbackend.crm.auth.domain.valueObject.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class UserJpaEntity {

  @Id
  @Column(name = "id", columnDefinition = "uuid")
  private UUID id;

  @Column(name = "nome", nullable = false)
  private String nome;

  @Embedded
  @AttributeOverrides({
          @AttributeOverride(name = "value", column = @Column(name = "email"))
  })
  private Email email;

  @Column(name = "senha", nullable = false)
  private String senha;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  @Column(name = "ativo", nullable = false)
  private boolean ativo;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;
}