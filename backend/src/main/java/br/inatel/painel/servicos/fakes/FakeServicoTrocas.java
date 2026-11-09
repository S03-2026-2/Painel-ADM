package br.inatel.painel.servicos.fakes;

import br.inatel.painel.entidades.Proposta;
import br.inatel.painel.entidades.StatusTroca;
import br.inatel.painel.entidades.Troca;
import br.inatel.painel.servicos.IServicoTrocas;
import br.inatel.painel.servicos.montagem.ConversorRespostas;

import java.util.List;

public class FakeServicoTrocas implements IServicoTrocas {

    private final ConversorRespostas conversor;

    public FakeServicoTrocas(ConversorRespostas conversor) {
        this.conversor = conversor;
    }

    @Override
    public List<Troca> buscarTrocas(StatusTroca status) {
        return conversor.trocas(DadosExemplo.TROCAS.stream()
                .filter(t -> status == null || t.status().equals(status.name()))
                .toList());
    }

    @Override
    public List<Proposta> buscarPropostas() {
        return conversor.propostas(DadosExemplo.PROPOSTAS);
    }
}
