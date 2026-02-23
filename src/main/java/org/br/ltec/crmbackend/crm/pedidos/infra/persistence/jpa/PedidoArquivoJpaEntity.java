package org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedido_arquivo")
public class PedidoArquivoJpaEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "pedido_id", nullable = false, unique = true)
  private UUID pedidoId;

  @Column(name = "caminho", nullable = false, length = 1024)
  private String caminho;

  @Column(name = "nome_original", nullable = false, length = 255)
  private String nomeOriginal;

  @Column(name = "content_type", nullable = false, length = 100)
  private String contentType;

  @Column(name = "tamanho_bytes", nullable = false)
  private long tamanhoBytes;

  @Column(name = "sha256", nullable = false, length = 64)
  private String sha256;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public PedidoArquivoJpaEntity() {}

  @PrePersist
  void prePersist() {
    if (this.id == null) this.id = UUID.randomUUID();
    if (this.criadoEm == null) this.criadoEm = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getPedidoId() {
    return pedidoId;
  }

  public void setPedidoId(UUID pedidoId) {
    this.pedidoId = pedidoId;
  }

  public String getCaminho() {
    return caminho;
  }

  public void setCaminho(String caminho) {
    this.caminho = caminho;
  }

  public String getNomeOriginal() {
    return nomeOriginal;
  }

  public void setNomeOriginal(String nomeOriginal) {
    this.nomeOriginal = nomeOriginal;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public long getTamanhoBytes() {
    return tamanhoBytes;
  }

  public void setTamanhoBytes(long tamanhoBytes) {
    this.tamanhoBytes = tamanhoBytes;
  }

  public String getSha256() {
    return sha256;
  }

  public void setSha256(String sha256) {
    this.sha256 = sha256;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
