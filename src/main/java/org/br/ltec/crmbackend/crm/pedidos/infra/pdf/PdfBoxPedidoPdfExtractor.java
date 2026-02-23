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

    // --------- Campos simples ---------
    String nomePaciente = firstGroup(text, PedidoPdfPatterns.NOME_PACIENTE).orElse(null);

    String dataPedido = firstGroup(text, PedidoPdfPatterns.DATA_PEDIDO).orElse(null);
    String horaPedido = firstGroup(text, PedidoPdfPatterns.HORA_PEDIDO).orElse(null);

    String convenio = firstGroup(text, PedidoPdfPatterns.CONVENIO).orElse(null);
    String hospital = firstGroup(text, PedidoPdfPatterns.HOSPITAL).orElse(null);

    String cid = firstGroup(text, PedidoPdfPatterns.CID)
            .or(() -> firstGroup(text, PedidoPdfPatterns.CID_ALTERNATIVO))
            .orElse(null);

    // Médico + CRM
    String medicoNome = null;
    String crmUf = null;
    String crmNumero = null;

    Matcher mMed = PedidoPdfPatterns.MEDICO_E_CRM.matcher(text);
    if (mMed.find()) {
      medicoNome = mMed.group(1).trim();
      crmUf = mMed.group(2).trim();
      crmNumero = mMed.group(3).trim();
    }

    // --------- Procedimentos ---------
    List<PedidoExtraido.ProcedimentoExtraido> procedimentos = new ArrayList<>();
    Matcher mProc = PedidoPdfPatterns.PROCEDIMENTO_LINHA.matcher(text);
    while (mProc.find()) {
      String codigo = safe(mProc.group(1));
      String descricao = safe(mProc.group(2));

      // filtro simples pra não pegar linhas malucas
      if (codigo != null && codigo.length() >= 6 && descricao != null && descricao.length() >= 3) {
        procedimentos.add(new PedidoExtraido.ProcedimentoExtraido(codigo, descricao));
      }
    }

    // --------- Monta resultado ---------
    PedidoExtraido extraido = new PedidoExtraido();
    extraido.setNomePaciente(nomePaciente);
    extraido.setDataPedido(dataPedido);   // "dd/MM/yyyy"
    extraido.setHoraPedido(horaPedido);   // "HH:mm:ss"
    extraido.setConvenio(convenio);
    extraido.setHospital(hospital);
    extraido.setCid(cid);
    extraido.setMedicoNome(medicoNome);
    extraido.setCrmUf(crmUf);
    extraido.setCrmNumero(crmNumero);
    extraido.setProcedimentos(procedimentos);

    // opcional, mas MUITO útil pra debug quando regex falhar
    extraido.setTextoNormalizado(text);

    return extraido;
  }

  // ---------------- helpers ----------------

  private static String extractText(byte[] pdfBytes) {
    try (PDDocument doc = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
      PDFTextStripper stripper = new PDFTextStripper();
      stripper.setSortByPosition(true);
      return stripper.getText(doc);
    } catch (IOException e) {
      throw new RuntimeException("Falha ao ler PDF (PDFBox).", e);
    }
  }

  private static Optional<String> firstGroup(String text, java.util.regex.Pattern pattern) {
    Matcher m = pattern.matcher(text);
    if (m.find()) {
      return Optional.ofNullable(safe(m.group(1)));
    }
    return Optional.empty();
  }

  private static String safe(String s) {
    if (s == null) return null;
    String t = s.trim();
    return t.isBlank() ? null : t;
  }
}
