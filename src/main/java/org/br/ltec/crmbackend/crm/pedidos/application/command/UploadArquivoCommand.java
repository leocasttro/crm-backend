package org.br.ltec.crmbackend.crm.pedidos.application.command;

import org.springframework.web.multipart.MultipartFile;

public class UploadArquivoCommand {
  private final String pedidoId;
  private final MultipartFile arquivo;
  private final String observacao;
  private final String usuario;
  private Long checklistItemId;

  public UploadArquivoCommand(String pedidoId, MultipartFile arquivo, String observacao, String usuario) {
    this.pedidoId = pedidoId;
    this.arquivo = arquivo;
    this.observacao = observacao;
    this.usuario = usuario;
  }

  public String getPedidoId() { return pedidoId; }
  public MultipartFile getArquivo() { return arquivo; }
  public String getObservacao() { return observacao; }
  public String getUsuario() { return usuario; }

  public Long getChecklistItemId() { return checklistItemId; }
  public void setChecklistItemId(Long checklistItemId) { this.checklistItemId = checklistItemId; }
  }