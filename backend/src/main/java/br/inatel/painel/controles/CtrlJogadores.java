package br.inatel.painel.controles;

import br.inatel.painel.entidades.Carta;
import br.inatel.painel.entidades.Jogador;
import br.inatel.painel.servicos.IServicoCartas;
import br.inatel.painel.servicos.IServicoJogadores;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CtrlJogadores {

    public record JogadorComCartas(Jogador jogador, int quantidadeCartas) {
    }

    private final IServicoJogadores servicoJogadores;
    private final IServicoCartas servicoCartas;

    public CtrlJogadores(IServicoJogadores servicoJogadores, IServicoCartas servicoCartas) {
        this.servicoJogadores = servicoJogadores;
        this.servicoCartas = servicoCartas;
    }

    public List<Jogador> listarJogadores() {
        return servicoJogadores.buscarTodos();
    }

    public List<JogadorComCartas> listarComQuantidadeDeCartas() {
        List<Jogador> jogadores = listarJogadores();
        Map<String, Long> contagem = servicoCartas.buscarTodas().stream()
                .collect(Collectors.groupingBy(Carta::getDonoId, Collectors.counting()));
        return jogadores.stream()
                .map(j -> new JogadorComCartas(j, contagem.getOrDefault(j.getId(), 0L).intValue()))
                .toList();
    }
}
