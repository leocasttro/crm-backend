package org.br.ltec.crmbackend.crm.pedidos.domain.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdatePedidoData {
  private String indicacaoClinica;
  private String relatorioPreOperatorio;
  private String orientacoes;
  private String telefonePaciente;
  private String enderecoPaciente;
  private String cidCodigo2;
  private String cidCodigo3;
  private String cidCodigo4;
  private String numeroGuia;
  private String registroAns;
  private String numeroGuiaOperadora;
  private String codigoOperadora;
  private String nomeContratado;
  private String caraterAtendimento;
  private String tipoInternacao;
  private String regimeInternacao;
  private String qtdDiariasSolicitadas;
  private String cpfPaciente;
  private String emailPaciente;
  private String sexoPaciente;
}