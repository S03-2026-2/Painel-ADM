package br.inatel.painel.servicos.fakes;

import br.inatel.painel.entidades.Jogador;
import br.inatel.painel.entidades.Sessao;
import br.inatel.painel.servicos.CredenciaisInvalidasException;
import br.inatel.painel.servicos.IAutenticacao;
import br.inatel.painel.servicos.IServicoJogadores;
import br.inatel.painel.servicos.montagem.ConversorRespostas;
import br.inatel.painel.servicos.respostas.Respostas.RespostaSessao;

import java.util.List;

public class FakeServicoJogadores implements IAutenticacao, IServicoJogadores {

    private final ConversorRespostas conversor;

    public FakeServicoJogadores(ConversorRespostas conversor) {
        this.conversor = conversor;
    }

    @Override
    public Sessao autenticar(String login, String senha) {
        if ("admin".equals(login) && "admin".equals(senha)) {
            return conversor.sessao(new RespostaSessao("token-fake-admin", "j-000", "ADMIN"));
        }
        if ("ash".equals(login) && "ash".equals(senha)) {
            return conversor.sessao(new RespostaSessao("token-fake-ash", "j-001", "JOGADOR"));
        }
        throw new CredenciaisInvalidasException();
    }

    @Override
    public List<Jogador> buscarTodos() {
        return conversor.jogadores(DadosExemplo.JOGADORES);
    }
}
