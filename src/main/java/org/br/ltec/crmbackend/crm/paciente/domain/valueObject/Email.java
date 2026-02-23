package org.br.ltec.crmbackend.crm.paciente.domain.valueObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {
  private static final Pattern EMAIL_PATTERN = Pattern.compile(
          "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
  );
  private static final int MAX_LENGTH = 255;

  private final String endereco;
  private final String dominio;

  public Email(String endereco) {
    // ✅ PERMITE NULL AGORA
    if (endereco == null || endereco.trim().isEmpty()) {
      this.endereco = null;
      this.dominio = null;
      return;
    }

    String emailValidado = endereco.toLowerCase().trim();
    validar(emailValidado);
    this.endereco = emailValidado;
    this.dominio = extrairDominio(this.endereco);
  }

  private void validar(String endereco) {
    if (endereco == null || endereco.trim().isEmpty()) {
      throw new IllegalArgumentException("Email não pode ser vazio");
    }

    String emailTrimmed = endereco.trim();

    if (emailTrimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("Email muito longo");
    }

    if (!EMAIL_PATTERN.matcher(emailTrimmed).matches()) {
      throw new IllegalArgumentException("Email inválido: " + endereco);
    }
  }

  private String extrairDominio(String email) {
    if (email == null) return null;
    return email.substring(email.indexOf('@') + 1);
  }

  public String getEndereco() {
    return endereco;
  }

  public String getDominio() {
    return dominio;
  }

  public String getUsuario() {
    if (endereco == null) return null;
    return endereco.substring(0, endereco.indexOf('@'));
  }

  public boolean isGmail() {
    return dominio != null && dominio.equalsIgnoreCase("gmail.com");
  }

  public boolean isOutlook() {
    return dominio != null && (dominio.equalsIgnoreCase("outlook.com") || dominio.equalsIgnoreCase("hotmail.com"));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Email email = (Email) o;
    return Objects.equals(endereco, email.endereco);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endereco);
  }

  @Override
  public String toString() {
    return endereco == null ? "" : endereco;
  }
}