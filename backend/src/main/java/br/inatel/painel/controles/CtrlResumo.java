package br.inatel.painel.controles;

import br.inatel.painel.entidades.ResumoPainel;
import br.inatel.painel.entidades.StatusProposta;
import br.inatel.painel.entidades.StatusTroca;
import br.inatel.painel.entidades.Troca;
import br.inatel.painel.servicos.IServicoCartas;
import br.inatel.painel.servicos.IServicoJogadores;
import br.inatel.painel.servicos.IServicoTrocas;
import br.inatel.painel.servicos.ServicoIndisponivelException;

import java.util.List;
import java.util.function.Supplier;

public class CtrlResumo {

    private final IServicoJogadores servicoJogadores;
    private final IServicoCartas servicoCartas;
    private final IServicoTrocas servicoTrocas;

    public CtrlResumo(IServicoJogadores servicoJogadores, IServicoCartas servicoCartas, IServicoTrocas servicoTrocas) {
        this.servicoJogadores = servicoJogadores;
        this.servicoCartas = servicoCartas;
        this.servicoTrocas = servicoTrocas;
    }

    public ResumoPainel obterResumo() {
        List<?> jogadores = tentar(servicoJogadores::buscarTodos);
        List<?> cartas = tentar(servicoCartas::buscarTodas);
        List<Troca> trocas = tentar(() -> servicoTrocas.buscarTrocas(null));

        return new ResumoPainel(
                jogadores != null ? jogadores.size() : null,
                cartas != null ? cartas.size() : null,
                trocas != null ? contar(trocas, StatusTroca.ABERTA) : null,
                trocas != null ? propostasPendentes(trocas) : null,
                trocas != null ? contar(trocas, StatusTroca.FINALIZADA) : null);
    }

    private static int contar(List<Troca> trocas, StatusTroca status) {
        return (int) trocas.stream().filter(t -> t.getStatus() == status).count();
    }

    private static int propostasPendentes(List<Troca> trocas) {
        return (int) trocas.stream()
                .flatMap(t -> t.getPropostas().stream())
                .filter(p -> p.getStatus() == StatusProposta.PENDENTE)
                .count();
    }

    private static <T> T tentar(Supplier<T> consulta) {
        try {
            return consulta.get();
        } catch (ServicoIndisponivelException erro) {
            return null;
        }
    }
}
