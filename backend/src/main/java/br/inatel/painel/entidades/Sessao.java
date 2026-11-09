package br.inatel.painel.entidades;

public class Sessao {

    private final String token;
    private final String jogadorId;
    private final Perfil perfil;

    public Sessao(String token, String jogadorId, Perfil perfil) {
        this.token = token;
        this.jogadorId = jogadorId;
        this.perfil = perfil;
    }

    public String getToken() {
        return token;
    }

    public String getJogadorId() {
        return jogadorId;
    }

    public Perfil getPerfil() {
        return perfil;
    }
}
