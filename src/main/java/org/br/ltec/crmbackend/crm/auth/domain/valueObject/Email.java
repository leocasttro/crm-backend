package org.br.ltec.crmbackend.crm.auth.domain.valueObject;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor
public class Email {

  private static final Pattern EMAIL_PATTERN =
          Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

  private String value;

  public Email(String value) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException("Email não pode ser vazio");
    }

    String email = value.trim().toLowerCase();
    if (!EMAIL_PATTERN.matcher(email).matches()) {
      throw new IllegalArgumentException("Email inválido: " + value);
    }

    this.value = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Email email = (Email) o;
    return value.equals(email.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public String toString() {
    return value;
  }
}