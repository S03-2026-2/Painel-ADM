package br.inatel.painel.controles;

import br.inatel.painel.Cenario;
import br.inatel.painel.controles.CtrlTrocas.PropostaDetalhada;
import br.inatel.painel.controles.CtrlTrocas.TrocaAberta;
import br.inatel.painel.controles.CtrlTrocas.TrocaFinalizada;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CtrlTrocasTest {

    private final Cenario cenario = new Cenario();
    private final CtrlTrocas ctrl = new CtrlTrocas(cenario.trocas, cenario.jogadores);

    @Test
    void trocasEmAbertoVemComNickDoOfertanteECartaPronta() {
        List<TrocaAberta> abertas = ctrl.listarTrocasAbertas();

        assertEquals(2, abertas.size());
        TrocaAberta pikachu = abertas.get(0);
        assertEquals("ashketchum", pikachu.nickOfertante());
        assertEquals("pikachu", pikachu.cartaOferecida().getNome());
        assertEquals(2, pikachu.quantidadePropostas(), "propostas contadas dentro da troca (composição)");
        assertEquals(1, abertas.get(1).quantidadePropostas());
    }

    @Test
    void historicoMostraQuemRecebeuEACartaRecebida() {
        List<TrocaFinalizada> historico = ctrl.listarHistorico();

        assertEquals(1, historico.size());
        TrocaFinalizada troca = historico.get(0);
        assertEquals("misty", troca.nickOfertante());
        assertEquals("squirtle", troca.cartaOferecida().getNome());
        assertEquals("ashketchum", troca.nickReceptor());
        assertEquals("charmander", troca.cartaRecebida().getNome());
    }

    @Test
    void propostasVemComONickDoProponente() {
        List<PropostaDetalhada> propostas = ctrl.listarPropostas();

        assertEquals(4, propostas.size());
        assertEquals(List.of("ashketchum", "misty", "brock", "ashketchum"),
                propostas.stream().map(PropostaDetalhada::nickProponente).toList());
    }

    @Test
    void cadaTelaFazUmaConsultaAoServicoDeTrocasEUmaAoDeJogadores() {
        ctrl.listarTrocasAbertas();
        assertEquals(1, cenario.chamadasTrocas);
        assertEquals(1, cenario.chamadasJogadores);
    }
}
