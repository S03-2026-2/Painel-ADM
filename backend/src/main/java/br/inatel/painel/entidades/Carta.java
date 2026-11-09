package br.inatel.painel.entidades;

import java.util.List;

public class Carta {

    private final String id;
    private final String donoId;
    private final int pokemonId;
    private final String nome;
    private final List<String> tipos;
    private final String imagemUrl;
    private final String dataRecebimento;

    public Carta(String id, String donoId, int pokemonId, String nome, List<String> tipos,
                 String imagemUrl, String dataRecebimento) {
        this.id = id;
        this.donoId = donoId;
        this.pokemonId = pokemonId;
        this.nome = nome;
        this.tipos = List.copyOf(tipos);
        this.imagemUrl = imagemUrl;
        this.dataRecebimento = dataRecebimento;
    }

    public String getId() {
        return id;
    }

    public String getDonoId() {
        return donoId;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public String getNome() {
        return nome;
    }

    public List<String> getTipos() {
        return tipos;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public String getDataRecebimento() {
        return dataRecebimento;
    }
}
