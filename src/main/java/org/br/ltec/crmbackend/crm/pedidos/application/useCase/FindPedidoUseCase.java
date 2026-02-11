package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoRepository;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FindPedidoUseCase {

  private final PedidoRepository pedidoRepository;

  public FindPedidoUseCase(PedidoRepository pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  // Busca por ID
  public Optional<PedidoCirurgico> findById(String id) {
    PedidoId pedidoId = PedidoId.fromString(id);
    return pedidoRepository.buscarPorId(pedidoId);
  }

  // Busca por paciente
  public List<PedidoCirurgico> findByPacienteId(String pacienteId) {
    PacienteId id = PacienteId.fromString(pacienteId);
    return pedidoRepository.buscarPorPacienteId(id);
  }

  // Busca por status
  public List<PedidoCirurgico> findByStatus(StatusPedido.Tipo status, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarPorStatus(status, pagina, tamanhoPagina);
  }

  // Busca por médico solicitante
  public List<PedidoCirurgico> findByMedicoSolicitante(String crm, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarPorMedicoSolicitante(crm, pagina, tamanhoPagina);
  }

  // Busca por médico executor
  public List<PedidoCirurgico> findByMedicoExecutor(String crm, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarPorMedicoExecutor(crm, pagina, tamanhoPagina);
  }

  // Busca por procedimento
  public List<PedidoCirurgico> findByProcedimento(String codigoTUSS, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarPorProcedimento(codigoTUSS, pagina, tamanhoPagina);
  }

  // Busca por convênio
  public List<PedidoCirurgico> findByConvenio(String nomeConvenio, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarPorConvenio(nomeConvenio, pagina, tamanhoPagina);
  }

  // Busca por CID
  public List<PedidoCirurgico> findByCID(String codigoCID, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarPorCID(codigoCID, pagina, tamanhoPagina);
  }

  // Busca por data de criação
  public List<PedidoCirurgico> findByDataCriacao(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    validarDatas(dataInicio, dataFim);
    return pedidoRepository.buscarPorDataCriacao(dataInicio, dataFim, pagina, tamanhoPagina);
  }

  // Busca por data do pedido
  public List<PedidoCirurgico> findByDataPedido(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    validarDatas(dataInicio, dataFim);
    return pedidoRepository.buscarPorDataPedido(dataInicio, dataFim, pagina, tamanhoPagina);
  }

  // Busca por data de agendamento
  public List<PedidoCirurgico> findByDataAgendamento(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    validarDatas(dataInicio, dataFim);
    return pedidoRepository.buscarPorDataAgendamento(dataInicio, dataFim, pagina, tamanhoPagina);
  }

  // Busca por prioridade
  public List<PedidoCirurgico> findByPrioridade(String prioridade, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarPorPrioridade(prioridade, pagina, tamanhoPagina);
  }

  // Busca com agendamento
  public List<PedidoCirurgico> findComAgendamento(int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarComAgendamento(pagina, tamanhoPagina);
  }

  // Busca sem agendamento
  public List<PedidoCirurgico> findSemAgendamento(int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarSemAgendamento(pagina, tamanhoPagina);
  }

  // Busca por lateralidade
  public List<PedidoCirurgico> findByLateralidade(String lateralidade, int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarPorLateralidade(lateralidade, pagina, tamanhoPagina);
  }

  // Busca para confirmação de jejum
  public List<PedidoCirurgico> findParaConfirmacaoJejum(int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarParaConfirmacaoJejum(pagina, tamanhoPagina);
  }

  // Busca em andamento
  public List<PedidoCirurgico> findEmAndamento(int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarEmAndamento(pagina, tamanhoPagina);
  }

  // Busca todos
  public List<PedidoCirurgico> findAll(int pagina, int tamanhoPagina) {
    validarPaginacao(pagina, tamanhoPagina);
    return pedidoRepository.buscarTodos(pagina, tamanhoPagina);
  }

  // Métodos de contagem
  public long countAll() {
    return pedidoRepository.contarTotal();
  }

  public long countByStatus(StatusPedido.Tipo status) {
    return pedidoRepository.contarPorStatus(status);
  }

  public long countByPacienteId(String pacienteId) {
    PacienteId id = PacienteId.fromString(pacienteId);
    return pedidoRepository.contarPorPacienteId(id);
  }

  // Métodos de verificação
  public boolean existsById(String id) {
    PedidoId pedidoId = PedidoId.fromString(id);
    return pedidoRepository.existePorId(pedidoId);
  }

  public boolean existsActiveOrderForPatient(String pacienteId) {
    PacienteId id = PacienteId.fromString(pacienteId);
    return pedidoRepository.existePedidoAtivoParaPaciente(id);
  }

  // Validações
  private void validarPaginacao(int pagina, int tamanhoPagina) {
    if (pagina < 0) {
      throw new IllegalArgumentException("Página não pode ser negativa");
    }
    if (tamanhoPagina <= 0 || tamanhoPagina > 100) {
      throw new IllegalArgumentException("Tamanho da página deve estar entre 1 e 100");
    }
  }

  private void validarDatas(LocalDate dataInicio, LocalDate dataFim) {
    if (dataInicio == null || dataFim == null) {
      throw new IllegalArgumentException("Data início e data fim são obrigatórias");
    }
    if (dataInicio.isAfter(dataFim)) {
      throw new IllegalArgumentException("Data início não pode ser após data fim");
    }
  }
}