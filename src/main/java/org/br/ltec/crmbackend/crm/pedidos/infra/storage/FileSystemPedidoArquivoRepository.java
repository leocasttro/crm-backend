package org.br.ltec.crmbackend.crm.pedidos.infra.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  // ==================== MÉTODO PRINCIPAL ====================

  @Override
  public PedidoArquivoSalvo salvar(UUID pedidoId, ArquivoUpload upload, Long checklistItemId) {
    validateUpload(upload);

    Path pedidoDir = baseDir.resolve(pedidoId.toString()).normalize();
    ensureDirExists(pedidoDir);

    String extension = guessExtension(upload.contentType(), upload.nomeOriginal());
    String fileName = UUID.randomUUID() + extension;

    Path targetFile = pedidoDir.resolve(fileName).normalize();

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

    UUID arquivoId = UUID.randomUUID();

    return new PedidoArquivoSalvo(
            arquivoId,
            pedidoId,
            safeName(upload.nomeOriginal()),
            upload.contentType(),
            size,
            sha256,
            targetFile.toString(),
            checklistItemId
    );
  }

  // ==================== MÉTODOS DA INTERFACE (CORRIGIDOS) ====================

  @Override
  public Optional<PedidoArquivoSalvo> buscarPedidoId(UUID pedidoId) {
    Path pedidoDir = baseDir.resolve(pedidoId.toString()).normalize();

    if (!Files.exists(pedidoDir)) {
      return Optional.empty();
    }

    try (Stream<Path> files = Files.list(pedidoDir)) {
      return files
              .filter(Files::isRegularFile)
              .findFirst()
              .map(this::criarPedidoArquivoSalvoFromPath);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao listar arquivos do pedido", e);
    }
  }

  @Override
  public boolean existeArquivoParaItem(UUID pedidoId, Long checklistItemId) {
    // Delega para o JPA - este método não deve ser usado diretamente no FileSystem
    return false;
  }

  @Override
  public Optional<PedidoArquivoSalvo> buscarPorPedidoEItem(UUID pedidoId, Long checklistItemId) {
    // Delega para o JPA - este método não deve ser usado diretamente no FileSystem
    return Optional.empty();
  }

  @Override
  public List<PedidoArquivoSalvo> listarPorPedido(UUID pedidoId) {
    Path pedidoDir = baseDir.resolve(pedidoId.toString()).normalize();

    if (!Files.exists(pedidoDir)) {
      return List.of();
    }

    try (Stream<Path> files = Files.list(pedidoDir)) {
      return files
              .filter(Files::isRegularFile)
              .map(this::criarPedidoArquivoSalvoFromPath)
              .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Erro ao listar arquivos do pedido", e);
    }
  }

  @Override
  public void removerPorPedidoEItem(UUID pedidoId, Long checklistItemId) {
    Path pedidoDir = baseDir.resolve(pedidoId.toString()).normalize();

    if (!Files.exists(pedidoDir)) {
      return;
    }

    try (Stream<Path> files = Files.list(pedidoDir)) {
      files.filter(Files::isRegularFile)
              .forEach(path -> {
                try {
                  Files.delete(path);
                } catch (IOException e) {
                  throw new RuntimeException("Erro ao deletar arquivo: " + path, e);
                }
              });
    } catch (IOException e) {
      throw new RuntimeException("Erro ao remover arquivos do pedido", e);
    }
  }

  // ==================== MÉTODOS AUXILIARES ====================

  private PedidoArquivoSalvo criarPedidoArquivoSalvoFromPath(Path path) {
    try {
      byte[] content = Files.readAllBytes(path);
      String sha256 = sha256Hex(content);
      String fileName = path.getFileName().toString();

      // Tenta extrair o ID do nome do arquivo (primeira parte antes do .)
      UUID arquivoId = UUID.randomUUID(); // fallback
      try {
        String uuidPart = fileName.substring(0, fileName.indexOf('.'));
        arquivoId = UUID.fromString(uuidPart);
      } catch (Exception e) {
        // Ignora, usa random
      }

      return new PedidoArquivoSalvo(
              arquivoId,
              UUID.fromString(path.getParent().getFileName().toString()),
              fileName,
              guessContentType(fileName),
              content.length,
              sha256,
              path.toString(),
              null // FileSystem não tem checklistItemId
      );
    } catch (IOException e) {
      throw new RuntimeException("Erro ao ler arquivo: " + path, e);
    }
  }

  // ==================== MÉTODOS ADICIONAIS (NÃO DA INTERFACE) ====================

  public Optional<PedidoArquivoSalvo> buscarPorId(UUID pedidoId, UUID arquivoId) {
    Path pedidoDir = baseDir.resolve(pedidoId.toString()).normalize();

    if (!Files.exists(pedidoDir)) {
      return Optional.empty();
    }

    String fileNamePrefix = arquivoId.toString();

    try (Stream<Path> files = Files.list(pedidoDir)) {
      return files
              .filter(Files::isRegularFile)
              .filter(path -> path.getFileName().toString().startsWith(fileNamePrefix))
              .findFirst()
              .map(this::criarPedidoArquivoSalvoFromPath);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao buscar arquivo por ID", e);
    }
  }

  public byte[] carregarConteudo(UUID pedidoId, UUID arquivoId) {
    Path pedidoDir = baseDir.resolve(pedidoId.toString()).normalize();
    String fileNamePrefix = arquivoId.toString();

    try (Stream<Path> files = Files.list(pedidoDir)) {
      Optional<Path> arquivo = files
              .filter(Files::isRegularFile)
              .filter(path -> path.getFileName().toString().startsWith(fileNamePrefix))
              .findFirst();

      if (arquivo.isPresent()) {
        return Files.readAllBytes(arquivo.get());
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao carregar conteúdo do arquivo", e);
    }

    return new byte[0];
  }

  // ==================== HELPERS (mantidos iguais) ====================

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

    String ct = upload.contentType().toLowerCase();
    boolean looksPdf = ct.contains("pdf") || upload.nomeOriginal().toLowerCase().endsWith(".pdf");
    if (!looksPdf) {
      throw new IllegalArgumentException("Apenas arquivos PDF são permitidos.");
    }

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
      throw new RuntimeException("SHA-256 indisponível.", e);
    }
  }

  private static String safeName(String original) {
    String name = original.replace("\\", "/");
    int idx = name.lastIndexOf('/');
    name = (idx >= 0) ? name.substring(idx + 1) : name;
    name = name.replaceAll("[\\r\\n\\t]", " ").trim();
    if (name.isBlank()) return "arquivo.pdf";
    return name;
  }

  private static String guessExtension(String contentType, String originalName) {
    String lower = (originalName == null ? "" : originalName.toLowerCase());
    if (lower.endsWith(".pdf")) return ".pdf";

    String ct = (contentType == null ? "" : contentType.toLowerCase());
    if (ct.contains("pdf")) return ".pdf";

    return ".bin";
  }

  private static String guessContentType(String fileName) {
    if (fileName.toLowerCase().endsWith(".pdf")) {
      return "application/pdf";
    }
    return "application/octet-stream";
  }

  // No FileSystemPedidoArquivoRepository.java
  public void removerPorCaminho(String caminho) {
    try {
      Path path = Paths.get(caminho);
      if (Files.exists(path)) {
        Files.delete(path);
      }
    } catch (IOException e) {
      throw new RuntimeException("Erro ao remover arquivo: " + caminho, e);
    }
  }
}