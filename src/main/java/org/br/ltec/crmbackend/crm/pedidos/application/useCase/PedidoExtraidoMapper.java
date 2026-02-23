package org.br.ltec.crmbackend.crm.pedidos.application.useCase;

import org.br.ltec.crmbackend.crm.paciente.application.command.CreatePacienteCommand;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Documento;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Sexo;
import org.br.ltec.crmbackend.crm.paciente.domain.valueObject.Telefone;
import org.br.ltec.crmbackend.crm.pedidos.application.command.CreatePedidoCommand;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Lateralidade;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Prioridade;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PedidoExtraidoMapper {

  private static final DateTimeFormatter PDF_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public CreatePedidoCommand toCreatePedidoCommand(PedidoExtraido extraido) {

    CreatePedidoCommand cmd = new CreatePedidoCommand();

    // 📌 DADOS DO PACIENTE - extraídos diretamente do PedidoExtraido
    CreatePacienteCommand pacienteCmd = criarPacienteCommand(extraido);
    cmd.setPaciente(pacienteCmd);

    // 📌 CONVÊNIO
    cmd.setConvenioNome(extraido.getConvenio());
    // Número da carteira - pode ser extraído do textoNormalizado se necessário
    cmd.setConvenioNumeroCarteira(extrairNumeroCarteira(extraido.getTextoNormalizado()));

    // Validade da carteira - pode ser extraída se houver no texto
    LocalDate validade = extrairValidadeCarteira(extraido.getTextoNormalizado());
    if (validade != null) {
      cmd.setConvenioValidadeCarteira(validade);
    }

    // 📌 CID
    cmd.setCidCodigo(extraido.getCid());

    // 📌 MÉDICO
    cmd.setMedicoSolicitanteNome(extraido.getMedicoNome());
    if (extraido.getCrmNumero() != null) {
      String crm = extraido.getCrmNumero();
      if (extraido.getCrmUf() != null) {
        crm = crm + "/" + extraido.getCrmUf();
      }
      cmd.setMedicoSolicitanteCrm(crm);
    }

    // 📌 PROCEDIMENTOS
    if (extraido.getProcedimentos() != null && !extraido.getProcedimentos().isEmpty()) {
      var primeiro = extraido.getProcedimentos().get(0);
      cmd.setProcedimentoCodigoTUSS(primeiro.getCodigo());
      cmd.setProcedimentoDescricao(primeiro.getDescricao());
    }

    // 📌 DATA DO PEDIDO
    if (extraido.getDataPedido() != null && !extraido.getDataPedido().isBlank()) {
      cmd.setDataPedido(LocalDate.parse(extraido.getDataPedido(), PDF_DATE));
    }

    // 📌 PRIORIDADE (inferir do contexto ou usar padrão)
    cmd.setPrioridade(new Prioridade(Prioridade.Tipo.ELETIVA)); // padrão

    // 📌 LATERALIDADE (extrair do texto se possível)
    Lateralidade lateralidade = extrairLateralidade(extraido.getTextoNormalizado());
    cmd.setLateralidade(lateralidade != null ? lateralidade :
            new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL));

    // 📌 OBSERVAÇÕES (usar texto normalizado como observação)
    if (extraido.getTextoNormalizado() != null && !extraido.getTextoNormalizado().isEmpty()) {
      // Pega as primeiras 500 caracteres como observação
      String obs = extraido.getTextoNormalizado();
      if (obs.length() > 500) {
        obs = obs.substring(0, 500) + "...";
      }
      cmd.setObservacoes(List.of(obs));
    }

    return cmd;
  }

  private CreatePacienteCommand criarPacienteCommand(PedidoExtraido extraido) {
    CreatePacienteCommand pacienteCmd = new CreatePacienteCommand();

    // 🔹 Nome completo -> separar em primeiroNome e sobrenome
    String nomeCompleto = extraido.getNomePaciente();
    if (nomeCompleto != null && !nomeCompleto.isEmpty()) {
      String[] partes = nomeCompleto.split(" ", 2);
      pacienteCmd.setPrimeiroNome(partes[0]);
      if (partes.length > 1) {
        pacienteCmd.setSobrenome(partes[1]);
      } else {
        pacienteCmd.setSobrenome("");
      }
    }

    // 🔹 Data de nascimento (extrair do textoNormalizado)
    String dataNascimentoStr = extrairDataNascimento(extraido.getTextoNormalizado());
    if (dataNascimentoStr != null) {
      try {
        pacienteCmd.setDataNascimento(LocalDate.parse(dataNascimentoStr, PDF_DATE));
      } catch (Exception e) {
        // Ignora data inválida
      }
    }

    // 🔹 CPF (extrair do textoNormalizado)
    String cpf = extrairCpf(extraido.getTextoNormalizado());
    if (cpf != null) {
      pacienteCmd.setDocumentoNumero(cpf.replaceAll("[^0-9]", ""));
      pacienteCmd.setDocumentoTipo(Documento.TipoDocumento.CPF);
    }

    // 🔹 Telefone (extrair do textoNormalizado)
    String telefone = extrairTelefone(extraido.getTextoNormalizado());
    if (telefone != null) {
      List<CreatePacienteCommand.TelefoneRequest> telefones = new ArrayList<>();
      CreatePacienteCommand.TelefoneRequest telRequest =
              new CreatePacienteCommand.TelefoneRequest(
                      telefone,
                      Telefone.TipoTelefone.CELULAR,
                      true
              );
      telefones.add(telRequest);
      pacienteCmd.setTelefones(telefones);
    }

    // 🔹 Email (não vem no PDF)
    // pacienteCmd.setEmail(null);

    return pacienteCmd;
  }

  private String extrairDataNascimento(String texto) {
    if (texto == null) return null;
    Pattern pattern = Pattern.compile("Nasc\\.?:?\\s*(\\d{2}/\\d{2}/\\d{4})");
    Matcher matcher = pattern.matcher(texto);
    return matcher.find() ? matcher.group(1) : null;
  }

  private String extrairCpf(String texto) {
    if (texto == null) return null;
    Pattern pattern = Pattern.compile("\\b(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})\\b");
    Matcher matcher = pattern.matcher(texto);
    return matcher.find() ? matcher.group(1) : null;
  }

  private String extrairTelefone(String texto) {
    if (texto == null) return null;
    Pattern pattern = Pattern.compile("Tel:?\\.?\\s*\\(?(\\d{2})\\)?\\s*(\\d{4,5})-?(\\d{4})");
    Matcher matcher = pattern.matcher(texto);
    if (matcher.find()) {
      return "(" + matcher.group(1) + ") " + matcher.group(2) + "-" + matcher.group(3);
    }
    return null;
  }

  private String extrairNumeroCarteira(String texto) {
    if (texto == null) return null;
    // Procura por "Número:" seguido de números
    Pattern pattern = Pattern.compile("Número:?\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(texto);
    return matcher.find() ? matcher.group(1) : null;
  }

  private LocalDate extrairValidadeCarteira(String texto) {
    if (texto == null) return null;
    Pattern pattern = Pattern.compile("Validade:?\\s*(\\d{2}/\\d{2}/\\d{4})", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(texto);
    if (matcher.find()) {
      try {
        return LocalDate.parse(matcher.group(1), PDF_DATE);
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

  private Lateralidade extrairLateralidade(String texto) {
    if (texto == null) return null;

    String textoUpper = texto.toUpperCase();
    if (textoUpper.contains("DIREITA") || textoUpper.contains(" D ")) {
      return new Lateralidade(Lateralidade.Tipo.DIREITA);
    } else if (textoUpper.contains("ESQUERDA") || textoUpper.contains(" E ")) {
      return new Lateralidade(Lateralidade.Tipo.ESQUERDA);
    } else if (textoUpper.contains("BILATERAL") || textoUpper.contains(" B ")) {
      return new Lateralidade(Lateralidade.Tipo.BILATERAL);
    } else if (textoUpper.contains("NAO SE APLICA") || textoUpper.contains("N/A")) {
      return new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL);
    }
    return null;
  }
}