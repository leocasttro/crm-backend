package org.br.ltec.crmbackend.crm.pedidos.application.command;

import lombok.Value;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.PedidoId;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class SalvarDadosAutorizacaoCommand {
  UUID pedidoId;
  String statusAutorizacao;
  String numeroGuia;
  String senhaAutorizacao;
  LocalDate validadeAutorizacao;
  String tipoAcomodacao;
  String usuarioAtualizacao;
}
