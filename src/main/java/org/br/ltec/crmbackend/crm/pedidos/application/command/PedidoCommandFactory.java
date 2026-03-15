package org.br.ltec.crmbackend.crm.pedidos.application.command;

import org.br.ltec.crmbackend.crm.pedidos.adapter.web.CreatePedidoRequest;
import org.br.ltec.crmbackend.crm.pedidos.adapter.web.UpdatePedidoRequest;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Lateralidade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Prioridade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoCommandFactory {

  public CreatePedidoCommand toCreateCommand(CreatePedidoRequest req, String pacienteId, String usuario) {
    List<String> observacoes = new ArrayList<>();
    if (req.getObservacaoInicial() != null && !req.getObservacaoInicial().trim().isEmpty()) {
      observacoes.add(req.getObservacaoInicial());
    }

    return CreatePedidoCommand.builder()
            .pacienteId(pacienteId)
            .usuarioCriacao(usuario)
            .dataPedido(req.getDataPedido() != null ? req.getDataPedido() : LocalDate.now())
            .medicoSolicitanteNome(req.getMedicoSolicitanteNome())
            .medicoSolicitanteCrm(req.getMedicoSolicitanteCrm())
            .medicoSolicitanteEspecialidade(req.getMedicoSolicitanteEspecialidade())
            .procedimentoCodigoTUSS(req.getProcedimentoCodigo())
            .procedimentoDescricao(req.getProcedimentoNome())
            .procedimentoCategoria(req.getProcedimentoCategoria())
            .convenioNome(req.getConvenioNome())
            .convenioNumeroCarteira(req.getConvenioNumeroCarteira())
            .convenioValidadeCarteira(req.getConvenioValidadeCarteira())
            .convenioTipoPlano(req.getConvenioTipoPlano())
            .cidCodigo(req.getCidCodigo())
            .cidDescricao(req.getCidDescricao())
            .prioridade(new Prioridade(Prioridade.Tipo.fromString(req.getPrioridade()), req.getPrioridadeJustificativa()))
            .lateralidade(req.getLateralidade() != null ? new Lateralidade(req.getLateralidade()) : null)
            .status(StatusPedido.Tipo.RASCUNHO)
            .observacoes(observacoes)
            .documentosAnexados(new ArrayList<>())
            .agendamentoDataHora(req.getDataAgendamentoPretendida())
            .agendamentoObservacoes(req.getDataAgendamentoPretendida() != null ? "Agendamento pretendido informado na criação" : null)
            .build();
  }

  public UpdatePedidoCommand toUpdateCommand(String id, UpdatePedidoRequest req, String usuario) {
    UpdatePedidoCommand command = new UpdatePedidoCommand();
    command.setPedidoId(id);
    command.setUsuarioAtualizacao(usuario);
    command.setNomePaciente(req.getNomePaciente());
    command.setDataNascimento(req.getDataNascimento());
    command.setCpfPaciente(req.getCpfPaciente());
    command.setEmailPaciente(req.getEmailPaciente());
    command.setTelefonePaciente(req.getTelefonesPaciente());
    command.setSexoPaciente(req.getSexoPaciente());
    command.setEnderecoPaciente(req.getEnderecoPaciente());
    command.setMedicoSolicitanteNome(req.getMedicoSolicitanteNome());
    command.setMedicoSolicitanteCrm(req.getMedicoSolicitanteCrm());
    command.setMedicoSolicitanteEspecialidade(req.getMedicoSolicitanteEspecialidade());
    command.setProcedimentoCodigoTUSS(req.getProcedimentoCodigoTUSS());
    command.setProcedimentoDescricao(req.getProcedimentoDescricao());
    command.setProcedimentoCategoria(req.getProcedimentoCategoria());
    command.setConvenioNome(req.getConvenioNome());
    command.setConvenioNumeroCarteira(req.getConvenioNumeroCarteira());
    command.setConvenioValidadeCarteira(req.getConvenioValidadeCarteira());
    command.setConvenioTipoPlano(req.getConvenioTipoPlano());
    command.setCidCodigo(req.getCidCodigo());
    command.setCidDescricao(req.getCidDescricao());
    command.setDataPedido(req.getDataPedido());
    command.setObservacoes(req.getObservacoes());

    if (req.getPrioridade() != null) {
      command.setPrioridade(new Prioridade(Prioridade.Tipo.fromString(req.getPrioridade())));
    }

    return command;
  }
}
