package br.inatel.painel.servicos.clientes;

import br.inatel.painel.entidades.Jogador;
import br.inatel.painel.entidades.Sessao;
import br.inatel.painel.servicos.IAutenticacao;
import br.inatel.painel.servicos.IServicoJogadores;
import br.inatel.painel.servicos.montagem.ConversorRespostas;
import br.inatel.painel.servicos.respostas.Respostas.RespostaJogador;
import br.inatel.painel.servicos.respostas.Respostas.RespostaSessao;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClienteServicoJogadores implements IAutenticacao, IServicoJogadores {

    private static final String SERVICO = "Jogadores";

    private final ClienteHttp http;
    private final String urlBase;
    private final ConversorRespostas conversor;

    public ClienteServicoJogadores(ClienteHttp http, String urlBase, ConversorRespostas conversor) {
        this.http = http;
        this.urlBase = urlBase;
        this.conversor = conversor;
    }

    @Override
    public Sessao autenticar(String login, String senha) {
        RespostaSessao resposta = http.post(SERVICO, urlBase + "/auth/login",
                Map.of("login", login, "senha", senha), RespostaSessao.class);
        return conversor.sessao(resposta);
    }

    @Override
    public List<Jogador> buscarTodos() {
        List<RespostaJogador> resposta = http.get(SERVICO, urlBase + "/jogadores",
                new ParameterizedTypeReference<>() {
                });
        return conversor.jogadores(Objects.requireNonNullElse(resposta, List.of()));
    }
}
