package br.inatel.painel.servicos;

import br.inatel.painel.entidades.Sessao;

public interface IAutenticacao {

    Sessao autenticar(String login, String senha);
}
