package org.br.ltec.crmbackend.crm.pedidos.infra.pdf;

import java.util.regex.Pattern;

public class PedidoPdfPatterns {

  private PedidoPdfPatterns() {}

  public static final Pattern NOME_PACIENTE = Pattern.compile("Paciente:\\s*(.+?)\\s+Idade:");

  public static final Pattern IDADE = Pattern.compile("Idade:\\s*(\\d+)");

  public static final Pattern DATA_PEDIDO = Pattern.compile("Data:\\s*(\\d{2}/\\d{4})");

  public static final Pattern HORA_PEDIDO = Pattern.compile("Hora:\\s*(\\d{2}:\\d{2}:\\d{2})");

  public static final Pattern CONVENIO = Pattern.compile("Convênio:\\s*(.+?)(?:\\s+Número:|$)");

  public static final Pattern HOSPITAL = Pattern.compile("Hospital:\\s*(.+?)\\s+UTI:");

  public static final Pattern CID = Pattern.compile("CID:\\s*([A-Z0-9.]+)");

  public static final Pattern CID_ALTERNATIVO = Pattern.compile("CID\\s*10\\s*Principal\\s*([A-Z0-9.]+)");

  public static final Pattern MEDICO_E_CRM = Pattern.compile("Cirurgiao:\\s*(.+?)\\s*-\\s*\\(CRM-([A-Z]{2})\\s*(\\d+)\\)");

  public static final Pattern PROCEDIMENTO_LINHA = Pattern.compile("^\\s*\\d+\\s+(\\d{6,})\\s+(.+)$", Pattern.MULTILINE);
}
