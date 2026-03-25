package org.br.ltec.crmbackend.crm.pedidos.adapter.web;

import org.br.ltec.crmbackend.crm.pedidos.domain.model.OpmeItem;
import org.br.ltec.crmbackend.crm.pedidos.domain.model.PedidoCirurgico;
import org.br.ltec.crmbackend.crm.pedidos.domain.valueObject.Procedimento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoResponseMapper {

  private OpmeItemResponse toOpmeItemResponse(OpmeItem item) {
    if (item == null) return null;
    return OpmeItemResponse.builder()
            .id(item.getId().getValue().toString())
            .descricao(item.getDescricao())
            .quantidade(item.getQuantidade())
            .marcasAceitas(item.getMarcasAceitas())
            .marcasNegadas(item.getMarcasNegadas())
            .observacao(item.getObservacao())
            .build();
  }

  public PedidoResponse toResponse(PedidoCirurgico p) {
    List<OpmeItemResponse> opmeItens = p.getOpmeItens().stream()
            .map(this::toOpmeItemResponse)
            .collect(Collectors.toList());

    return PedidoResponse.builder()
            .id(p.getId().getValue().toString())
            .pacienteId(p.getPacienteId().getValue().toString())
            .status(p.getStatus().getTipo().name())
            .prioridade(p.getPrioridade().getTipo().name())
            .prioridadeJustificativa(p.getPrioridade().getJustificativa())
            .criadoEm(p.getCriadoEm())
            .atualizadoEm(p.getAtualizadoEm())
            .dataPedido(p.getDataPedido())
            .agendamentoDataHora(p.temAgendamento() ? p.getAgendamento().getDataHora() : null)
            .agendamentoLocal(p.temAgendamento() ? p.getAgendamento().getLocal() : null)
            .agendamentoHospital(p.temAgendamento() ? p.getAgendamento().getHospital() : null)
            .agendamentoFornecedor(p.temAgendamento() ? p.getAgendamento().getFornecedor() : null)
            .agendamentoRiscoCirurgico(p.temAgendamento() ? p.getAgendamento().getRiscoCirurgico() : null)
            .procedimento(p.getProcedimento().getDescricao())
            .procedimentoDescricao(p.getProcedimento().getDescricao())
            .procedimentoCodigo(p.getProcedimento().getCodigoTUSS())
            .procedimentoCategoria(p.getProcedimento().getCategoria())
            .procedimentos(mapProcedimentos(p.getTodosProcedimentos()))
            .indicacaoClinica(p.getIndicacaoClinica())
            .relatorioPreOperatorio(p.getRelatorioPreOperatorio())
            .orientacoes(p.getOrientacoes())
            .lateralidade(p.getLateralidade().getTipo().name())
            .convenio(p.getConvenio().getNome())
            .convenioNome(p.getConvenio().getNome())
            .convenioNumeroCarteira(p.getConvenio().getNumeroCarteira())
            .convenioValidadeCarteira(p.getConvenio().getValidade())
            .convenioTipoPlano(p.getConvenio().getTipoPlano())
            .numeroCarteira(p.getConvenio().getNumeroCarteira())
            .validadeCarteira(p.getConvenio().getValidade())
            .cid(p.getCid() != null ? p.getCid().getCodigo() : null)
            .cidCodigo(p.getCid() != null ? p.getCid().getCodigo() : null)
            .cidDescricao(p.getCid() != null ? p.getCid().getDescricao() : null)
            .cidCodigo2(p.getCidCodigo2())
            .cidCodigo3(p.getCidCodigo3())
            .cidCodigo4(p.getCidCodigo4())
            .medicoSolicitante(p.getMedicoSolicitante().getNome())
            .medicoSolicitanteNome(p.getMedicoSolicitante().getNome())
            .medicoSolicitanteCrm(p.getMedicoSolicitante().getCrm())
            .medicoSolicitanteEspecialidade(p.getMedicoSolicitante().getEspecialidade())
            .medicoExecutorNome(p.getMedicoExecutor() != null ? p.getMedicoExecutor().getNome() : null)
            .medicoExecutorCrm(p.getMedicoExecutor() != null ? p.getMedicoExecutor().getCrm() : null)
            .medicoExecutorEspecialidade(p.getMedicoExecutor() != null ? p.getMedicoExecutor().getEspecialidade() : null)
            .numeroGuia(p.getNumeroGuia())
            .registroAns(p.getRegistroAns())
            .numeroGuiaOperadora(p.getNumeroGuiaOperadora())
            .codigoOperadora(p.getCodigoOperadora())
            .nomeContratado(p.getNomeContratado())
            .caraterAtendimento(p.getCaraterAtendimento())
            .tipoInternacao(p.getTipoInternacao())
            .regimeInternacao(p.getRegimeInternacao())
            .qtdDiariasSolicitadas(p.getQtdDiariasSolicitadas())
            .temDocumentos(!p.getDocumentosAnexados().isEmpty())
            .documentosAnexados(p.getDocumentosAnexados())
            .observacoes(p.getObservacoes())
            .quantidadeObservacoes(p.getObservacoes().size())
            .consultaPreDataHora(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getDataHora() : null)
            .consultaPreCuidados(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getCuidados() : null)
            .consultaPreObservacoesEspeciais(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getObservacoesEspeciais() : null)
            .consultaPreLocal(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().local() : null)
            .statusAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getStatus() != null ?
                    p.getDadosAutorizacao().getStatus().getValor() : null)
            .numeroGuiaAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getNumeroGuia() != null ?
                    p.getDadosAutorizacao().getNumeroGuia().getValor() : null)
            .senhaAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getSenha() != null ?
                    p.getDadosAutorizacao().getSenha().getValor() : null)
            .validadeAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getValidade() != null ?
                    p.getDadosAutorizacao().getValidade().getValor() : null)
            .tipoAcomodacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getTipoAcomodacao() != null ?
                    p.getDadosAutorizacao().getTipoAcomodacao().getValor() : null)
            .opmeItens(opmeItens)
            .build();
  }

  public PedidoDetalhadoResponse toDetalhadoResponse(PedidoCirurgico p) {
    System.out.println("=== Mapeando PedidoDetalhadoResponse ===");
    System.out.println("Pedido ID: " + p.getId().getValue());
    System.out.println("OPME items count: " + p.getOpmeItens().size());

    p.getOpmeItens().forEach(item -> {
      System.out.println("  - ID: " + item.getId().getValue());
      System.out.println("    Descrição: " + item.getDescricao());
      System.out.println("    Quantidade: " + item.getQuantidade());
      System.out.println("    Marcas Aceitas: " + item.getMarcasAceitas());
    });

    List<OpmeItemResponse> opmeItens = p.getOpmeItens().stream()
            .map(this::toOpmeItemResponse)
            .collect(Collectors.toList());

    return PedidoDetalhadoResponse.builder()
            .id(p.getId().getValue().toString())
            .pacienteId(p.getPacienteId().getValue().toString())
            .medicoSolicitante(p.getMedicoSolicitante().getNome())
            .medicoSolicitanteCrm(p.getMedicoSolicitante().getCrm())
            .medicoExecutor(p.temMedicoExecutor() ? p.getMedicoExecutor().getNome() : null)
            .procedimento(p.getProcedimento().getDescricao())
            .procedimentoCodigo(p.getProcedimento().getCodigoTUSS())
            .convenio(p.getConvenio().getNome())
            .convenioNumeroCarteira(p.getConvenio().getNumeroCarteira())
            .cid(p.getCid() != null ? p.getCid().getCodigo() : null)
            .lateralidade(p.getLateralidade().getTipo().name())
            .prioridade(p.getPrioridade().getTipo().name())
            .status(p.getStatus().getTipo().name())
            .dataPedido(p.getDataPedido())
            .criadoEm(p.getCriadoEm())
            .usuarioCriacao(p.getUsuarioCriacao())
            .atualizadoEm(p.getAtualizadoEm())
            .usuarioAtualizacao(p.getUsuarioAtualizacao())
            .agendadoPara(p.temAgendamento() ? p.getAgendamento().getDataHora() : null)
            .observacoes(p.getObservacoes())
            .documentosAnexados(p.getDocumentosAnexados())
            .consultaPreDataHora(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getDataHora() : null)
            .consultaPreCuidados(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getCuidados() : null)
            .consultaPreObservacoesEspeciais(p.getConsultaPreOperatoria() != null ? p.getConsultaPreOperatoria().getObservacoesEspeciais() : null)
            .statusAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getStatus() != null ?
                    p.getDadosAutorizacao().getStatus().getValor() : null)
            .numeroGuiaAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getNumeroGuia() != null ?
                    p.getDadosAutorizacao().getNumeroGuia().getValor() : null)
            .senhaAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getSenha() != null ?
                    p.getDadosAutorizacao().getSenha().getValor() : null)
            .validadeAutorizacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getValidade() != null ?
                    p.getDadosAutorizacao().getValidade().getValor() : null)
            .tipoAcomodacao(p.getDadosAutorizacao() != null && p.getDadosAutorizacao().getTipoAcomodacao() != null ?
                    p.getDadosAutorizacao().getTipoAcomodacao().getValor() : null)
            .opmeItens(opmeItens)
            .build();
  }

  private List<PedidoResponse.ProcedimentoResponse> mapProcedimentos(List<Procedimento> procedimentos) {
    if (procedimentos == null || procedimentos.isEmpty()) return null;
    return procedimentos.stream()
            .map(this::mapProcedimento)
            .collect(Collectors.toList());
  }

  private PedidoResponse.ProcedimentoResponse mapProcedimento(Procedimento p) {
    if (p == null) return null;
    return PedidoResponse.ProcedimentoResponse.builder()
            .codigoTUSS(p.getCodigoTUSS())
            .descricao(p.getDescricao())
            .categoria(p.getCategoria())
            .build();
  }
}