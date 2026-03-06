package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.pedidos.application.command.UploadArquivoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoArquivoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.ResultadoOperacao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
public class UploadArquivoUseCase {

  private final PedidoArquivoRepository arquivoRepository;
  private final PedidoRepository pedidoRepository;

  public UploadArquivoUseCase(
          PedidoArquivoRepository arquivoRepository,
          PedidoRepository pedidoRepository) {
    this.arquivoRepository = arquivoRepository;
    this.pedidoRepository = pedidoRepository;
  }

  @Transactional
  public ResultadoOperacao<PedidoArquivoRepository.PedidoArquivoSalvo> execute(UploadArquivoCommand command) {
    try {
      // Verifica se o pedido existe
      PedidoId pedidoId = PedidoId.fromString(command.getPedidoId());
      if (!pedidoRepository.existePorId(pedidoId)) {
        return ResultadoOperacao.erro("Pedido não encontrado: " + command.getPedidoId());
      }

      if (command.getChecklistItemId() != null) {
        boolean existe = arquivoRepository.existeArquivoParaItem(
                UUID.fromString(command.getPedidoId()),
                command.getChecklistItemId()
        );

        if (existe) {
          return ResultadoOperacao.erro(
                  "Já existe um arquivo para o item " + command.getChecklistItemId() +
                          " do checklist. Remova o existente primeiro ou use outro item."
          );
        }
      }

      // Converte MultipartFile para ArquivoUpload
      PedidoArquivoRepository.ArquivoUpload upload = new PedidoArquivoRepository.ArquivoUpload(
              command.getArquivo().getOriginalFilename(),
              command.getArquivo().getContentType(),
              command.getArquivo().getBytes()
      );

      // Salva o arquivo
      PedidoArquivoRepository.PedidoArquivoSalvo salvo = arquivoRepository.salvar(
              UUID.fromString(command.getPedidoId()),
              upload,
              command.getChecklistItemId()
      );

      return ResultadoOperacao.sucesso(salvo, "Arquivo enviado com sucesso");

    } catch (IOException e) {
      return ResultadoOperacao.erro("Erro ao ler o arquivo: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      return ResultadoOperacao.erro("Erro na validação: " + e.getMessage());
    }
  }
}