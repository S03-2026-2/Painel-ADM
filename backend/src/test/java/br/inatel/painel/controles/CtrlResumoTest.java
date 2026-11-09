package br.inatel.painel.controles;

import br.inatel.painel.Cenario;
import br.inatel.painel.entidades.ResumoPainel;
import br.inatel.painel.servicos.IServicoCartas;
import br.inatel.painel.servicos.ServicoIndisponivelException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CtrlResumoTest {

    private final Cenario cenario = new Cenario();

    @Test
    void calculaOsTotaisDaPlataforma() {
        ResumoPainel resumo = new CtrlResumo(cenario.jogadores, cenario.cartas, cenario.trocas).obterResumo();

        assertEquals(3, resumo.getTotalJogadores());
        assertEquals(15, resumo.getTotalCartas());
        assertEquals(2, resumo.getTrocasAbertas());
        assertEquals(2, resumo.getPropostasPendentes());
        assertEquals(1, resumo.getTrocasFinalizadas());
    }

    @Test
    void fazNoMaximoUmaConsultaPorServico() {
        new CtrlResumo(cenario.jogadores, cenario.cartas, cenario.trocas).obterResumo();

        assertEquals(1, cenario.chamadasJogadores);
        assertEquals(1, cenario.chamadasCartas);
        assertEquals(1, cenario.chamadasTrocas);
    }

    @Test
    void servicoForaDoArDeixaSoOTotalDeleIndisponivel() {
        IServicoCartas foraDoAr = () -> {
            throw new ServicoIndisponivelException("Cartas");
        };
        ResumoPainel resumo = new CtrlResumo(cenario.jogadores, foraDoAr, cenario.trocas).obterResumo();

        assertNull(resumo.getTotalCartas());
        assertEquals(3, resumo.getTotalJogadores());
        assertEquals(2, resumo.getTrocasAbertas());
    }
}
