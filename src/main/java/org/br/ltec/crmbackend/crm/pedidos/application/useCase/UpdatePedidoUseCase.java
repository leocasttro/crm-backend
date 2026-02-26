package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.port.PacienteRepository;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Endereco;
import org.br.ltec.crmbackend.crm.pedidos.application.command.UpdatePedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Service
@Transactional
public class UpdatePedidoUseCase {

  private final PedidoRepository pedidoRepository;
  private final PacienteRepository pacienteRepository;

  public UpdatePedidoUseCase(PedidoRepository pedidoRepository,
                             PacienteRepository pacienteRepository) {
    this.pedidoRepository = pedidoRepository;
    this.pacienteRepository = pacienteRepository;
  }

  @Transactional
  public PedidoCirurgico execute(UpdatePedidoCommand command) {
    log.info("Iniciando atualização do pedido: {}", command.getPedidoId());

    // Buscar pedido existente
    PedidoId pedidoId = PedidoId.fromString(command.getPedidoId());
    PedidoCirurgico pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com ID: " + command.getPedidoId()));

    String usuario = command.getUsuarioAtualizacao() != null ? command.getUsuarioAtualizacao() : "sistema";

    // Buscar paciente associado ao pedido
    PedidoCirurgico finalPedido = pedido;
    Paciente paciente = pacienteRepository.buscarPorId(pedido.getPacienteId())
            .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com ID: " + finalPedido.getPacienteId()));

    log.info("Paciente encontrado: ID={}, Nome atual={}", paciente.getId(), paciente.getNome());

    boolean pacienteModificado = false;

    // ==================== DADOS DO PACIENTE ====================
    if (command.getNomePaciente() != null) {
      log.info("Alterando nome de '{}' para '{}'", paciente.getNome(), command.getNomePaciente());
      paciente.setNome(command.getNomePaciente());
      pacienteModificado = true;
    }

    if (command.getDataNascimento() != null) {
      log.info("Alterando data nascimento de '{}' para '{}'",
              paciente.getDataNascimento(), command.getDataNascimento());
      paciente.setDataNascimento(command.getDataNascimento());
      pacienteModificado = true;
    }

    if (command.getCpfPaciente() != null) {
      log.info("Alterando CPF de '{}' para '{}'",
              paciente.getDocumento(), command.getCpfPaciente());
      paciente.setDocumento(command.getCpfPaciente());
      pacienteModificado = true;
    }

    if (command.getEmailPaciente() != null) {
      log.info("Alterando email de '{}' para '{}'",
              paciente.getEmail(), command.getEmailPaciente());
      paciente.setEmail(command.getEmailPaciente());
      pacienteModificado = true;
    }

    if (command.getTelefonePaciente() != null && !command.getTelefonePaciente().isEmpty()) {
      log.info("Alterando telefones");
      paciente.setTelefones(command.getTelefonePaciente());
      pacienteModificado = true;
    }

    if (command.getSexoPaciente() != null) {
      log.info("Alterando sexo de '{}' para '{}'",
              paciente.getSexo(), command.getSexoPaciente());
      paciente.setSexo(command.getSexoPaciente());
      pacienteModificado = true;
    }

    if (command.getEnderecoPaciente() != null && command.getEnderecoPaciente().isPresent()) {
      String enderecoCompleto = command.getEnderecoPaciente().get();
      log.info("Alterando endereço para: {}", enderecoCompleto);

      String[] partes = enderecoCompleto.split(",");
      Endereco novoEndereco = new Endereco(
              partes.length > 0 ? partes[0].trim() : "",
              partes.length > 1 ? partes[1].trim() : "",
              partes.length > 2 ? partes[2].trim() : "",
              partes.length > 3 ? partes[3].trim() : "",
              partes.length > 4 ? partes[4].trim() : "",
              partes.length > 5 ? partes[5].trim() : "",
              ""
      );
      paciente.setEndereco(novoEndereco);
      pacienteModificado = true;
    }

    // Salvar paciente se modificado
    if (pacienteModificado) {
      log.info("Salvando paciente modificado...");
      paciente = pacienteRepository.salvar(paciente);
      pacienteRepository.flush();
    }

    boolean pedidoModificado = false;

    // ==================== MÉDICO ====================
    if (command.getMedicoSolicitanteNome() != null) {
      log.info("Alterando médico solicitante para: {}", command.getMedicoSolicitanteNome());

      String crm = command.getMedicoSolicitanteCrm() != null
              ? command.getMedicoSolicitanteCrm()
              : (pedido.getMedicoSolicitante() != null ? pedido.getMedicoSolicitante().getCrm() : null);

      String especialidade = command.getMedicoSolicitanteEspecialidade() != null
              ? command.getMedicoSolicitanteEspecialidade()
              : (pedido.getMedicoSolicitante() != null ? pedido.getMedicoSolicitante().getEspecialidade() : null);

      Medico medicoSolicitante = new Medico(
              command.getMedicoSolicitanteNome(),
              crm,
              especialidade
      );
      pedido.setMedicoSolicitante(medicoSolicitante);
      pedidoModificado = true;
    } else if (command.getMedicoSolicitanteCrm() != null || command.getMedicoSolicitanteEspecialidade() != null) {
      if (pedido.getMedicoSolicitante() != null) {
        String nome = pedido.getMedicoSolicitante().getNome();
        String crm = command.getMedicoSolicitanteCrm() != null
                ? command.getMedicoSolicitanteCrm()
                : pedido.getMedicoSolicitante().getCrm();
        String especialidade = command.getMedicoSolicitanteEspecialidade() != null
                ? command.getMedicoSolicitanteEspecialidade()
                : pedido.getMedicoSolicitante().getEspecialidade();

        Medico medicoSolicitante = new Medico(nome, crm, especialidade);
        pedido.setMedicoSolicitante(medicoSolicitante);
        pedidoModificado = true;
      }
    }

    // ==================== PROCEDIMENTO ====================
    if (command.getProcedimentoDescricao() != null ||
            command.getProcedimentoCodigoTUSS() != null ||
            command.getProcedimentoCategoria() != null) {

      log.info("Alterando procedimento");

      String codigoTUSS = command.getProcedimentoCodigoTUSS() != null
              ? command.getProcedimentoCodigoTUSS()
              : (pedido.getProcedimento() != null ? pedido.getProcedimento().getCodigoTUSS() : null);

      String descricao = command.getProcedimentoDescricao() != null
              ? command.getProcedimentoDescricao()
              : (pedido.getProcedimento() != null ? pedido.getProcedimento().getDescricao() : null);

      String categoria = command.getProcedimentoCategoria() != null
              ? command.getProcedimentoCategoria()
              : (pedido.getProcedimento() != null ? pedido.getProcedimento().getCategoria() : null);

      if (descricao != null) {
        Procedimento procedimento = new Procedimento(codigoTUSS, descricao, categoria);
        pedido.atualizarProcedimento(procedimento, usuario);
        pedidoModificado = true;
      }
    }

    // ==================== CONVÊNIO ====================
    if (command.getConvenioNome() != null ||
            command.getConvenioNumeroCarteira() != null ||
            command.getConvenioValidadeCarteira() != null ||
            command.getConvenioTipoPlano() != null) {

      log.info("Alterando convênio");

      String nome = command.getConvenioNome() != null
              ? command.getConvenioNome()
              : (pedido.getConvenio() != null ? pedido.getConvenio().getNome() : null);

      String numeroCarteira = command.getConvenioNumeroCarteira() != null
              ? command.getConvenioNumeroCarteira()
              : (pedido.getConvenio() != null ? pedido.getConvenio().getNumeroCarteira() : null);

      LocalDate validade = command.getConvenioValidadeCarteira() != null
              ? command.getConvenioValidadeCarteira()
              : (pedido.getConvenio() != null ? pedido.getConvenio().getValidade() : null);

      String tipoPlano = command.getConvenioTipoPlano() != null
              ? command.getConvenioTipoPlano()
              : (pedido.getConvenio() != null ? pedido.getConvenio().getTipoPlano() : null);

      if (nome != null) {
        Convenio convenio = new Convenio(nome, numeroCarteira, validade, tipoPlano);
        pedido.atualizarConvenio(convenio, usuario);
        pedidoModificado = true;
      }
    }

    // ==================== CID ====================
    if (command.getCidCodigo() != null || command.getCidDescricao() != null) {
      log.info("Alterando CID para: {}", command.getCidCodigo());

      String codigo = command.getCidCodigo() != null
              ? command.getCidCodigo()
              : (pedido.getCid() != null ? pedido.getCid().getCodigo() : null);

      String descricao = command.getCidDescricao() != null
              ? command.getCidDescricao()
              : (pedido.getCid() != null ? pedido.getCid().getDescricao() : null);

      if (codigo != null) {
        CID cid = new CID(codigo, descricao);
        pedido.atualizarCID(cid, usuario);
        pedidoModificado = true;
      }
    }

    // ==================== PRIORIDADE ====================
    if (command.getPrioridade() != null) {
      log.info("Alterando prioridade para: {}", command.getPrioridade());
      pedido.atualizarPrioridade(command.getPrioridade(), usuario);
      pedidoModificado = true;
    }

    // ==================== DATA DO PEDIDO ====================
    if (command.getDataPedido() != null) {
      log.info("Alterando data do pedido para: {}", command.getDataPedido());
      pedido.setDataPedido(command.getDataPedido());
      pedidoModificado = true;
    }

    // ==================== OBSERVAÇÕES ====================
    if (command.getObservacoes() != null && !command.getObservacoes().isEmpty()) {
      log.info("Adicionando {} observações", command.getObservacoes().size());
      PedidoCirurgico finalPedido1 = pedido;
      command.getObservacoes().forEach(obs ->
              finalPedido1.adicionarObservacao(obs, usuario));
      pedidoModificado = true;
    }

    // 🔥 SALVAR PEDIDO SE MODIFICADO
    if (pedidoModificado) {
      log.info("Salvando pedido modificado...");
      pedido = pedidoRepository.salvar(pedido);
      pedidoRepository.flush();
    }

    log.info("Finalizando transação - mudanças persistidas");

    return pedido;
  }
}