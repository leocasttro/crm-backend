package org.br.ltec.crmbackend.crm.pedidos.application.command;

public class ExcluirArquivoCommand {
  private final String arquivoId;
  private final String usuario;

  public ExcluirArquivoCommand(String arquivoId, String usuario) {
    this.arquivoId = arquivoId;
    this.usuario = usuario;
  }

  public String getArquivoId() { return arquivoId; }
  public String getUsuario() { return usuario; }
}