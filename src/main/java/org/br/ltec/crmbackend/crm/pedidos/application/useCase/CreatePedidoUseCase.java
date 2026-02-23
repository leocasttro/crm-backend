package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.paciente.application.useCase.CreatePacienteUseCase;  // IMPORT!
import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.port.PacienteRepository;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.pedidos.application.command.CreatePedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoBuilder;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatePedidoUseCase {

  private final PedidoRepository pedidoRepository;
  private final PacienteRepository pacienteRepository;
  private final CreatePacienteUseCase createPacienteUseCase;  // ✅ ADICIONADO!
  private final PedidoBuilder pedidoBuilder;

  // ✅ CONSTRUTOR ATUALIZADO
  public CreatePedidoUseCase(PedidoRepository pedidoRepository,
                             PacienteRepository pacienteRepository,
                             CreatePacienteUseCase createPacienteUseCase,  // ✅ NOVO PARÂMETRO
                             PedidoBuilder pedidoBuilder) {
    this.pedidoRepository = pedidoRepository;
    this.pacienteRepository = pacienteRepository;
    this.createPacienteUseCase = createPacienteUseCase;  // ✅ ATRIBUIR
    this.pedidoBuilder = pedidoBuilder;
  }

  public PedidoCirurgico execute(CreatePedidoCommand command) {

    PacienteId pacienteId = null;

    // ✅ AGORA FUNCIONA!
    if (command.getPaciente() != null) {
      // Cria o paciente com os dados do command
      Paciente novoPaciente = createPacienteUseCase.execute(command.getPaciente());
      pacienteId = novoPaciente.getId();
      System.out.println(">>> Paciente criado com ID: " + pacienteId);
    }
    else if (command.getPacienteId() != null && !command.getPacienteId().isBlank()) {
      pacienteId = PacienteId.fromString(command.getPacienteId());
    }
    else {
      throw new IllegalArgumentException("É necessário informar dados do paciente");
    }

    // Validar convênio (só se tiver validade)
    if (command.getConvenioValidadeCarteira() != null &&
            command.getConvenioValidadeCarteira().isBefore(java.time.LocalDate.now())) {
      throw new IllegalArgumentException("Convênio está vencido");
    }

    try {
      // Criar Value Objects
      PedidoId pedidoId = PedidoId.generate();

      // Médico solicitante
      Medico medicoSolicitante = new Medico(
              command.getMedicoSolicitanteNome(),
              command.getMedicoSolicitanteCrm(),
              command.getMedicoSolicitanteEspecialidade()
      );

      // Médico executor (se fornecido)
      Medico medicoExecutor = null;
      if (command.getMedicoExecutorNome() != null && !command.getMedicoExecutorNome().trim().isEmpty()) {
        medicoExecutor = new Medico(
                command.getMedicoExecutorNome(),
                command.getMedicoExecutorCrm(),
                command.getMedicoExecutorEspecialidade()
        );
      }

      // Procedimento
      Procedimento procedimento = new Procedimento(
              command.getProcedimentoCodigoTUSS(),
              command.getProcedimentoDescricao(),
              command.getProcedimentoCategoria()
      );

      // Convênio
      Convenio convenio = new Convenio(
              command.getConvenioNome(),
              command.getConvenioNumeroCarteira(),
              command.getConvenioValidadeCarteira(),
              command.getConvenioTipoPlano()
      );

      // CID (se fornecido)
      CID cid = null;
      if (command.getCidCodigo() != null && !command.getCidCodigo().trim().isEmpty()) {
        cid = new CID(command.getCidCodigo(), command.getCidDescricao());
      }

      // Agendamento (se fornecido)
      DataHoraAgendamento agendamento = null;
      if (command.getAgendamentoDataHora() != null) {
        agendamento = new DataHoraAgendamento(
                command.getAgendamentoDataHora(),
                command.getAgendamentoSala(),
                command.getAgendamentoDuracaoEstimada(),
                command.getAgendamentoObservacoes()
        );
      }

      // Status
      StatusPedido status = new StatusPedido(command.getStatus());

      // Prioridade
      Prioridade prioridade = command.getPrioridade();

      // Lateralidade
      Lateralidade lateralidade = command.getLateralidade() != null ?
              command.getLateralidade() :
              new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL);

      // Criar pedido usando o builder
      PedidoCirurgico pedido = pedidoBuilder
              .comId(pedidoId)
              .comPacienteId(pacienteId)
              .comMedicoSolicitante(medicoSolicitante)
              .comMedicoExecutor(medicoExecutor)
              .comProcedimento(procedimento)
              .comConvenio(convenio)
              .comCid(cid)
              .comAgendamento(agendamento)
              .comStatus(status)
              .comPrioridade(prioridade)
              .comLateralidade(lateralidade)
              .comObservacoes(command.getObservacoes())
              .comDocumentosAnexados(command.getDocumentosAnexados())
              .comUsuarioCriacao(command.getUsuarioCriacao())
              .comDataPedido(command.getDataPedido())
              .build();

      // Salvar pedido
      return pedidoRepository.salvar(pedido);

    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Dados inválidos fornecidos: " + e.getMessage(), e);
    }
  }
}