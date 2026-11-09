package br.inatel.painel.servicos.montagem;

import br.inatel.painel.servicos.IServicoPokemon;
import br.inatel.painel.servicos.IServicoPokemon.DadosPokemon;
import br.inatel.painel.servicos.ServicoIndisponivelException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogoPokemonTest {

    @Test
    void buscaCadaPokemonDiferenteUmaVezSo() {
        AtomicInteger chamadas = new AtomicInteger();
        IServicoPokemon pokeapi = id -> {
            chamadas.incrementAndGet();
            return new DadosPokemon(id, "pokemon-" + id, List.of("normal"), "");
        };

        new CatalogoPokemon(pokeapi).carregar(List.of(25, 25, 1, 25, 1));

        assertEquals(2, chamadas.get());
    }

    @Test
    void seAPokeApiFalharACartaApareceComoPokemonN() {
        IServicoPokemon foraDoAr = id -> {
            throw new ServicoIndisponivelException("PokéAPI");
        };

        IntFunction<DadosPokemon> pokemon = new CatalogoPokemon(foraDoAr).carregar(List.of(25));

        assertEquals("Pokémon 25", pokemon.apply(25).nome());
        assertTrue(pokemon.apply(25).tipos().isEmpty());
    }
}
