package org.br.ltec.crmbackend.crm.pedidos.infra.pdf;

import java.util.regex.Pattern;

public class PedidoPdfPatterns {

  private PedidoPdfPatterns() {}

  // ==================== DADOS BÁSICOS ====================
  public static final Pattern NOME_PACIENTE = Pattern.compile("Paciente:\\s*(.+?)\\s+Idade:");
  public static final Pattern IDADE = Pattern.compile("Idade:\\s*(\\d+)");
  public static final Pattern DATA_PEDIDO = Pattern.compile("Data:\\s*(\\d{2}/\\d{2}/\\d{4})");
  public static final Pattern HORA_PEDIDO = Pattern.compile("Hora:\\s*(\\d{2}:\\d{2}:\\d{2})");
  public static final Pattern CONVENIO = Pattern.compile("Convênio:\\s*(.+?)(?:\\s+Número:|$)");
  public static final Pattern HOSPITAL = Pattern.compile("Hospital:\\s*(.+?)\\s+UTI:");
  public static final Pattern CID_PRINCIPAL = Pattern.compile("CID:\\s*([A-Z]\\d+)(?=[A-Z]|\\s|$)");

  // ==================== DATA DE NASCIMENTO ====================
  public static final Pattern DATA_NASCIMENTO = Pattern.compile("Nasc\\.?:\\s*(\\d{2}/\\d{2}/\\d{4})");

  // ==================== MÉDICO E CRM ====================
  public static final Pattern MEDICO_E_CRM = Pattern.compile("Cirurgiao?:\\s*(.+?)\\s*-\\s*\\(CRM-([A-Z]{2})\\s*(\\d+)\\)");
  public static final Pattern MEDICO_DADOS_COMPLETOS = Pattern.compile("14 - Nome do Profissional Solicitante\\s*(.+?)\\s+15 - Conselho Profissional\\s*(\\w+)\\s+16 - Número no Conselho\\s*(\\d+)\\s+17 - UF\\s*(\\w{2})\\s+18 - Código CBO\\s*(\\d+)");

  // ==================== PROCEDIMENTOS ====================
  public static final Pattern PROCEDIMENTO_OPME = Pattern.compile("^(\\d+)\\s+(\\d{8,})\\s+(.+?)(?:\\s+Mesma via de Acesso)?$", Pattern.MULTILINE);
  public static final Pattern PROCEDIMENTO_GUIA = Pattern.compile("\\d{2}\\s*-\\s*\\d{2}\\s+(\\d{8,})\\s+(.+?)\\s+(\\d+)", Pattern.MULTILINE);

  public static final Pattern OPME_BLOCO = Pattern.compile(
          "OPME\\s*\\n(.*?)(?=\\s*Qtd\\s+Codigo|\\s*\\d+\\s+\\d{6,})",
          Pattern.DOTALL | Pattern.CASE_INSENSITIVE
  );

  public static final Pattern OPME_MARCAS_ACEITAS = Pattern.compile("\\(([^)]+)\\)");

  public static final Pattern OPME_NAO_COTAR = Pattern.compile(
          "Não cotar com:\\s*(.+)", Pattern.CASE_INSENSITIVE
  );
  // ==================== DADOS DA GUIA ====================
  public static final Pattern NUMERO_GUIA = Pattern.compile("No Guia no Prestador?:(\\d+)");
  public static final Pattern REGISTRO_ANS = Pattern.compile("1 - Registro ANS\\s*(\\d+)");
  public static final Pattern NUMERO_GUIA_OPERADORA = Pattern.compile("3 - Número da Guia Atribuído pela Operadora\\s*(\\d+)");

  // ==================== DADOS DO BENEFICIÁRIO ====================
  public static final Pattern NUMERO_CARTEIRA = Pattern.compile(
          "7\\s*-\\s*Número\\s+da\\s+Carteira.*?\\n\\s*(\\d+)",
          Pattern.MULTILINE | Pattern.DOTALL
  );
  // Em PedidoPdfPatterns.java
  public static final Pattern VALIDADE_CARTEIRA = Pattern.compile(
          "8\\s*-\\s*Validade\\s+da\\s+Carteira.*?\\n\\s*\\d+\\s+(\\d{2}/\\d{2}/\\d{4})",
          Pattern.MULTILINE | Pattern.DOTALL
  );
  public static final Pattern CARTAO_NACIONAL_SAUDE = Pattern.compile("11 - Cartão Nacional de Saúde\\s*(\\d+)");

