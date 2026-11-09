package br.inatel.painel.controles;

import br.inatel.painel.entidades.Carta;
import br.inatel.painel.entidades.Jogador;
import br.inatel.painel.servicos.IServicoCartas;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CtrlCartasJogadores {

    private final CtrlJogadores ctrlJogadores;
    private final IServicoCartas servicoCartas;

    public CtrlCartasJogadores(CtrlJogadores ctrlJogadores, IServicoCartas servicoCartas) {
        this.ctrlJogadores = ctrlJogadores;
        this.servicoCartas = servicoCartas;
    }

    public Map<Jogador, List<Carta>> listarCartasPorJogador() {
        List<Jogador> jogadores = ctrlJogadores.listarJogadores();
        List<Carta> cartas = servicoCartas.buscarTodas();

        Map<Jogador, List<Carta>> porJogador = new LinkedHashMap<>();
        for (Jogador jogador : jogadores) {
            porJogador.put(jogador, cartas.stream()
                    .filter(c -> c.getDonoId().equals(jogador.getId()))
                    .sorted(Comparator.comparingInt(Carta::getPokemonId))
                    .toList());
        }
        return porJogador;
    }
}
