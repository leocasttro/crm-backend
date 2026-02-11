package org.br.ltec.crmbackend.crm.paciente.infra.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pacientes",
        indexes = {
                @Index(name = "idx_paciente_documento", columnList = "documento_numero", unique = true),
                @Index(name = "idx_paciente_email", columnList = "email", unique = true),
                @Index(name = "idx_paciente_nome", columnList = "nome_completo"),
                @Index(name = "idx_paciente_cidade", columnList = "cidade")
        })
@Getter
@Setter
public class PacienteJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "primeiro_nome", nullable = false, length = 50)
  private String primeiroNome;

  @Column(name = "sobrenome", nullable = false, length = 50)
  private String sobrenome;

  @Column(name = "nome_completo", nullable = false, length = 100)
  private String nomeCompleto;

  @Column(name = "documento_numero", nullable = false, unique = true, length = 20)
  private String documentoNumero;

  @Column(name = "documento_tipo", nullable = false, length = 10)
  private String documentoTipo;

  @Column(name = "email", nullable = false, unique = true, length = 255)
  private String email;

  @Column(name = "data_nascimento", nullable = false)
  private LocalDate dataNascimento;

  @Column(name = "idade")
  private Integer idade;

  @Column(name = "sexo_codigo", length = 1)
  private Character sexoCodigo;

  @Column(name = "sexo_descricao", length = 20)
  private String sexoDescricao;

  // Endereço
  @Column(name = "logradouro", length = 100)
  private String logradouro;

  @Column(name = "numero", length = 10)
  private String numero;

  @Column(name = "complemento", length = 50)
  private String complemento;

  @Column(name = "bairro", length = 50)
  private String bairro;

  @Column(name = "cidade", length = 50)
  private String cidade;

  @Column(name = "estado", length = 2)
  private String estado;

  @Column(name = "cep", length = 9)
  private String cep;

  @Column(name = "pais", length = 50)
  private String pais = "Brasil";

  // Telefones (armazenados como JSON string)
  @Column(name = "telefones", columnDefinition = "TEXT")
  private String telefones;

  // Campos calculados
  @Column(name = "maior_de_idade")
  private Boolean maiorDeIdade;

  @Column(name = "idoso")
  private Boolean idoso;

  @Column(name = "possui_whatsapp")
  private Boolean possuiWhatsApp;

  @Column(name = "ativo", nullable = false)
  private Boolean ativo = true;

  @Column(name = "criado_em", nullable = false)
  private LocalDate criadoEm = LocalDate.now();

  @Column(name = "atualizado_em")
  private LocalDate atualizadoEm;

  @PreUpdate
  protected void onUpdate() {
    this.atualizadoEm = LocalDate.now();
  }
}