package br.inatel.painel.servicos.fakes;

import br.inatel.painel.servicos.IServicoPokemon;
import br.inatel.painel.servicos.ServicoIndisponivelException;

import java.util.List;
import java.util.Map;

public class FakePokeAPI implements IServicoPokemon {

    private static final Map<Integer, DadosPokemon> POKEMON = Map.ofEntries(
            pokemon(1, "bulbasaur", "grass", "poison"),
            pokemon(4, "charmander", "fire"),
            pokemon(7, "squirtle", "water"),
            pokemon(25, "pikachu", "electric"),
            pokemon(39, "jigglypuff", "normal", "fairy"),
            pokemon(52, "meowth", "normal"),
            pokemon(54, "psyduck", "water"),
            pokemon(63, "abra", "psychic"),
            pokemon(66, "machop", "fighting"),
            pokemon(74, "geodude", "rock", "ground"),
            pokemon(92, "gastly", "ghost", "poison"),
            pokemon(129, "magikarp", "water"),
            pokemon(133, "eevee", "normal"),
            pokemon(143, "snorlax", "normal"),
            pokemon(147, "dratini", "dragon"));

    private static Map.Entry<Integer, DadosPokemon> pokemon(int id, String nome, String... tipos) {
        String imagem = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
                + id + ".png";
        return Map.entry(id, new DadosPokemon(id, nome, List.of(tipos), imagem));
    }

    @Override
    public DadosPokemon buscarPokemon(int id) {
        DadosPokemon dados = POKEMON.get(id);
        if (dados == null) throw new ServicoIndisponivelException("PokéAPI");
        return dados;
    }
}
