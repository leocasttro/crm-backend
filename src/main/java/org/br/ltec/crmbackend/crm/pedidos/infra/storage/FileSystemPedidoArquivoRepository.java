package org.br.ltec.crmbackend.crm.pedidos.infra.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

import org.br.ltec.crmbackend.crm.pedidos.domain.port.PedidoArquivoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileSystemPedidoArquivoRepository implements PedidoArquivoRepository {

  private final Path baseDir;

  public FileSystemPedidoArquivoRepository(
          @Value("${crm.pedidos.storage.base-dir:./storage/pedidos}") String baseDir
  ) {
    this.baseDir = Paths.get(baseDir).toAbsolutePath().normalize();
    ensureDirExists(this.baseDir);
  }

  @Override
  public PedidoArquivoSalvo salvar(UUID pedidoId, ArquivoUpload upload) {
    validateUpload(upload);

    // Pasta do pedido: <base>/<pedidoId>/
    Path pedidoDir = baseDir.resolve(pedidoId.toString()).normalize();
    ensureDirExists(pedidoDir);

    // Nome do arquivo (evita colisão e path traversal)
    String extension = guessExtension(upload.contentType(), upload.nomeOriginal());
    String fileName = UUID.randomUUID() + extension;

    Path targetFile = pedidoDir.resolve(fileName).normalize();

    // Segurança extra: garante que o arquivo está dentro do baseDir
    if (!targetFile.startsWith(pedidoDir)) {
      throw new IllegalArgumentException("Caminho de arquivo inválido.");
    }

    try {
      Files.write(targetFile, upload.bytes());
    } catch (IOException e) {
      throw new RuntimeException("Falha ao salvar PDF no filesystem.", e);
    }

    String sha256 = sha256Hex(upload.bytes());
    long size = upload.bytes().length;

    // id aqui é o id do "registro do arquivo".
    // Como filesystem não gera id, usamos UUID (você pode trocar depois se salvar metadados no banco).
    UUID arquivoId = UUID.randomUUID();

    return new PedidoArquivoSalvo(
            arquivoId,
            pedidoId,
            safeName(upload.nomeOriginal()),
            upload.contentType(),
            size,
            sha256,
            targetFile.toString()
    );
  }

  @Override
  public Optional<PedidoArquivoSalvo> buscarPedidoId(UUID pedidoId) {
    return Optional.empty();
  }


  // ----------------- helpers -----------------

  private static void validateUpload(ArquivoUpload upload) {
    if (upload == null) throw new IllegalArgumentException("Upload é obrigatório.");
    if (upload.bytes() == null || upload.bytes().length == 0) {
      throw new IllegalArgumentException("Arquivo vazio.");
    }
    if (upload.nomeOriginal() == null || upload.nomeOriginal().isBlank()) {
      throw new IllegalArgumentException("Nome do arquivo é obrigatório.");
    }
    if (upload.contentType() == null || upload.contentType().isBlank()) {
      throw new IllegalArgumentException("Content-Type é obrigatório.");
    }

    // Se você quiser restringir para PDF:
    // Alguns browsers mandam application/pdf; outros podem mandar octet-stream.
    String ct = upload.contentType().toLowerCase();
    boolean looksPdf = ct.contains("pdf") || upload.nomeOriginal().toLowerCase().endsWith(".pdf");
    if (!looksPdf) {
      throw new IllegalArgumentException("Apenas arquivos PDF são permitidos.");
    }

    // Limite simples (ex.: 10MB) – ajuste como quiser
    int maxBytes = 10 * 1024 * 1024;
    if (upload.bytes().length > maxBytes) {
      throw new IllegalArgumentException("Arquivo excede o tamanho máximo de 10MB.");
    }
  }

  private static void ensureDirExists(Path dir) {
    try {
      Files.createDirectories(dir);
    } catch (IOException e) {
      throw new RuntimeException("Não foi possível criar diretório: " + dir, e);
    }
  }

  private static String sha256Hex(byte[] bytes) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(bytes);
      return HexFormat.of().formatHex(digest);
    } catch (NoSuchAlgorithmException e) {
      // SHA-256 sempre existe na JVM padrão
      throw new RuntimeException("SHA-256 indisponível.", e);
    }
  }

  private static String safeName(String original) {
    // só para armazenar no metadado (não usamos isso como path)
    String name = original.replace("\\", "/");
    int idx = name.lastIndexOf('/');
    name = (idx >= 0) ? name.substring(idx + 1) : name;

    // remove caracteres estranhos
    name = name.replaceAll("[\\r\\n\\t]", " ").trim();
    if (name.isBlank()) return "arquivo.pdf";
    return name;
  }

  private static String guessExtension(String contentType, String originalName) {
    String lower = (originalName == null ? "" : originalName.toLowerCase());
    if (lower.endsWith(".pdf")) return ".pdf";

    String ct = (contentType == null ? "" : contentType.toLowerCase());
    if (ct.contains("pdf")) return ".pdf";

    // fallback
    return ".bin";
  }
}
