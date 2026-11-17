package br.inatel.painel.servicos.fabricas;

import br.inatel.painel.servicos.IAutenticacao;
import br.inatel.painel.servicos.IServicoCartas;
import br.inatel.painel.servicos.IServicoJogadores;
import br.inatel.painel.servicos.IServicoTrocas;
import br.inatel.painel.servicos.fakes.FakeServicoCartas;
import br.inatel.painel.servicos.fakes.FakeServicoJogadores;
import br.inatel.painel.servicos.fakes.FakeServicoTrocas;
import br.inatel.painel.servicos.montagem.ConversorRespostas;

public class FabricaFakes implements FabricaServicos {

    private final ConversorRespostas conversor;

    public FabricaFakes(ConversorRespostas conversor) {
        this.conversor = conversor;
    }

    @Override
    public IAutenticacao criarAutenticacao() {
        return new FakeServicoJogadores(conversor);
    }

    @Override
    public IServicoJogadores criarServicoJogadores() {
        return new FakeServicoJogadores(conversor);
    }

    @Override
    public IServicoCartas criarServicoCartas() {
        return new FakeServicoCartas(conversor);
    }

    @Override
    public IServicoTrocas criarServicoTrocas() {
        return new FakeServicoTrocas(conversor);
    }
}
