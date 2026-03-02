package org.br.ltec.crmbackend.crm.pedidos.domain.valueObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Value Object genérico para resultados de operações
 * @param <T> Tipo dos dados retornados em caso de sucesso
 */
public class ResultadoOperacao<T> {
  private final boolean sucesso;
  private final T dados;
  private final String mensagem;
  private final List<String> erros;

  private ResultadoOperacao(boolean sucesso, T dados, String mensagem, List<String> erros) {
    this.sucesso = sucesso;
    this.dados = dados;
    this.mensagem = mensagem;
    this.erros = erros != null ? Collections.unmodifiableList(erros) : Collections.emptyList();
  }

  public static <T> ResultadoOperacao<T> sucesso(T dados, String mensagem) {
    return new ResultadoOperacao<>(true, dados, mensagem, null);
  }

  public static <T> ResultadoOperacao<T> sucesso(T dados) {
    return new ResultadoOperacao<>(true, dados, "Operação realizada com sucesso", null);
  }

  public static <T> ResultadoOperacao<T> erro(String mensagem, String... erros) {
    return new ResultadoOperacao<>(false, null, mensagem, Arrays.asList(erros));
  }

  public static <T> ResultadoOperacao<T> erro(String mensagem) {
    return new ResultadoOperacao<>(false, null, mensagem, Collections.singletonList(mensagem));
  }

  public boolean isSucesso() {
    return sucesso;
  }

  public T getDados() {
    if (!sucesso) {
      throw new IllegalStateException("Não é possível obter dados de um resultado de erro");
    }
    return dados;
  }

  public String getMensagem() {
    return mensagem;
  }

  public List<String> getErros() {
    return erros;
  }

  public boolean hasErros() {
    return !erros.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResultadoOperacao<?> that = (ResultadoOperacao<?>) o;
    return sucesso == that.sucesso &&
            Objects.equals(dados, that.dados) &&
            Objects.equals(mensagem, that.mensagem) &&
            Objects.equals(erros, that.erros);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sucesso, dados, mensagem, erros);
  }

  @Override
  public String toString() {
    return "ResultadoOperacao{" +
            "sucesso=" + sucesso +
            ", dados=" + dados +
            ", mensagem='" + mensagem + '\'' +
            ", erros=" + erros +
            '}';
  }
}