public class Paciente {

  private final PacienteId id;
  private final NomeCompleto nome;
  private final Documento documento;
  private final Email email;
  private LocalDate dataNascimento;

  public Paciente(PacienteId id, NomeCompleto nome, Documento documento, LocalDate dataNascimento) {
    if (dataNascimento.isAfter(LocalDate.now())) {
      throw new DomainException("Data de nascimento inválida");
    }
    this.id = id;
    this.nome = nome;
    this.documento = documento;
    this.dataNascimento = dataNascimento;
  }

  public void atualizarNome(NomeCompleto novoNome) {
    this.nome = novoNome;
  }
}
