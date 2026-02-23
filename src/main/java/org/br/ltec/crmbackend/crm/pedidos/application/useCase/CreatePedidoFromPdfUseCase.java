package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import java.util.UUID;

import org.br.ltec.crmbackend.crm.pedidos.application.command.CreatePedidoFromPdfCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoArquivoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoPdfExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreatePedidoFromPdfUseCase {

  private final PedidoPdfExtractor pedidoPdfExtractor;
  private final PedidoExtraidoMapper pedidoExtraidoMapper;
  private final CreatePedidoUseCase createPedidoUseCase;
  private final PedidoArquivoRepository pedidoArquivoRepository;

  public CreatePedidoFromPdfUseCase(
          PedidoPdfExtractor pedidoPdfExtractor,
          PedidoExtraidoMapper pedidoExtraidoMapper,
          CreatePedidoUseCase createPedidoUseCase,
          PedidoArquivoRepository pedidoArquivoRepository
  ) {
    this.pedidoPdfExtractor = pedidoPdfExtractor;
    this.pedidoExtraidoMapper = pedidoExtraidoMapper;
    this.createPedidoUseCase = createPedidoUseCase;
    this.pedidoArquivoRepository = pedidoArquivoRepository;
  }

  @Transactional
  public PedidoCirurgico execute(CreatePedidoFromPdfCommand command) {
    if (command == null || command.getPdfBytes() == null || command.getPdfBytes().length == 0) {
      throw new IllegalArgumentException("PDF é obrigatório.");
    }

    PedidoExtraido extraido = pedidoPdfExtractor.extract(command.getPdfBytes());
    var createPedidoCommand = pedidoExtraidoMapper.toCreatePedidoCommand(extraido, command);

    PedidoCirurgico pedido = createPedidoUseCase.execute(createPedidoCommand);

    UUID pedidoId = pedido.getId().getValue();

    pedidoArquivoRepository.salvar(
            pedidoId,
            new PedidoArquivoRepository.ArquivoUpload(
                    command.getOriginalFilename(),
                    command.getContentType(),
                    command.getPdfBytes()
            )
    );

    return pedido;
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
