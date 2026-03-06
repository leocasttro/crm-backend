package org.br.ltec.crmbackend.crm.pedidos.application.command;

public class DownloadArquivoCommand {
  private final String pedidoId;
  private final String arquivoId; // NOVO CAMPO

  public DownloadArquivoCommand(String pedidoId, String arquivoId) {
    this.pedidoId = pedidoId;
    this.arquivoId = arquivoId;
  }

  public String getPedidoId() { return pedidoId; }
  public String getArquivoId() { return arquivoId; } // NOVO GETTER
}