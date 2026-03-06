package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.application.command.DownloadArquivoCommand;
import org.br.ltec.crmbackend.crm.pedidos.infra.persistence.jpa.PedidoArquivoRepositoryJpaAdapter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class DownloadArquivoUseCase {

  private final PedidoArquivoRepositoryJpaAdapter metadataRepository;

  public DownloadArquivoUseCase(PedidoArquivoRepositoryJpaAdapter metadataRepository) {
    this.metadataRepository = metadataRepository;
  }

  public DownloadResult execute(DownloadArquivoCommand command) {
    try {
      UUID pedidoId = UUID.fromString(command.getPedidoId());

      // Busca metadados do arquivo usando o adapter JPA
      var metadadosOpt = metadataRepository.buscarPedidoId(pedidoId);

      if (metadadosOpt.isEmpty()) {
        return DownloadResult.erro("Arquivo não encontrado para este pedido");
      }

      var metadados = metadadosOpt.get();

      // Lê o arquivo do disco usando o caminho salvo nos metadados
      Path caminho = Paths.get(metadados.caminho());

      if (!Files.exists(caminho)) {
        return DownloadResult.erro("Arquivo físico não encontrado no caminho: " + caminho);
      }

      byte[] conteudo = Files.readAllBytes(caminho);

      return DownloadResult.sucesso(
              conteudo,
              metadados.contentType(),
              metadados.nomeOriginal()
      );

    } catch (IOException e) {
      return DownloadResult.erro("Erro ao ler o arquivo: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      return DownloadResult.erro("ID de pedido inválido: " + command.getPedidoId());
    }
  }

  public static class DownloadResult {
    private final boolean sucesso;
    private final byte[] conteudo;
    private final String contentType;
    private final String nomeArquivo;
    private final String mensagemErro;

    private DownloadResult(boolean sucesso, byte[] conteudo, String contentType,
                           String nomeArquivo, String mensagemErro) {
      this.sucesso = sucesso;
      this.conteudo = conteudo;
      this.contentType = contentType;
      this.nomeArquivo = nomeArquivo;
      this.mensagemErro = mensagemErro;
    }

    public static DownloadResult sucesso(byte[] conteudo, String contentType, String nomeArquivo) {
      return new DownloadResult(true, conteudo, contentType, nomeArquivo, null);
    }

    public static DownloadResult erro(String mensagem) {
      return new DownloadResult(false, null, null, null, mensagem);
    }

    public boolean isSucesso() { return sucesso; }
    public byte[] getConteudo() { return conteudo; }
    public String getContentType() { return contentType; }
    public String getNomeArquivo() { return nomeArquivo; }
    public String getMensagemErro() { return mensagemErro; }
  }
}