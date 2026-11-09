package br.inatel.painel.controles;

import br.inatel.painel.entidades.Carta;
import br.inatel.painel.entidades.Jogador;
import br.inatel.painel.entidades.Proposta;
import br.inatel.painel.entidades.StatusTroca;
import br.inatel.painel.entidades.Troca;
import br.inatel.painel.servicos.IServicoJogadores;
import br.inatel.painel.servicos.IServicoTrocas;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CtrlTrocas {

    public record TrocaAberta(Troca troca, String nickOfertante, Carta cartaOferecida,
                              int quantidadePropostas, Duration tempoEmAberto) {
    }

    public record PropostaDetalhada(Proposta proposta, String nickProponente, Carta cartaProposta) {
    }

    public record TrocaFinalizada(Troca troca, String nickOfertante, Carta cartaOferecida,
                                  String nickReceptor, Carta cartaRecebida) {
    }

    private final IServicoTrocas servicoTrocas;
    private final IServicoJogadores servicoJogadores;

    public CtrlTrocas(IServicoTrocas servicoTrocas, IServicoJogadores servicoJogadores) {
        this.servicoTrocas = servicoTrocas;
        this.servicoJogadores = servicoJogadores;
    }

    public List<TrocaAberta> listarTrocasAbertas() {
        List<Troca> trocas = servicoTrocas.buscarTrocas(StatusTroca.ABERTA);
        Function<String, String> nick = mapaDeNicks();
        Instant agora = Instant.now();
        return trocas.stream()
                .map(t -> new TrocaAberta(t, nick.apply(t.getOfertanteId()), t.getCartaOferecida(),
                        t.getPropostas().size(), t.tempoEmAberto(agora)))
                .toList();
    }

    public List<PropostaDetalhada> listarPropostas() {
        List<Proposta> propostas = servicoTrocas.buscarPropostas();
        Function<String, String> nick = mapaDeNicks();
        return propostas.stream()
                .map(p -> new PropostaDetalhada(p, nick.apply(p.getProponenteId()), p.getCartaProposta()))
                .toList();
    }

    public List<TrocaFinalizada> listarHistorico() {
        List<Troca> trocas = servicoTrocas.buscarTrocas(StatusTroca.FINALIZADA);
        Function<String, String> nick = mapaDeNicks();
        return trocas.stream()
                .sorted(Comparator.comparing((Troca t) -> Objects.requireNonNullElse(t.getDataFinalizacao(), ""))
                        .reversed())
                .map(t -> {
                    Proposta aceita = t.getPropostaAceita().orElse(null);
                    return new TrocaFinalizada(t, nick.apply(t.getOfertanteId()), t.getCartaOferecida(),
                            aceita != null ? nick.apply(aceita.getProponenteId()) : "—",
                            aceita != null ? aceita.getCartaProposta() : null);
                })
                .toList();
    }

    private Function<String, String> mapaDeNicks() {
        Map<String, String> nicks = servicoJogadores.buscarTodos().stream()
                .collect(Collectors.toMap(Jogador::getId, Jogador::getNick));
        return id -> nicks.getOrDefault(id, "(" + id + ")");
    }
}
