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

  // ==================== PROCEDIMENTOS (OPME) ====================
  public static final Pattern PROCEDIMENTO_OPME = Pattern.compile("^(\\d+)\\s+(\\d{8,})\\s+(.+?)(?:\\s+Mesma via de Acesso)?$", Pattern.MULTILINE);
  public static final Pattern PROCEDIMENTO_GUIA = Pattern.compile("\\d{2}\\s*-\\s*\\d{2}\\s+(\\d{8,})\\s+(.+?)\\s+(\\d+)", Pattern.MULTILINE);

  // ==================== DADOS DA GUIA ====================
  public static final Pattern NUMERO_GUIA = Pattern.compile("No Guia no Prestador?:(\\d+)");
  public static final Pattern REGISTRO_ANS = Pattern.compile("1 - Registro ANS\\s*(\\d+)");
  public static final Pattern NUMERO_GUIA_OPERADORA = Pattern.compile("3 - Número da Guia Atribuído pela Operadora\\s*(\\d+)");

  // ==================== DADOS DO BENEFICIÁRIO ====================
  public static final Pattern NUMERO_CARTEIRA = Pattern.compile("7 - Número da Carteira\\s*(\\d+)");
  public static final Pattern VALIDADE_CARTEIRA = Pattern.compile("8 - Validade da Carteira\\s*(\\d{2}/\\d{2}/\\d{4})");
  public static final Pattern CARTAO_NACIONAL_SAUDE = Pattern.compile("11 - Cartão Nacional de Saúde\\s*(\\d+)");

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

  // ==================== INDICAÇÃO CLÍNICA (multilinha) ====================
  public static final Pattern INDICACAO_CLINICA = Pattern.compile(
          "28 - Indicação Clínica\\s*([^\\n]+(?:\\n[^\\n]+)*?)(?=\\n\\s*\\d+\\s+-|\\n\\s*29 -)",
          Pattern.MULTILINE);

  // ==================== DATA DA SOLICITAÇÃO ====================
  public static final Pattern DATA_SOLICITACAO = Pattern.compile("46 - Data da Solicitação\\s*(\\d{2}/\\d{2}/\\d{4})");

  // ==================== TELEFONE ====================
  public static final Pattern TELEFONE = Pattern.compile("Tel:?\\.?\\s*\\(?(\\d{2})\\)?\\s*(\\d{4,5})-?(\\d{4})");

  // ==================== ENDEREÇO ====================
  public static final Pattern ENDERECO = Pattern.compile("([^\\n]+?)\\s+CEP:\\s*(\\d{5}-?\\d{3})");

  // ==================== ALERGIAS ====================
  public static final Pattern ALERGIAS = Pattern.compile("Alergias?:\\s*([^\\s]+)");

  // ==================== LATERALIDADE ====================
  public static final Pattern LATERALIDADE = Pattern.compile("Lateralidade:\\s*([^\\n]+)");

  // ==================== TIPO (Eletiva/Urgência) ====================
  public static final Pattern TIPO = Pattern.compile("Tipo:\\s*(\\w+)");
}