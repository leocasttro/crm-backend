package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.br.ltec.crmbackend.crm.pedidos.application.command.CreatePedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.application.command.CreatePedidoFromPdfCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Lateralidade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Prioridade;
import org.springframework.stereotype.Component;

@Component
public class PedidoExtraidoMapper {

  private static final DateTimeFormatter PDF_DATE =
          DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public CreatePedidoCommand toCreatePedidoCommand(
          PedidoExtraido extraido,
          CreatePedidoFromPdfCommand originalCommand
  ) {

    CreatePedidoCommand cmd = new CreatePedidoCommand();

    // Convênio / CID / Médico
    cmd.setConvenioNome(extraido.getConvenio());
    cmd.setCidCodigo(extraido.getCid());
    cmd.setMedicoSolicitanteNome(extraido.getMedicoNome());

    if (extraido.getCrmNumero() != null && !extraido.getCrmNumero().isBlank()) {
      // Se você quiser guardar "CRM-UF numero", você pode concatenar aqui.
      cmd.setMedicoSolicitanteCrm(extraido.getCrmNumero());
    }

    // Procedimento (pega o primeiro)
    if (extraido.getProcedimentos() != null && !extraido.getProcedimentos().isEmpty()) {
      var primeiro = extraido.getProcedimentos().get(0);
      cmd.setProcedimentoCodigoTUSS(primeiro.getCodigo());     // opcional
      cmd.setProcedimentoDescricao(primeiro.getDescricao());
    }

    // Data do pedido (LocalDate)
    if (extraido.getDataPedido() != null && !extraido.getDataPedido().isBlank()) {
      LocalDate data = LocalDate.parse(extraido.getDataPedido(), PDF_DATE);
      cmd.setDataPedido(data);
    }

    // Defaults (ajuste conforme seus valueObjects)
    cmd.setPrioridade(new Prioridade(Prioridade.Tipo.ELETIVA));
    cmd.setLateralidade(new Lateralidade (Lateralidade.Tipo.NAO_APLICAVEL));

    // Se você quiser preencher pacienteId a partir do request
    if (originalCommand != null && originalCommand.getPacienteId() != null) {
      cmd.setPacienteId(originalCommand.getPacienteId());
    }

    return cmd;
  }
}
