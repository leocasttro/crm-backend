package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.paciente.application.useCase.CreatePacienteUseCase;
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
  private final CreatePacienteUseCase createPacienteUseCase;

  public CreatePedidoUseCase(PedidoRepository pedidoRepository,
                             PacienteRepository pacienteRepository,
                             CreatePacienteUseCase createPacienteUseCase) {
    this.pedidoRepository = pedidoRepository;
    this.pacienteRepository = pacienteRepository;
    this.createPacienteUseCase = createPacienteUseCase;
  }

  public PedidoCirurgico execute(CreatePedidoCommand command) {

    PacienteId pacienteId = null;

    if (command.getPaciente() != null) {
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

    // Validar convênio
    if (command.getConvenioValidadeCarteira() != null &&
            command.getConvenioValidadeCarteira().isBefore(java.time.LocalDate.now())) {
      throw new IllegalArgumentException("Convênio está vencido");
    }

    try {
      PedidoId pedidoId = PedidoId.generate();

      // Médico solicitante
      Medico medicoSolicitante = new Medico(
              command.getMedicoSolicitanteNome(),
              command.getMedicoSolicitanteCrm(),
              command.getMedicoSolicitanteEspecialidade()
      );

      // Médico executor
      Medico medicoExecutor = null;
      if (command.getMedicoExecutorNome() != null && !command.getMedicoExecutorNome().trim().isEmpty()) {
        medicoExecutor = new Medico(
                command.getMedicoExecutorNome(),
                command.getMedicoExecutorCrm(),
                command.getMedicoExecutorEspecialidade()
        );
      }

      // PROCEDIMENTO
      if (command.getProcedimentos() == null || command.getProcedimentos().isEmpty()) {
        throw new IllegalArgumentException("Pelo menos um procedimento é obrigatório");
      }
      var todosProcedimentos = command.getProcedimentos();
      Procedimento procedimentoPrincipal = command.getProcedimentos().get(0);

      // Convênio
      Convenio convenio = new Convenio(
              command.getConvenioNome(),
              command.getConvenioNumeroCarteira(),
              command.getConvenioValidadeCarteira(),
              command.getConvenioTipoPlano()
      );

      // CID
      CID cid = null;
      if (command.getCidCodigo() != null && !command.getCidCodigo().trim().isEmpty()) {
        cid = new CID(command.getCidCodigo(), command.getCidDescricao());
      }

      // 🔥 CORREÇÃO AQUI: Usar criar() em vez do construtor
      DataHoraAgendamento agendamento = null;
      if (command.getAgendamentoDataHora() != null) {
        agendamento = DataHoraAgendamento.criar(
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

      // ✅ Cria uma NOVA instância do builder a cada requisição
      PedidoBuilder pedidoBuilder = new PedidoBuilder();

      // Usar o builder com todos os métodos individuais
      PedidoCirurgico pedido = pedidoBuilder
              .comId(pedidoId)
              .comPacienteId(pacienteId)
              .comMedicoSolicitante(medicoSolicitante)
              .comMedicoExecutor(medicoExecutor)
              .comProcedimento(procedimentoPrincipal)
              .comTodosProcedimentos(todosProcedimentos)
              .comConvenio(convenio)
              .comCid(cid)
              .comAgendamento(agendamento)  // ✅ Agora usa o objeto criado corretamente
              .comStatus(status)
              .comPrioridade(prioridade)
              .comLateralidade(lateralidade)
              .comObservacoes(command.getObservacoes())
              .comDocumentosAnexados(command.getDocumentosAnexados())
              .comUsuarioCriacao(command.getUsuarioCriacao())
              .comDataPedido(command.getDataPedido())

              // 🔥 DADOS CLÍNICOS
              .comIndicacaoClinica(command.getIndicacaoClinica())
              .comRelatorioPreOperatorio(command.getRelatorioPreOperatorio())
              .comOrientacoes(command.getOrientacoes())

              // 🔥 CIDs secundários
              .comCidsSecundarios(
                      command.getCidCodigo2(),
                      command.getCidCodigo3(),
                      command.getCidCodigo4()
              )

              // 🔥 Dados da guia
              .comNumeroGuia(command.getNumeroGuia())
              .comRegistroAns(command.getRegistroAns())
              .comNumeroGuiaOperadora(command.getNumeroGuiaOperadora())
              .comCodigoOperadora(command.getCodigoOperadora())
              .comNomeContratado(command.getNomeContratado())

              // 🔥 Dados da internação
              .comCaraterAtendimento(command.getCaraterAtendimento())
              .comTipoInternacao(command.getTipoInternacao())
              .comRegimeInternacao(command.getRegimeInternacao())
              .comQtdDiariasSolicitadas(command.getQtdDiariasSolicitadas())

              // 🔥 Dados de contato do paciente
              .comTelefonePaciente(command.getTelefonePaciente())
              .comEnderecoPaciente(command.getEnderecoPaciente())
              .comCpfPaciente(command.getCpfPaciente())
              .comEmailPaciente(command.getEmailPaciente())
              .comSexoPaciente(command.getSexoPaciente())
              .build();

      System.out.println("=== ANTES DE SALVAR ===");
      System.out.println("Procedimentos no pedido: " +
              (pedido.getTodosProcedimentos() != null ? pedido.getTodosProcedimentos().size() : "null"));
      if (pedido.getTodosProcedimentos() != null) {
        for (Procedimento p : pedido.getTodosProcedimentos()) {
          System.out.println("  - " + p.getCodigoTUSS() + ": " + p.getDescricao());
        }
      }

      return pedidoRepository.salvar(pedido);

    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Dados inválidos fornecidos: " + e.getMessage(), e);
    }
  }
}