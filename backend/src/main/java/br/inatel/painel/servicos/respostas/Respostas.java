package br.inatel.painel.servicos.respostas;

import java.util.List;

public final class Respostas {

    private Respostas() {
    }

    public record RespostaSessao(String token, String jogadorId, String perfil) {
    }

    public record RespostaJogador(String id, String nick, String email, String dataCadastro) {
    }

    public record RespostaCarta(String id, String jogadorId, int pokemonId, String dataRecebimento) {
    }

    public record RespostaCartaResumida(String id, int pokemonId) {
    }

    public record RespostaProposta(String id, String trocaId, String proponenteId,
                                   RespostaCartaResumida cartaProposta, String status, String dataProposta) {
    }

    public record RespostaTroca(String id, String ofertanteId, RespostaCartaResumida cartaOferecida,
                                String status, String dataAbertura, String dataFinalizacao,
                                List<RespostaProposta> propostas) {
    }
}
