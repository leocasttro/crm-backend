package org.br.ltec.crmbackend.crm.pedidos.infra.pdf;

import java.text.Normalizer;

public class PedidoPdfTextNormalizer {

  public String normalize(String rawText) {
    if (rawText == null || rawText.isBlank()) {
      return "";
    }

    String text = rawText;

    text = Normalizer.normalize(text, Normalizer.Form.NFKC);

    text = text.replaceAll("[\\p{Cntrl}&&[^\n]]", "");

    text = text.replaceAll("[ \\t]+", " ");

    text = text.replaceAll("\\s*:\\*", ": ");

    text = text.replaceAll("(?m)^\\s+", "");
    text = text.replaceAll("(?m)\\s+$", "");

    text = text.replaceAll("(?m)^(\\s*\\n){2,}", "\n");

    return text.trim();
  }

}
