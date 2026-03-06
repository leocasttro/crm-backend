package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoArquivoRepository;

public class UploadArquivoResponse {
  private final boolean sucesso;
  private final String mensagem;
  private final ArquivoInfo arquivo;

  private UploadArquivoResponse(boolean sucesso, String mensagem, ArquivoInfo arquivo) {
    this.sucesso = sucesso;
    this.mensagem = mensagem;
    this.arquivo = arquivo;
  }

  public static UploadArquivoResponse sucesso(
          PedidoArquivoRepository.PedidoArquivoSalvo salvo,
          String mensagem) {
    return new UploadArquivoResponse(
            true,
            mensagem,
            new ArquivoInfo(
                    salvo.id().toString(),
                    salvo.nomeOriginal(),
                    salvo.tamanhoBytes(),
                    salvo.sha256(),
                    salvo.checklistItemId()
            )
    );
  }

  public static UploadArquivoResponse erro(String mensagem) {
    return new UploadArquivoResponse(false, mensagem, null);
  }

  public boolean isSucesso() { return sucesso; }
  public String getMensagem() { return mensagem; }
  public ArquivoInfo getArquivo() { return arquivo; }

  public static class ArquivoInfo {
    private final String id;
    private final String nomeOriginal;
    private final long tamanhoBytes;
    private final String sha256;
    private final Long checklistItemId;

    public ArquivoInfo(String id, String nomeOriginal, long tamanhoBytes, String sha256, Long checklistItemId) {
      this.id = id;
      this.nomeOriginal = nomeOriginal;
      this.tamanhoBytes = tamanhoBytes;
      this.sha256 = sha256;
      this.checklistItemId = checklistItemId;
    }

    public String getId() { return id; }
    public String getNomeOriginal() { return nomeOriginal; }
    public long getTamanhoBytes() { return tamanhoBytes; }
    public String getSha256() { return sha256; }
    public Long getChecklistItemId() { return  checklistItemId; }
  }
}