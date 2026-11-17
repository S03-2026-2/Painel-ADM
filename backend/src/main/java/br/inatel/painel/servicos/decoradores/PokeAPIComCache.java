package br.inatel.painel.servicos.decoradores;

import br.inatel.painel.servicos.IServicoPokemon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PokeAPIComCache implements IServicoPokemon {

    private final IServicoPokemon servico;
    private final Map<Integer, DadosPokemon> cache = new ConcurrentHashMap<>();

    public PokeAPIComCache(IServicoPokemon servico) {
        this.servico = servico;
    }

    @Override
    public DadosPokemon buscarPokemon(int id) {
        DadosPokemon emCache = cache.get(id);
        if (emCache != null) return emCache;
        DadosPokemon dados = servico.buscarPokemon(id);
        cache.put(id, dados);
        return dados;
    }
}
