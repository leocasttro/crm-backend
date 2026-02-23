package org.br.ltec.crmbackend.crm.pedidos.application.command;

public class CreatePedidoFromPdfCommand {

  private byte[] pdfBytes;
  private String originalFilename;
  private String contentType;

  // opcionais (mesmo motivo do request)
  private String cpf;
  private String pacienteId;

  public byte[] getPdfBytes() {
    return pdfBytes;
  }

  public void setPdfBytes(byte[] pdfBytes) {
    this.pdfBytes = pdfBytes;
  }

  public String getOriginalFilename() {
    return originalFilename;
  }

  public void setOriginalFilename(String originalFilename) {
    this.originalFilename = originalFilename;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getPacienteId() {
    return pacienteId;
  }

  public void setPacienteId(String pacienteId) {
    this.pacienteId = pacienteId;
  }
}
