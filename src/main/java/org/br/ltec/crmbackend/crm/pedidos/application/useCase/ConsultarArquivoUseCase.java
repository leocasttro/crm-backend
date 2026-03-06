package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.application.command.DownloadArquivoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoArquivoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.ResultadoOperacao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultarArquivoUseCase {

  private final PedidoArquivoRepository arquivoRepository;
  private final DownloadArquivoUseCase downloadUseCase;

  public ConsultarArquivoUseCase(PedidoArquivoRepository arquivoRepository, DownloadArquivoUseCase downloadArquivoUseCase) {
    this.arquivoRepository = arquivoRepository;
    this.downloadUseCase = downloadArquivoUseCase;
  }

  public ResultadoOperacao<Boolean> existeArquivoParaItem(String pedidoId, Long checklistItemId) {
    try {
      boolean existe = arquivoRepository.existeArquivoParaItem(UUID.fromString(pedidoId), checklistItemId);
      return ResultadoOperacao.sucesso(existe, "Consulta realizada com sucesso");
    } catch (Exception e) {
      return ResultadoOperacao.erro("Erro ao verificar arquivo: " + e.getMessage());
    }
  }

  public ResultadoOperacao<PedidoArquivoRepository.PedidoArquivoSalvo> buscarArquivoPorItem(String pedidoId, Long checklistItemId) {
    try {
      Optional<PedidoArquivoRepository.PedidoArquivoSalvo> arquivo = arquivoRepository.buscarPorPedidoEItem(
              UUID.fromString(pedidoId), checklistItemId);

      return arquivo.map(a -> ResultadoOperacao.sucesso(a, "Arquivo encontrado"))
              .orElse(ResultadoOperacao.erro("Arquivo não encontrado para o item" + checklistItemId));
    } catch (Exception e) {
      return ResultadoOperacao.erro("Erro ao buscar arquivo: " + e.getMessage());
    }
  }

  public ResultadoOperacao<List<PedidoArquivoRepository.PedidoArquivoSalvo>> listarArquivosDoPedido(
          String pedidoId
   ) {
    try {
      List<PedidoArquivoRepository.PedidoArquivoSalvo> arquivos = arquivoRepository.listarPorPedido(UUID.fromString(
              pedidoId
      ));

      return ResultadoOperacao.sucesso(arquivos, arquivos.isEmpty() ? "Nenhum arquivo encontrado" : arquivos.size() + " arquivos encontrados");
    } catch (Exception e) {
      return ResultadoOperacao.erro("Erro ao listar arquivos: " + e.getMessage());
    }
  }

  public ResultadoOperacao<DownloadArquivoUseCase.DownloadResult> downloadArquivoPorItem(
          String pedidoId,
          Long checklistItemId) {

    try {
      Optional<PedidoArquivoRepository.PedidoArquivoSalvo> arquivo =
              arquivoRepository.buscarPorPedidoEItem(UUID.fromString(pedidoId), checklistItemId);

      if (arquivo.isEmpty()) {
        return ResultadoOperacao.erro("Arquivo não encontrado para o item " + checklistItemId);
      }

      DownloadArquivoCommand command = new DownloadArquivoCommand(
              pedidoId,
              arquivo.get().id().toString()
      );

      DownloadArquivoUseCase.DownloadResult downloadResult = downloadUseCase.execute(command);

      if (!downloadResult.isSucesso()) {
        return ResultadoOperacao.erro("Erro ao fazer download: " + downloadResult.getNomeArquivo());
      }

      return ResultadoOperacao.sucesso(downloadResult, "Download realizado com sucesso");

    } catch (Exception e) {
      return ResultadoOperacao.erro("Erro ao fazer download: " + e.getMessage());
    }
  }
}
