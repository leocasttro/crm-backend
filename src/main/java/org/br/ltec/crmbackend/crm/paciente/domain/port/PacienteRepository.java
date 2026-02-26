package org.br.ltec.crmbackend.crm.paciente.domain.port;

import org.br.ltec.crmbackend.crm.paciente.domain.model.Paciente;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.PacienteId;

import java.util.List;
import java.util.Optional;

/**
 * Port: Interface de repositório definida no domínio.
 * O domínio define o que precisa, a infraestrutura implementa como fazer.
 */
public interface PacienteRepository {

  void flush();
  void clear();
  /**
   * Salva ou atualiza um paciente
   * @return paciente salvo com possíveis alterações (ID gerado, etc.)
   */
  Paciente salvar(Paciente paciente);

  /**
   * Busca paciente por ID
   */
  Optional<Paciente> buscarPorId(PacienteId id);

  /**
   * Busca paciente por documento (CPF, CNPJ, etc.)
   */
  Optional<Paciente> buscarPorDocumento(String documento);

  /**
   * Busca paciente por email
   */
  Optional<Paciente> buscarPorEmail(String email);

  /**
   * Verifica se existe paciente com o documento informado
   * (excluindo o paciente com o ID especificado, útil para updates)
   */
  boolean existePorDocumento(String documento, PacienteId excluirId);

  /**
   * Verifica se existe paciente com o email informado
   * (excluindo o paciente com o ID especificado)
   */
  boolean existePorEmail(String email, PacienteId excluirId);

  /**
   * Lista todos os pacientes com paginação
   */
  List<Paciente> listar(int pagina, int tamanhoPagina);

  /**
   * Busca pacientes por nome (busca parcial, case insensitive)
   */
  List<Paciente> buscarPorNome(String nome, int pagina, int tamanhoPagina);

  /**
   * Exclui um paciente
   */
  void excluir(PacienteId id);

  /**
   * Conta o total de pacientes
   */
  long contar();

  /**
   * Conta pacientes com o nome especificado
   */
  long contarPorNome(String nome);
}