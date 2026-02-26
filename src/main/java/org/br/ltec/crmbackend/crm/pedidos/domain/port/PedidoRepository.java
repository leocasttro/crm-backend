package org.br.ltec.crmbackend.crm.pedidos.domain.port;

import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.StatusPedido;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Port: Interface de repositório para PedidoCirurgico definida no domínio.
 */
public interface PedidoRepository {

  void clear();

  /**
   * Salva ou atualiza um pedido
   * @return pedido salvo com possíveis alterações
   */
  PedidoCirurgico salvar(PedidoCirurgico pedido);

  /**
   * Busca pedido por ID
   */
  Optional<PedidoCirurgico> buscarPorId(PedidoId id);

  /**
   * Busca pedidos por ID do paciente
   */
  List<PedidoCirurgico> buscarPorPacienteId(PacienteId pacienteId);

  /**
   * Busca pedidos por status
   */
  List<PedidoCirurgico> buscarPorStatus(StatusPedido.Tipo status, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos por CRM do médico solicitante
   */
  List<PedidoCirurgico> buscarPorMedicoSolicitante(String crm, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos por CRM do médico executor
   */
  List<PedidoCirurgico> buscarPorMedicoExecutor(String crm, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos por código TUSS do procedimento
   */
  List<PedidoCirurgico> buscarPorProcedimento(String codigoTUSS, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos por nome do convênio
   */
  List<PedidoCirurgico> buscarPorConvenio(String nomeConvenio, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos por CID
   */
  List<PedidoCirurgico> buscarPorCID(String codigoCID, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos por data de criação
   */
  List<PedidoCirurgico> buscarPorDataCriacao(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos por data do pedido
   */
  List<PedidoCirurgico> buscarPorDataPedido(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos por data de agendamento
   */
  List<PedidoCirurgico> buscarPorDataAgendamento(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos por prioridade
   */
  List<PedidoCirurgico> buscarPorPrioridade(String prioridade, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos com agendamento
   */
  List<PedidoCirurgico> buscarComAgendamento(int pagina, int tamanhoPagina);

  /**
   * Busca pedidos sem agendamento
   */
  List<PedidoCirurgico> buscarSemAgendamento(int pagina, int tamanhoPagina);

  /**
   * Busca pedidos por lateralidade
   */
  List<PedidoCirurgico> buscarPorLateralidade(String lateralidade, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos criados por um usuário específico
   */
  List<PedidoCirurgico> buscarPorUsuarioCriacao(String usuario, int pagina, int tamanhoPagina);

  /**
   * Busca pedidos que precisam de confirmação de jejum
   * (pedidos agendados ou confirmados)
   */
  List<PedidoCirurgico> buscarParaConfirmacaoJejum(int pagina, int tamanhoPagina);

  /**
   * Busca pedidos em andamento (não finalizados)
   */
  List<PedidoCirurgico> buscarEmAndamento(int pagina, int tamanhoPagina);

  /**
   * Busca todos os pedidos com paginação
   */
  List<PedidoCirurgico> buscarTodos(int pagina, int tamanhoPagina);

  /**
   * Exclui um pedido
   */
  void excluir(PedidoId id);

  /**
   * Verifica se existe pedido com o ID
   */
  boolean existePorId(PedidoId id);

  /**
   * Verifica se existe pedido ativo para o paciente
   */
  boolean existePedidoAtivoParaPaciente(PacienteId pacienteId);

  /**
   * Conta total de pedidos
   */
  long contarTotal();

  /**
   * Conta pedidos por status
   */
  long contarPorStatus(StatusPedido.Tipo status);

  /**
   * Conta pedidos por paciente
   */
  long contarPorPacienteId(PacienteId pacienteId);

  void flush();
}