package br.inatel.painel.controles;

import br.inatel.painel.entidades.Perfil;
import br.inatel.painel.entidades.Sessao;
import br.inatel.painel.servicos.CredenciaisInvalidasException;
import br.inatel.painel.servicos.IAutenticacao;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CtrlAutenticacao {

    private final IAutenticacao autenticacao;
    private final Map<String, Sessao> sessoes = new ConcurrentHashMap<>();

    public CtrlAutenticacao(IAutenticacao autenticacao) {
        this.autenticacao = autenticacao;
    }

    public Optional<Sessao> autenticar(String login, String senha) {
        try {
            Sessao sessao = autenticacao.autenticar(login, senha);
            if (sessao.getPerfil() != Perfil.ADMIN) return Optional.empty();
            sessoes.put(sessao.getToken(), sessao);
            return Optional.of(sessao);
        } catch (CredenciaisInvalidasException erro) {
            return Optional.empty();
        }
    }

    public boolean sessaoValida(String token) {
        return token != null && sessoes.containsKey(token);
    }

    public void sair(String token) {
        if (token != null) sessoes.remove(token);
    }
}
