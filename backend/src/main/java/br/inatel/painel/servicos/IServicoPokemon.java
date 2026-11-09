package br.inatel.painel.servicos;

import java.util.List;

public interface IServicoPokemon {

    DadosPokemon buscarPokemon(int id);

    record DadosPokemon(int id, String nome, List<String> tipos, String imagemUrl) {
    }
}
