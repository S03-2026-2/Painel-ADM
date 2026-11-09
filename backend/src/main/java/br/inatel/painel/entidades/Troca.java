package br.inatel.painel.entidades;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class Troca {

    private final String id;
    private final String ofertanteId;
    private final Carta cartaOferecida;
    private final StatusTroca status;
    private final String dataAbertura;
    private final String dataFinalizacao;
    private final List<Proposta> propostas;

    public Troca(String id, String ofertanteId, Carta cartaOferecida, StatusTroca status,
                 String dataAbertura, String dataFinalizacao, List<Proposta> propostas) {
        this.id = id;
        this.ofertanteId = ofertanteId;
        this.cartaOferecida = cartaOferecida;
        this.status = status;
        this.dataAbertura = dataAbertura;
        this.dataFinalizacao = dataFinalizacao;
        this.propostas = List.copyOf(propostas);
    }

    public String getId() {
        return id;
    }

    public String getOfertanteId() {
        return ofertanteId;
    }

    public Carta getCartaOferecida() {
        return cartaOferecida;
    }

    public StatusTroca getStatus() {
        return status;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public String getDataFinalizacao() {
        return dataFinalizacao;
    }

    public List<Proposta> getPropostas() {
        return propostas;
    }

    public Optional<Proposta> getPropostaAceita() {
        return propostas.stream().filter(p -> p.getStatus() == StatusProposta.ACEITA).findFirst();
    }

    public Duration tempoEmAberto(Instant agora) {
        Instant fim = dataFinalizacao != null ? Instant.parse(dataFinalizacao) : agora;
        return Duration.between(Instant.parse(dataAbertura), fim);
    }
}
