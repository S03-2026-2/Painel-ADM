package br.inatel.painel.entidades;

public class Jogador {

    private final String id;
    private final String nick;
    private final String email;
    private final String dataCadastro;

    public Jogador(String id, String nick, String email, String dataCadastro) {
        this.id = id;
        this.nick = nick;
        this.email = email;
        this.dataCadastro = dataCadastro;
    }

    public String getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    public String getEmail() {
        return email;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }
}
