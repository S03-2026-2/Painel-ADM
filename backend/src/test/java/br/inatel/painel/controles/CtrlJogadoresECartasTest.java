package br.inatel.painel.controles;

import br.inatel.painel.Cenario;
import br.inatel.painel.controles.CtrlJogadores.JogadorComCartas;
import br.inatel.painel.entidades.Carta;
import br.inatel.painel.entidades.Jogador;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CtrlJogadoresECartasTest {

    private final Cenario cenario = new Cenario();
    private final CtrlJogadores ctrlJogadores = new CtrlJogadores(cenario.jogadores, cenario.cartas);

    @Test
    void contaAsCartasDeCadaJogador() {
        List<JogadorComCartas> lista = ctrlJogadores.listarComQuantidadeDeCartas();

        assertEquals(3, lista.size());
        lista.forEach(j -> assertEquals(5, j.quantidadeCartas()));
        assertEquals(1, cenario.chamadasCartas);
    }

    @Test
    void agrupaAsCartasPorJogadorComUmaUnicaConsulta() {
        CtrlCartasJogadores ctrl = new CtrlCartasJogadores(ctrlJogadores, cenario.cartas);

        Map<Jogador, List<Carta>> porJogador = ctrl.listarCartasPorJogador();

        assertEquals(3, porJogador.size());
        porJogador.values().forEach(cartas -> assertEquals(5, cartas.size()));
        assertEquals(1, cenario.chamadasCartas, "uma consulta de cartas, não uma por jogador");
        assertEquals(1, cenario.chamadasJogadores);
    }

    @Test
    void cartasVemCompletasComOsDadosDoPokemonEmOrdemDeNumero() {
        CtrlCartasJogadores ctrl = new CtrlCartasJogadores(ctrlJogadores, cenario.cartas);

        List<Carta> doAsh = ctrl.listarCartasPorJogador().entrySet().stream()
                .filter(e -> e.getKey().getNick().equals("ashketchum"))
                .findFirst().orElseThrow().getValue();

        assertEquals(List.of(7, 25, 63, 74, 129), doAsh.stream().map(Carta::getPokemonId).toList());
        Carta pikachu = doAsh.get(1);
        assertEquals("pikachu", pikachu.getNome());
        assertEquals(List.of("electric"), pikachu.getTipos());
        assertTrue(pikachu.getImagemUrl().endsWith("/25.png"));
    }
}
