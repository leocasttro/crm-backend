package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "pedido_opme_itens",
        indexes = {
                @Index(name = "idx_opme_pedido_id", columnList = "pedido_id")
        }
)
@Getter
@Setter
public class OpmeItemJpaEntity implements Persistable<UUID> {

  @Id
  @Column(name = "id", updatable = false)
  private UUID id;

  @Transient
  private boolean isNew = true;

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return isNew || id == null;
  }

  @PostLoad
  protected void markNotNew() {
    this.isNew = false;
  }

  @PrePersist
  protected void prePersist() {
    this.isNew = true;
    if (this.id == null) {
      this.id = UUID.randomUUID();
    }
    this.criadoEm = LocalDateTime.now();
    this.atualizadoEm = LocalDateTime.now();
  }

  @PreUpdate
  protected void preUpdate() {
    this.isNew = false;
    this.atualizadoEm = LocalDateTime.now();
  }

  @Column(name = "pedido_id", nullable = false)
  private UUID pedidoId;

  @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
  private String descricao;

  @Column(name = "quantidade", nullable = false)
  private Integer quantidade;

  // Armazenado como string separada por vírgula: "MEDTRONIC,XOMED,RICHARDS"
  @Column(name = "marcas_aceitas", columnDefinition = "TEXT")
  private String marcasAceitas;

  // Texto livre: "Não cotar com: Delphi, Osteofix, Delta"
  @Column(name = "marcas_negadas", columnDefinition = "TEXT")
  private String marcasNegadas;

  @Column(name = "observacao", columnDefinition = "TEXT")
  private String observacao;

  @Column(name = "criado_em")
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;
}