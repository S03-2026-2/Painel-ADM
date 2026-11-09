package br.inatel.painel.api;

import br.inatel.painel.entidades.Carta;

import java.util.List;

public final class Dtos {

    private Dtos() {
    }

    public record LoginEntrada(String login, String senha) {
    }

    public record LoginSaida(String token) {
    }

    public record ErroSaida(String mensagem) {
    }

    public record CartaDto(String id, int pokemonId, String nome, List<String> tipos, String imagemUrl) {
        public static CartaDto de(Carta carta) {
            if (carta == null) return null;
            return new CartaDto(carta.getId(), carta.getPokemonId(), carta.getNome(), carta.getTipos(),
                    carta.getImagemUrl());
        }
    }

    public record ResumoDto(Integer totalJogadores, Integer totalCartas, Integer trocasAbertas,
                            Integer propostasPendentes, Integer trocasFinalizadas) {
    }

    public record JogadorDto(String id, String nick, String email, String dataCadastro, int quantidadeCartas) {
    }

    public record JogadorResumidoDto(String id, String nick) {
    }

    public record CartasDoJogadorDto(JogadorResumidoDto jogador, List<CartaDto> cartas) {
    }

    public record TrocaAbertaDto(String id, String nickOfertante, CartaDto cartaOferecida, String dataAbertura,
                                 long tempoEmAbertoMs, int quantidadePropostas) {
    }

    public record PropostaDto(String id, String trocaId, String nickProponente, CartaDto cartaProposta,
                              String status, String dataProposta) {
    }

    public record TrocaFinalizadaDto(String id, String dataFinalizacao, String nickOfertante,
                                     CartaDto cartaOferecida, String nickReceptor, CartaDto cartaRecebida) {
    }
}
