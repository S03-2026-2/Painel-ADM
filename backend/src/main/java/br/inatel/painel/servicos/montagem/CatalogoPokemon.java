package br.inatel.painel.servicos.montagem;

import br.inatel.painel.servicos.IServicoPokemon;
import br.inatel.painel.servicos.IServicoPokemon.DadosPokemon;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

public class CatalogoPokemon {

    private final IServicoPokemon pokeapi;

    public CatalogoPokemon(IServicoPokemon pokeapi) {
        this.pokeapi = pokeapi;
    }

    public IntFunction<DadosPokemon> carregar(Collection<Integer> ids) {
        Map<Integer, DadosPokemon> mapa = new HashMap<>();
        for (int id : new LinkedHashSet<>(ids)) {
            try {
                mapa.put(id, pokeapi.buscarPokemon(id));
            } catch (RuntimeException erro) {
                mapa.put(id, desconhecido(id));
            }
        }
        return id -> mapa.getOrDefault(id, desconhecido(id));
    }

    private static DadosPokemon desconhecido(int id) {
        return new DadosPokemon(id, "Pokémon " + id, List.of(), "");
    }
}
