package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.br.ltec.crmbackend.crm.pedidos.adapter.web.ImportPedidoPdfResponse;
import org.br.ltec.crmbackend.crm.pedidos.application.command.CreatePedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.OpmeItem;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.OpmeItemRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoArquivoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoPdfExtractor;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.OpmeItemId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CreatePedidoFromPdfUseCase {

  private final PedidoPdfExtractor pedidoPdfExtractor;
  private final PedidoExtraidoMapper pedidoExtraidoMapper;
  private final CreatePedidoUseCase createPedidoUseCase;
  private final PedidoArquivoRepository pedidoArquivoRepository;
  private final OpmeItemRepository opmeItemRepository;

  public CreatePedidoFromPdfUseCase(
          PedidoPdfExtractor pedidoPdfExtractor,
          PedidoExtraidoMapper pedidoExtraidoMapper,
          CreatePedidoUseCase createPedidoUseCase,
          PedidoArquivoRepository pedidoArquivoRepository,
          OpmeItemRepository opmeItemRepository
  ) {
    this.pedidoPdfExtractor = pedidoPdfExtractor;
    this.pedidoExtraidoMapper = pedidoExtraidoMapper;
    this.createPedidoUseCase = createPedidoUseCase;
    this.pedidoArquivoRepository = pedidoArquivoRepository;
    this.opmeItemRepository = opmeItemRepository;
  }

  @Transactional
  public ImportPedidoPdfResponse execute(MultipartFile arquivo) {

    if (arquivo == null || arquivo.isEmpty()) {
      throw new IllegalArgumentException("Arquivo PDF é obrigatório");
    }

    if (!"application/pdf".equals(arquivo.getContentType())) {
      throw new IllegalArgumentException("Arquivo deve ser PDF");
    }

    try {
      byte[] pdfBytes = arquivo.getBytes();

      PedidoExtraido extraido = pedidoPdfExtractor.extract(pdfBytes);

      CreatePedidoCommand createPedidoCommand = pedidoExtraidoMapper.toCreatePedidoCommand(extraido);

      PedidoCirurgico pedido = createPedidoUseCase.execute(createPedidoCommand);

      if (extraido.getOpmeItens() != null && !extraido.getOpmeItens().isEmpty()) {
        List<OpmeItem> itens = extraido.getOpmeItens().stream()
                .map(opme -> OpmeItem.builder()
                        .id(OpmeItemId.fromString(UUID.randomUUID().toString()))
                        .pedidoId(pedido.getId())
                        .descricao(opme.getDescricao())
                        .quantidade(opme.getQuantidade())
                        .marcasAceitas(opme.getMarcasAceitas())
                        .marcasNegadas(opme.getMarcasNegadas())
                        .build())
                .collect(Collectors.toList());

        opmeItemRepository.saveAll(itens);
      }

      List<ImportPedidoPdfResponse.ProcedimentoResponse> procedimentosResponse =
              extraido.getProcedimentos().stream()
                      .map(p -> new ImportPedidoPdfResponse.ProcedimentoResponse(
                              p.getCodigo(),
                              p.getDescricao(),
                              p.getQuantidade()
                      ))
                      .collect(Collectors.toList());

      return ImportPedidoPdfResponse.sucesso(
              pedido.getId().getValue().toString(),
              pedido.getPacienteId() != null ? pedido.getPacienteId().getValue().toString() : null,
              extraido,
              procedimentosResponse
      );

    } catch (IOException e) {
      throw new RuntimeException("Erro ao processar arquivo PDF", e);
    }
  }


  public static class Resultado {
    private String pedidoId;
    private String nomePaciente;
    private String convenio;
    private String cid;

    public String getPedidoId() { return pedidoId; }
    public void setPedidoId(String pedidoId) { this.pedidoId = pedidoId; }

    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }

    public String getConvenio() { return convenio; }
    public void setConvenio(String convenio) { this.convenio = convenio; }

    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }
  }
}
