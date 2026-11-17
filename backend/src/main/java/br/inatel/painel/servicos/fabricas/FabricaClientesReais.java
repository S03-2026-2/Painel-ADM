package br.inatel.painel.servicos.fabricas;

import br.inatel.painel.servicos.IAutenticacao;
import br.inatel.painel.servicos.IServicoCartas;
import br.inatel.painel.servicos.IServicoJogadores;
import br.inatel.painel.servicos.IServicoTrocas;
import br.inatel.painel.servicos.clientes.ClienteHttp;
import br.inatel.painel.servicos.clientes.ClienteServicoCartas;
import br.inatel.painel.servicos.clientes.ClienteServicoJogadores;
import br.inatel.painel.servicos.clientes.ClienteServicoTrocas;
import br.inatel.painel.servicos.montagem.ConversorRespostas;

import java.util.function.Supplier;

public class FabricaClientesReais implements FabricaServicos {

    public record Urls(String jogadores, String cartas, String trocas) {
    }

    private final ClienteHttp http;
    private final Urls urls;
    private final ConversorRespostas conversor;

    public FabricaClientesReais(Supplier<String> token, Urls urls, ConversorRespostas conversor) {
        this.http = new ClienteHttp(token);
        this.urls = urls;
        this.conversor = conversor;
    }

    @Override
    public IAutenticacao criarAutenticacao() {
        return new ClienteServicoJogadores(http, urls.jogadores(), conversor);
    }

    @Override
    public IServicoJogadores criarServicoJogadores() {
        return new ClienteServicoJogadores(http, urls.jogadores(), conversor);
    }

    @Override
    public IServicoCartas criarServicoCartas() {
        return new ClienteServicoCartas(http, urls.cartas(), conversor);
    }

    @Override
    public IServicoTrocas criarServicoTrocas() {
        return new ClienteServicoTrocas(http, urls.trocas(), conversor);
    }
}
