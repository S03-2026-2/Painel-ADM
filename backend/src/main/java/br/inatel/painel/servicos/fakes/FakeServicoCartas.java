package br.inatel.painel.servicos.fakes;

import br.inatel.painel.entidades.Carta;
import br.inatel.painel.servicos.IServicoCartas;
import br.inatel.painel.servicos.montagem.ConversorRespostas;

import java.util.List;

public class FakeServicoCartas implements IServicoCartas {

    private final ConversorRespostas conversor;

    public FakeServicoCartas(ConversorRespostas conversor) {
        this.conversor = conversor;
    }

    @Override
    public List<Carta> buscarTodas() {
        return conversor.cartas(DadosExemplo.CARTAS);
    }
}
