package br.inatel.painel.servicos.montagem;

import br.inatel.painel.entidades.Carta;
import br.inatel.painel.entidades.Jogador;
import br.inatel.painel.entidades.Perfil;
import br.inatel.painel.entidades.Proposta;
import br.inatel.painel.entidades.Sessao;
import br.inatel.painel.entidades.StatusProposta;
import br.inatel.painel.entidades.StatusTroca;
import br.inatel.painel.entidades.Troca;
import br.inatel.painel.servicos.IServicoPokemon.DadosPokemon;
import br.inatel.painel.servicos.respostas.Respostas.RespostaCarta;
import br.inatel.painel.servicos.respostas.Respostas.RespostaJogador;
import br.inatel.painel.servicos.respostas.Respostas.RespostaProposta;
import br.inatel.painel.servicos.respostas.Respostas.RespostaSessao;
import br.inatel.painel.servicos.respostas.Respostas.RespostaTroca;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class ConversorRespostas {

    private final CatalogoPokemon catalogo;

    public ConversorRespostas(CatalogoPokemon catalogo) {
        this.catalogo = catalogo;
    }

    public Sessao sessao(RespostaSessao r) {
        return new Sessao(r.token(), r.jogadorId(), Perfil.valueOf(r.perfil()));
    }

    public List<Jogador> jogadores(List<RespostaJogador> respostas) {
        return respostas.stream()
                .map(r -> new Jogador(r.id(), r.nick(), r.email(), r.dataCadastro()))
                .toList();
    }

    public List<Carta> cartas(List<RespostaCarta> respostas) {
        IntFunction<DadosPokemon> pokemon = catalogo.carregar(respostas.stream().map(RespostaCarta::pokemonId).toList());
        return respostas.stream()
                .map(r -> carta(r.id(), r.jogadorId(), pokemon.apply(r.pokemonId()), r.dataRecebimento()))
                .toList();
    }

    public List<Proposta> propostas(List<RespostaProposta> respostas) {
        IntFunction<DadosPokemon> pokemon = catalogo.carregar(
                respostas.stream().map(r -> r.cartaProposta().pokemonId()).toList());
        return respostas.stream().map(r -> proposta(r, pokemon)).toList();
    }

    public List<Troca> trocas(List<RespostaTroca> respostas) {
        List<Integer> ids = new ArrayList<>();
        for (RespostaTroca t : respostas) {
            ids.add(t.cartaOferecida().pokemonId());
            propostasDe(t).forEach(p -> ids.add(p.cartaProposta().pokemonId()));
        }
        IntFunction<DadosPokemon> pokemon = catalogo.carregar(ids);
        return respostas.stream()
                .map(t -> new Troca(
                        t.id(),
                        t.ofertanteId(),
                        carta(t.cartaOferecida().id(), t.ofertanteId(), pokemon.apply(t.cartaOferecida().pokemonId()), null),
                        StatusTroca.valueOf(t.status()),
                        t.dataAbertura(),
                        t.dataFinalizacao(),
                        propostasDe(t).stream().map(p -> proposta(p, pokemon)).toList()))
                .toList();
    }

    private static List<RespostaProposta> propostasDe(RespostaTroca troca) {
        return troca.propostas() != null ? troca.propostas() : List.of();
    }

    private static Proposta proposta(RespostaProposta r, IntFunction<DadosPokemon> pokemon) {
        return new Proposta(
                r.id(),
                r.trocaId(),
                r.proponenteId(),
                carta(r.cartaProposta().id(), r.proponenteId(), pokemon.apply(r.cartaProposta().pokemonId()), null),
                StatusProposta.valueOf(r.status()),
                r.dataProposta());
    }

    private static Carta carta(String id, String donoId, DadosPokemon pokemon, String dataRecebimento) {
        return new Carta(id, donoId, pokemon.id(), pokemon.nome(), pokemon.tipos(), pokemon.imagemUrl(), dataRecebimento);
    }
}
