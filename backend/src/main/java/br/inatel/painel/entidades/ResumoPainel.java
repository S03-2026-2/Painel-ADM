package br.inatel.painel.entidades;

public class ResumoPainel {

    private final Integer totalJogadores;
    private final Integer totalCartas;
    private final Integer trocasAbertas;
    private final Integer propostasPendentes;
    private final Integer trocasFinalizadas;

    public ResumoPainel(Integer totalJogadores, Integer totalCartas, Integer trocasAbertas,
                        Integer propostasPendentes, Integer trocasFinalizadas) {
        this.totalJogadores = totalJogadores;
        this.totalCartas = totalCartas;
        this.trocasAbertas = trocasAbertas;
        this.propostasPendentes = propostasPendentes;
        this.trocasFinalizadas = trocasFinalizadas;
    }

    public Integer getTotalJogadores() {
        return totalJogadores;
    }

    public Integer getTotalCartas() {
        return totalCartas;
    }

    public Integer getTrocasAbertas() {
        return trocasAbertas;
    }

    public Integer getPropostasPendentes() {
        return propostasPendentes;
    }

    public Integer getTrocasFinalizadas() {
        return trocasFinalizadas;
    }
}
