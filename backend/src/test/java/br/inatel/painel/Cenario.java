package br.inatel.painel;

import br.inatel.painel.entidades.Proposta;
import br.inatel.painel.entidades.StatusTroca;
import br.inatel.painel.entidades.Troca;
import br.inatel.painel.servicos.IServicoCartas;
import br.inatel.painel.servicos.IServicoJogadores;
import br.inatel.painel.servicos.IServicoTrocas;
import br.inatel.painel.servicos.fakes.FakePokeAPI;
import br.inatel.painel.servicos.fakes.FakeServicoCartas;
import br.inatel.painel.servicos.fakes.FakeServicoJogadores;
import br.inatel.painel.servicos.fakes.FakeServicoTrocas;
import br.inatel.painel.servicos.montagem.CatalogoPokemon;
import br.inatel.painel.servicos.montagem.ConversorRespostas;

import java.util.List;

public class Cenario {

    public final ConversorRespostas conversor = new ConversorRespostas(new CatalogoPokemon(new FakePokeAPI()));
    public final FakeServicoJogadores fakeJogadores = new FakeServicoJogadores(conversor);

    public int chamadasJogadores;
    public int chamadasCartas;
    public int chamadasTrocas;

    public final IServicoJogadores jogadores = () -> {
        chamadasJogadores++;
        return fakeJogadores.buscarTodos();
    };

    private final FakeServicoCartas fakeCartas = new FakeServicoCartas(conversor);
    public final IServicoCartas cartas = () -> {
        chamadasCartas++;
        return fakeCartas.buscarTodas();
    };

    private final FakeServicoTrocas fakeTrocas = new FakeServicoTrocas(conversor);
    public final IServicoTrocas trocas = new IServicoTrocas() {
        @Override
        public List<Troca> buscarTrocas(StatusTroca status) {
            chamadasTrocas++;
            return fakeTrocas.buscarTrocas(status);
        }

        @Override
        public List<Proposta> buscarPropostas() {
            chamadasTrocas++;
            return fakeTrocas.buscarPropostas();
        }
    };
}
