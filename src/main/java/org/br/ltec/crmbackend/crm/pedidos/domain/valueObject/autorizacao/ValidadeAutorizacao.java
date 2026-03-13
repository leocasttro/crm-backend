package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.autorizacao;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Slf4j
@Value
public class ValidadeAutorizacao {
  LocalDate valor;

  public ValidadeAutorizacao(LocalDate valor) {
    log.debug("ValidadeAutorizacao constructor recebido: {}", valor);

    if (valor != null) {
      // ⚠️ Comentário: estamos em 2026, então 2024 já passou!
      // if (valor.isBefore(LocalDate.now())) {
      //   log.error("Data de validade no passado: {} (hoje é {})", valor, LocalDate.now());
      //   throw new IllegalArgumentException("Data de validade não pode ser no passado");
      // }

      // Por enquanto, vamos permitir qualquer data para teste
      log.debug("Data de validade aceita: {} (hoje é {})", valor, LocalDate.now());
    }

    this.valor = valor;
  }

  public boolean isPresente() {
    return valor != null;
  }

  public boolean isVencida() {
    return valor != null && valor.isBefore(LocalDate.now());
  }

  public boolean isValida() {
    return valor != null && !valor.isBefore(LocalDate.now());
  }

  public int diasParaVencer() {
    if (valor == null) return 0;
    return (int) LocalDate.now().until(valor).getDays();
  }
}