package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import lombok.Data;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.*;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdatePedidoRequest {

  // Dados do Paciente
  private NomeCompleto nomePaciente;
  private DataNascimento dataNascimento;
  private Documento cpfPaciente;
  private Email emailPaciente;
  private List<Telefone> telefonesPaciente;
  private Sexo sexoPaciente;
  private String enderecoPaciente;

  // Médico
  private String medicoSolicitanteNome;
  private String medicoSolicitanteCrm;
  private String medicoSolicitanteEspecialidade;

  // Procedimento
  private String procedimentoCodigoTUSS;
  private String procedimentoDescricao;
  private String procedimentoCategoria;

  // Convênio
  private String convenioNome;
  private String convenioNumeroCarteira;
  private LocalDate convenioValidadeCarteira;
  private String convenioTipoPlano;

  // CID
  private String cidCodigo;
  private String cidDescricao;

  // Prioridade
  private String prioridade;

  // Data do pedido
  private LocalDate dataPedido;

  // Observações
  private List<String> observacoes;

  public List<Telefone> getTelefonesPaciente() { return telefonesPaciente; }
  public void setTelefonesPaciente(List<Telefone> telefonesPaciente) {
    this.telefonesPaciente = telefonesPaciente;
  }
}