  // 🔥 NOVO: Telefone
  public static final Pattern TELEFONE = Pattern.compile(
          "Telefone:\\s*\\((\\d{2})\\)(\\d\\.?\\d{4})-?(\\d{4})",
          Pattern.MULTILINE
  );

  // 🔥 NOVO: Endereço
  public static final Pattern ENDERECO = Pattern.compile("([^\\n]+?)\\s+CEP:\\s*(\\d{5}-?\\d{3})");

  // ==================== DADOS DO CONTRATADO ====================
  public static final Pattern CODIGO_OPERADORA = Pattern.compile("12 - Código na Operadora\\s*(\\d+)");
  public static final Pattern NOME_CONTRATADO = Pattern.compile("13 - Nome do Contratado\\s*([^\\n]+)");
  public static final Pattern CBO = Pattern.compile("18 - Código CBO\\s*(\\d+)");

  // ==================== DADOS DA INTERNAÇÃO ====================
  public static final Pattern CARATER_ATENDIMENTO = Pattern.compile("22-Caráter do Atendimento\\s*(\\w+)");
  public static final Pattern TIPO_INTERNACAO = Pattern.compile("23-Tp\\. de Internação\\s*(\\d)");
  public static final Pattern REGIME_INTERNACAO = Pattern.compile("24-Regime de Internação\\s*(\\d)");
  public static final Pattern QTD_DIARIAS = Pattern.compile("25-Qtde\\. Diárias Solicitadas\\s*(\\d)");
  public static final Pattern PREVISAO_OPME = Pattern.compile("26-Previsão de uso de OPME\\s*(\\d)");

  // ==================== CIDs ====================
  public static final Pattern CID_10_PRINCIPAL = Pattern.compile("29 - CID 10 Principal\\s*([A-Z]\\d+)");
  public static final Pattern CID_10_SECUNDARIO_2 = Pattern.compile("30 - CID 10 \\(2\\)\\s*([A-Z]\\d+)?");
  public static final Pattern CID_10_SECUNDARIO_3 = Pattern.compile("31 - CID 10 \\(3\\)\\s*([A-Z]\\d+)?");
  public static final Pattern CID_10_SECUNDARIO_4 = Pattern.compile("32 - CID 10 \\(4\\)\\s*([A-Z]\\d+)?");

  // 🔥 NOVO: RELATÓRIO PRÉ-OPERATÓRIO (texto completo entre RELATÓRIO PRÉ-OPERATÓRIO e OPME)
  public static final Pattern RELATORIO_PRE_OPERATORIO = Pattern.compile(
          "RELATÓRIO PRÉ-OPERATÓRIO\\s*([\\s\\S]*?)(?=\\n\\s*OPME|\\n\\s*Cirurgiao:|\\n\\s*\\d+\\s+\\d{8,}|$)",
          Pattern.MULTILINE);

  // ==================== INDICAÇÃO CLÍNICA (multilinha) ====================
  public static final Pattern INDICACAO_CLINICA = Pattern.compile(
          "28 - Indicação Clínica\\s*([^\\n]+(?:\\n[^\\n]+)*?)(?=\\n\\s*\\d+\\s+-|\\n\\s*29 -)",
          Pattern.MULTILINE);

  // ==================== DATA DA SOLICITAÇÃO ====================
  public static final Pattern DATA_SOLICITACAO = Pattern.compile("46 - Data da Solicitação\\s*(\\d{2}/\\d{2}/\\d{4})");

  // ==================== ALERGIAS ====================
  public static final Pattern ALERGIAS = Pattern.compile("Alergias?:\\s*([^\\s]+)");

  // ==================== LATERALIDADE ====================
  public static final Pattern LATERALIDADE = Pattern.compile("Lateralidade:\\s*([^\\n]+)");

  // ==================== TIPO (Eletiva/Urgência) ====================
  public static final Pattern TIPO = Pattern.compile("Tipo:\\s*(\\w+)");

  // ==================== ORIENTAÇÕES (final do documento) ====================
  public static final Pattern ORIENTACOES = Pattern.compile(
          "ORIENTAÇÕES PRÉ-OPERATÓRIAS\\s*([\\s\\S]*)",
          Pattern.MULTILINE);
}