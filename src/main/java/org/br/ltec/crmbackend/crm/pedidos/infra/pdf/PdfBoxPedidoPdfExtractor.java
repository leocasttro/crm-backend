package org.br.ltec.crmbackend.crm.pedidos.infra.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.br.ltec.crmbackend.crm.pedidos.application.useCase.PedidoExtraido;
import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoPdfExtractor;
import org.springframework.stereotype.Component;

@Component
public class PdfBoxPedidoPdfExtractor implements PedidoPdfExtractor {

  private final PedidoPdfTextNormalizer normalizer = new PedidoPdfTextNormalizer();

  @Override
  public PedidoExtraido extract(byte[] pdfBytes) {
    if (pdfBytes == null || pdfBytes.length == 0) {
      throw new IllegalArgumentException("PDF vazio.");
    }

    String rawText = extractText(pdfBytes);
    String text = normalizer.normalize(rawText);

    PedidoExtraido extraido = new PedidoExtraido();

    // ==================== DADOS BÁSICOS ====================
    extraido.setNomePaciente(firstGroup(text, PedidoPdfPatterns.NOME_PACIENTE).orElse(null));
    extraido.setDataPedido(firstGroup(text, PedidoPdfPatterns.DATA_PEDIDO).orElse(null));
    extraido.setHoraPedido(firstGroup(text, PedidoPdfPatterns.HORA_PEDIDO).orElse(null));
    extraido.setConvenio(firstGroup(text, PedidoPdfPatterns.CONVENIO).orElse(null));
    extraido.setHospital(firstGroup(text, PedidoPdfPatterns.HOSPITAL).orElse(null));
    extraido.setTipo(firstGroup(text, PedidoPdfPatterns.TIPO).orElse(null));
    extraido.setAlergias(firstGroup(text, PedidoPdfPatterns.ALERGIAS).orElse(null));
    extraido.setLateralidade(firstGroup(text, PedidoPdfPatterns.LATERALIDADE).orElse(null));

    // ==================== DATA DE NASCIMENTO ====================
    extraido.setDataNascimento(firstGroup(text, PedidoPdfPatterns.DATA_NASCIMENTO).orElse(null));

    // ==================== CID PRINCIPAL ====================
    String cid = firstGroup(text, PedidoPdfPatterns.CID_PRINCIPAL)
            .or(() -> firstGroup(text, PedidoPdfPatterns.CID_10_PRINCIPAL))
            .orElse(null);
    extraido.setCid(cid);

    // ==================== CIDs SECUNDÁRIOS ====================
    extraido.setCid2(firstGroup(text, PedidoPdfPatterns.CID_10_SECUNDARIO_2).orElse(null));
    extraido.setCid3(firstGroup(text, PedidoPdfPatterns.CID_10_SECUNDARIO_3).orElse(null));
    extraido.setCid4(firstGroup(text, PedidoPdfPatterns.CID_10_SECUNDARIO_4).orElse(null));

    // ==================== MÉDICO E CRM (formato inicial) ====================
    Matcher mMed = PedidoPdfPatterns.MEDICO_E_CRM.matcher(text);
    if (mMed.find()) {
      extraido.setMedicoNome(safe(mMed.group(1)));
      extraido.setCrmUf(safe(mMed.group(2)));
      extraido.setCrmNumero(safe(mMed.group(3)));
    }

    // ==================== DADOS COMPLETOS DO MÉDICO (da guia) ====================
    Matcher mMedCompleto = PedidoPdfPatterns.MEDICO_DADOS_COMPLETOS.matcher(text);
    if (mMedCompleto.find()) {
      // Se já não tiver nome do médico, usa o da guia
      if (extraido.getMedicoNome() == null) {
        extraido.setMedicoNome(safe(mMedCompleto.group(1)));
      }
      extraido.setConselhoProfissional(safe(mMedCompleto.group(2)));
      extraido.setNumeroConselho(safe(mMedCompleto.group(3)));
      extraido.setUfConselho(safe(mMedCompleto.group(4)));
      extraido.setCbo(safe(mMedCompleto.group(5)));
    }

    // ==================== TELEFONE ====================
    Matcher mTel = PedidoPdfPatterns.TELEFONE.matcher(text);
    if (mTel.find()) {
      String telefone = "(" + mTel.group(1) + ") " + mTel.group(2) + "-" + mTel.group(3);
      extraido.setTelefone(telefone);
    }

    // ==================== ENDEREÇO ====================
    Matcher mEnd = PedidoPdfPatterns.ENDERECO.matcher(text);
    if (mEnd.find()) {
      extraido.setEnderecoMedico(safe(mEnd.group(1)));
    }

    // ==================== PROCEDIMENTOS (OPME - formato simples) ====================
    List<PedidoExtraido.ProcedimentoExtraido> procedimentos = new ArrayList<>();
    Matcher mProcOPME = PedidoPdfPatterns.PROCEDIMENTO_OPME.matcher(text);
    while (mProcOPME.find()) {
      String quantidade = safe(mProcOPME.group(1));
      String codigo = safe(mProcOPME.group(2));
      String descricao = safe(mProcOPME.group(3));

      if (codigo != null && codigo.length() >= 6 && descricao != null) {
        procedimentos.add(new PedidoExtraido.ProcedimentoExtraido(codigo, descricao, quantidade));
      }
    }

    // ==================== PROCEDIMENTOS (formato da guia) ====================
    Matcher mProcGuia = PedidoPdfPatterns.PROCEDIMENTO_GUIA.matcher(text);
    while (mProcGuia.find()) {
      String codigo = safe(mProcGuia.group(1));
      String descricao = safe(mProcGuia.group(2));
      String quantidade = safe(mProcGuia.group(3));

      if (codigo != null && codigo.length() >= 6 && descricao != null) {
        // Evita duplicatas
        boolean exists = procedimentos.stream()
                .anyMatch(p -> codigo.equals(p.getCodigo()));
        if (!exists) {
          procedimentos.add(new PedidoExtraido.ProcedimentoExtraido(codigo, descricao, quantidade));
        }
      }
    }
    extraido.setProcedimentos(procedimentos);

    // ==================== DADOS DA GUIA ====================
    extraido.setNumeroGuia(firstGroup(text, PedidoPdfPatterns.NUMERO_GUIA).orElse(null));
    extraido.setRegistroAns(firstGroup(text, PedidoPdfPatterns.REGISTRO_ANS).orElse(null));
    extraido.setNumeroGuiaOperadora(firstGroup(text, PedidoPdfPatterns.NUMERO_GUIA_OPERADORA).orElse(null));

    // ==================== DADOS DO BENEFICIÁRIO ====================
    extraido.setNumeroCarteira(firstGroup(text, PedidoPdfPatterns.NUMERO_CARTEIRA).orElse(null));
    extraido.setValidadeCarteira(firstGroup(text, PedidoPdfPatterns.VALIDADE_CARTEIRA).orElse(null));
    extraido.setCartaoNacionalSaude(firstGroup(text, PedidoPdfPatterns.CARTAO_NACIONAL_SAUDE).orElse(null));

    // ==================== DADOS DO CONTRATADO ====================
    extraido.setCodigoOperadora(firstGroup(text, PedidoPdfPatterns.CODIGO_OPERADORA).orElse(null));
    extraido.setNomeContratado(firstGroup(text, PedidoPdfPatterns.NOME_CONTRATADO).orElse(null));

    // ==================== DADOS DA INTERNAÇÃO ====================
    extraido.setCaraterAtendimento(firstGroup(text, PedidoPdfPatterns.CARATER_ATENDIMENTO).orElse(null));
    extraido.setTipoInternacao(firstGroup(text, PedidoPdfPatterns.TIPO_INTERNACAO).orElse(null));
    extraido.setRegimeInternacao(firstGroup(text, PedidoPdfPatterns.REGIME_INTERNACAO).orElse(null));
    extraido.setQtdDiariasSolicitadas(firstGroup(text, PedidoPdfPatterns.QTD_DIARIAS).orElse(null));
    extraido.setPrevisaoUsoOpmepdf(firstGroup(text, PedidoPdfPatterns.PREVISAO_OPME).orElse(null));

    // ==================== INDICAÇÃO CLÍNICA (multilinha) ====================
    Matcher mIndicacao = PedidoPdfPatterns.INDICACAO_CLINICA.matcher(text);
    if (mIndicacao.find()) {
      extraido.setIndicacaoClinica(safe(mIndicacao.group(1).replaceAll("\\s+", " ")));
    }

    // 🔥 NOVO: RELATÓRIO PRÉ-OPERATÓRIO
    Matcher mRelatorio = PedidoPdfPatterns.RELATORIO_PRE_OPERATORIO.matcher(text);
    if (mRelatorio.find()) {
      extraido.setRelatorioPreOperatorio(safe(mRelatorio.group(1).replaceAll("\\s+", " ")));
    }

    // 🔥 NOVO: ORIENTAÇÕES
    Matcher mOrientacoes = PedidoPdfPatterns.ORIENTACOES.matcher(text);
    if (mOrientacoes.find()) {
      extraido.setOrientacoes(safe(mOrientacoes.group(1).replaceAll("\\s+", " ")));
    }

    // ==================== DATA DA SOLICITAÇÃO ====================
    extraido.setDataSolicitacao(firstGroup(text, PedidoPdfPatterns.DATA_SOLICITACAO).orElse(null));

    // ==================== TEXTO NORMALIZADO (para debug) ====================
    extraido.setTextoNormalizado(text);

    return extraido;
  }

  // ---------------- helpers ----------------

  private String extractText(byte[] pdfBytes) {
    try (PDDocument doc = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
      PDFTextStripper stripper = new PDFTextStripper();
      stripper.setSortByPosition(true);
      return stripper.getText(doc);
    } catch (IOException e) {
      throw new RuntimeException("Falha ao ler PDF (PDFBox).", e);
    }
  }

  private Optional<String> firstGroup(String text, java.util.regex.Pattern pattern) {
    Matcher m = pattern.matcher(text);
    if (m.find()) {
      return Optional.ofNullable(safe(m.group(1)));
    }
    return Optional.empty();
  }

  private String safe(String s) {
    if (s == null) return null;
    String t = s.trim();
    return t.isBlank() ? null : t;
  }
}