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

    // 📌 DADOS DO PACIENTE
    CreatePacienteCommand pacienteCmd = criarPacienteCommand(extraido);
    cmd.setPaciente(pacienteCmd);

    // 📌 CONVÊNIO
    cmd.setConvenioNome(extraido.getConvenio());
    cmd.setConvenioNumeroCarteira(extraido.getNumeroCarteira() != null ?
            extraido.getNumeroCarteira() : extrairNumeroCarteira(extraido.getTextoNormalizado()));

    // Validade da carteira
    if (extraido.getValidadeCarteira() != null && !extraido.getValidadeCarteira().isEmpty()) {
      try {
        cmd.setConvenioValidadeCarteira(LocalDate.parse(extraido.getValidadeCarteira(), PDF_DATE));
      } catch (Exception e) {
        LocalDate validade = extrairValidadeCarteira(extraido.getTextoNormalizado());
        cmd.setConvenioValidadeCarteira(validade);
      }
    } else {
      LocalDate validade = extrairValidadeCarteira(extraido.getTextoNormalizado());
      if (validade != null) {
        cmd.setConvenioValidadeCarteira(validade);
      }
    }

    // 📌 CID PRINCIPAL
    cmd.setCidCodigo(extraido.getCid());

    // 🔥 CIDs SECUNDÁRIOS
    cmd.setCidCodigo2(extraido.getCid2());
    cmd.setCidCodigo3(extraido.getCid3());
    cmd.setCidCodigo4(extraido.getCid4());

    // 📌 MÉDICO
    cmd.setMedicoSolicitanteNome(extraido.getMedicoNome());
    if (extraido.getCrmNumero() != null) {
      String crm = extraido.getCrmNumero();
      if (extraido.getCrmUf() != null) {
        crm = crm + "/" + extraido.getCrmUf();
      }
      cmd.setMedicoSolicitanteCrm(crm);
    }

    // Dados completos do médico
    cmd.setMedicoSolicitanteEspecialidade(extraido.getCbo());
    cmd.setMedicoExecutorNome(extraido.getMedicoExecutorNome());
    cmd.setMedicoExecutorCrm(extraido.getMedicoExecutorCrm());
    cmd.setMedicoExecutorEspecialidade(extraido.getMedicoExecutorEspecialidade());

    // 📌 PROCEDIMENTO - CORRIGIDO
    if (extraido.getProcedimentos() != null && !extraido.getProcedimentos().isEmpty()) {
      var primeiroProcedimento = extraido.getProcedimentos().get(0);
      cmd.setProcedimentoCodigoTUSS(primeiroProcedimento.getCodigo());

      // ✅ SEMPRE usa a descrição do procedimento
      cmd.setProcedimentoDescricao(primeiroProcedimento.getDescricao());
    }

    // 🔥 DADOS CLÍNICOS
    cmd.setIndicacaoClinica(extraido.getIndicacaoClinica());
    cmd.setRelatorioPreOperatorio(extraido.getRelatorioPreOperatorio());
    cmd.setOrientacoes(extraido.getOrientacoes());

    // 📌 DATA DO PEDIDO
    if (extraido.getDataPedido() != null && !extraido.getDataPedido().isBlank()) {
      cmd.setDataPedido(LocalDate.parse(extraido.getDataPedido(), PDF_DATE));
    }

    // 📌 PRIORIDADE
    if (extraido.getCaraterAtendimento() != null) {
      String carater = extraido.getCaraterAtendimento().toUpperCase();
      if (carater.contains("URG")) {
        cmd.setPrioridade(new Prioridade(Prioridade.Tipo.URGENTE));
      } else {
        cmd.setPrioridade(new Prioridade(Prioridade.Tipo.ELETIVA));
      }
    } else {
      cmd.setPrioridade(new Prioridade(Prioridade.Tipo.ELETIVA));
    }

    // 📌 LATERALIDADE
    if (extraido.getLateralidade() != null) {
      cmd.setLateralidade(mapearLateralidade(extraido.getLateralidade()));
    } else {
      Lateralidade lateralidade = extrairLateralidade(extraido.getTextoNormalizado());
      cmd.setLateralidade(lateralidade != null ? lateralidade :
              new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL));
    }

    // 🔥 DADOS DA GUIA
    cmd.setNumeroGuia(extraido.getNumeroGuia());
    cmd.setRegistroAns(extraido.getRegistroAns());
    cmd.setNumeroGuiaOperadora(extraido.getNumeroGuiaOperadora());
    cmd.setCodigoOperadora(extraido.getCodigoOperadora());
    cmd.setNomeContratado(extraido.getNomeContratado());

    // 🔥 DADOS DA INTERNAÇÃO
    cmd.setCaraterAtendimento(extraido.getCaraterAtendimento());
    cmd.setTipoInternacao(extraido.getTipoInternacao());
    cmd.setRegimeInternacao(extraido.getRegimeInternacao());
    cmd.setQtdDiariasSolicitadas(extraido.getQtdDiariasSolicitadas());

    // 🔥 DADOS DE CONTATO DO PACIENTE
    cmd.setTelefonePaciente(extraido.getTelefone());
    cmd.setEnderecoPaciente(extraido.getEnderecoMedico());
    cmd.setCpfPaciente(extraido.getCpf());
    cmd.setEmailPaciente(extraido.getEmail());
    cmd.setSexoPaciente(extraido.getSexo());

    // 📌 OBSERVAÇÕES
    List<String> observacoes = new ArrayList<>();

    if (extraido.getTextoNormalizado() != null && !extraido.getTextoNormalizado().isEmpty()) {
      String obs = extraido.getTextoNormalizado();
      if (obs.length() > 500) {
        obs = obs.substring(0, 500) + "...";
      }
      observacoes.add(obs);
    }

    if (extraido.getOrientacoes() != null && !extraido.getOrientacoes().isEmpty()) {
      observacoes.add("ORIENTAÇÕES: " + extraido.getOrientacoes());
    }

    if (!observacoes.isEmpty()) {
      cmd.setObservacoes(observacoes);
    }

    return cmd;
  }

  private CreatePacienteCommand criarPacienteCommand(PedidoExtraido extraido) {
    CreatePacienteCommand pacienteCmd = new CreatePacienteCommand();

    // 🔹 Nome completo
    String nomeCompleto = extraido.getNomePaciente();
    if (nomeCompleto != null && !nomeCompleto.isEmpty()) {
      String[] partes = nomeCompleto.split(" ", 2);
      pacienteCmd.setPrimeiroNome(partes[0]);
      pacienteCmd.setSobrenome(partes.length > 1 ? partes[1] : "");
    }

    // 🔹 Data de nascimento
    String dataNascimentoStr = extraido.getDataNascimento();
    if (dataNascimentoStr != null && !dataNascimentoStr.isEmpty()) {
      try {
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, PDF_DATE);
        pacienteCmd.setDataNascimento(dataNascimento);
      } catch (Exception e) {
        System.err.println("Erro ao converter data: " + dataNascimentoStr);
      }
    }

    // 🔹 CPF
    String cpf = extraido.getCpf() != null ? extraido.getCpf() : extrairCpf(extraido.getTextoNormalizado());
    if (cpf != null) {
      pacienteCmd.setDocumentoNumero(cpf.replaceAll("[^0-9]", ""));
      pacienteCmd.setDocumentoTipo(Documento.TipoDocumento.CPF);
    }

    // 🔹 Email
    if (extraido.getEmail() != null) {
      pacienteCmd.setEmail(extraido.getEmail());
    }

    // 🔹 Telefone
    if (extraido.getTelefone() != null) {
      List<CreatePacienteCommand.TelefoneRequest> telefones = new ArrayList<>();
      telefones.add(new CreatePacienteCommand.TelefoneRequest(
              extraido.getTelefone(),
              Telefone.TipoTelefone.CELULAR,
              true
      ));
      pacienteCmd.setTelefones(telefones);
    }

    // 🔹 Sexo - CORRIGIDO
    if (extraido.getSexo() != null) {
      String sexoStr = extraido.getSexo().toUpperCase();
      if (sexoStr.contains("M") || sexoStr.equals("MASCULINO")) {
        pacienteCmd.setSexo(Sexo.Tipo.MASCULINO);
      } else if (sexoStr.contains("F") || sexoStr.equals("FEMININO")) {
        pacienteCmd.setSexo(Sexo.Tipo.FEMININO);
      } else {
        pacienteCmd.setSexo(Sexo.Tipo.NAO_INFORMADO);
      }
    }

    return pacienteCmd;
  }

  private Lateralidade mapearLateralidade(String lateralidade) {
    if (lateralidade == null) return new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL);
    String upper = lateralidade.toUpperCase();
    if (upper.contains("DIREITA")) return new Lateralidade(Lateralidade.Tipo.DIREITA);
    if (upper.contains("ESQUERDA")) return new Lateralidade(Lateralidade.Tipo.ESQUERDA);
    if (upper.contains("BILATERAL")) return new Lateralidade(Lateralidade.Tipo.BILATERAL);
    return new Lateralidade(Lateralidade.Tipo.NAO_APLICAVEL);
  }

  private String extrairCpf(String texto) {
    if (texto == null) return null;
    Pattern pattern = Pattern.compile("\\b(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})\\b");
    Matcher matcher = pattern.matcher(texto);
    return matcher.find() ? matcher.group(1) : null;
  }

  private String extrairNumeroCarteira(String texto) {
    if (texto == null) return null;
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