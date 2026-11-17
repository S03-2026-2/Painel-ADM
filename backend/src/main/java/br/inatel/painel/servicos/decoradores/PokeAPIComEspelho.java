package br.inatel.painel.servicos.decoradores;

import br.inatel.painel.servicos.IServicoPokemon;
import br.inatel.painel.servicos.ServicoIndisponivelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PokeAPIComEspelho implements IServicoPokemon {

    private static final Logger LOG = LoggerFactory.getLogger(PokeAPIComEspelho.class);

    private final IServicoPokemon principal;
    private final IServicoPokemon reserva;
    private volatile boolean avisou;

    public PokeAPIComEspelho(IServicoPokemon principal, IServicoPokemon reserva) {
        this.principal = principal;
        this.reserva = reserva;
    }

    @Override
    public DadosPokemon buscarPokemon(int id) {
        try {
            return principal.buscarPokemon(id);
        } catch (ServicoIndisponivelException erro) {
            if (!avisou) {
                avisou = true;
                LOG.warn("PokéAPI indisponível. Usando o espelho da PokéAPI no GitHub.");
            }
            return reserva.buscarPokemon(id);
        }
    }
}
