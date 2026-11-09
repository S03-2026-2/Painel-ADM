package br.inatel.painel.config;

import br.inatel.painel.api.TokenDaRequisicao;
import br.inatel.painel.controles.CtrlAutenticacao;
import br.inatel.painel.controles.CtrlCartasJogadores;
import br.inatel.painel.controles.CtrlJogadores;
import br.inatel.painel.controles.CtrlResumo;
import br.inatel.painel.controles.CtrlTrocas;
import br.inatel.painel.servicos.IAutenticacao;
import br.inatel.painel.servicos.IServicoCartas;
import br.inatel.painel.servicos.IServicoJogadores;
import br.inatel.painel.servicos.IServicoPokemon;
import br.inatel.painel.servicos.IServicoTrocas;
import br.inatel.painel.servicos.clientes.ClienteHttp;
import br.inatel.painel.servicos.clientes.ClientePokeAPI;
import br.inatel.painel.servicos.clientes.ClienteServicoCartas;
import br.inatel.painel.servicos.clientes.ClienteServicoJogadores;
import br.inatel.painel.servicos.clientes.ClienteServicoTrocas;
import br.inatel.painel.servicos.fakes.FakePokeAPI;
import br.inatel.painel.servicos.fakes.FakeServicoCartas;
import br.inatel.painel.servicos.fakes.FakeServicoJogadores;
import br.inatel.painel.servicos.fakes.FakeServicoTrocas;
import br.inatel.painel.servicos.montagem.CatalogoPokemon;
import br.inatel.painel.servicos.montagem.ConversorRespostas;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicosConfig {

    public record Servicos(IAutenticacao autenticacao, IServicoJogadores jogadores,
                           IServicoCartas cartas, IServicoTrocas trocas) {
    }

    @Bean
    public IServicoPokemon pokeapi(@Value("${painel.pokeapi.usar-fake}") boolean usarFake,
                                   @Value("${painel.url.pokeapi}") String url,
                                   @Value("${painel.url.pokeapi-espelho:}") String urlEspelho) {
        return usarFake ? new FakePokeAPI() : new ClientePokeAPI(url, urlEspelho);
    }

    @Bean
    public ConversorRespostas conversorRespostas(IServicoPokemon pokeapi) {
        return new ConversorRespostas(new CatalogoPokemon(pokeapi));
    }

    @Bean
    public Servicos servicos(ConversorRespostas conversor,
                             @Value("${painel.usar-fakes}") boolean usarFakes,
                             @Value("${painel.url.jogadores}") String urlJogadores,
                             @Value("${painel.url.cartas}") String urlCartas,
                             @Value("${painel.url.trocas}") String urlTrocas) {
        if (usarFakes) {
            FakeServicoJogadores jogadores = new FakeServicoJogadores(conversor);
            return new Servicos(jogadores, jogadores, new FakeServicoCartas(conversor),
                    new FakeServicoTrocas(conversor));
        }
        ClienteHttp http = new ClienteHttp(TokenDaRequisicao::atual);
        ClienteServicoJogadores jogadores = new ClienteServicoJogadores(http, urlJogadores, conversor);
        return new Servicos(jogadores, jogadores, new ClienteServicoCartas(http, urlCartas, conversor),
                new ClienteServicoTrocas(http, urlTrocas, conversor));
    }

    @Bean
    public CtrlAutenticacao ctrlAutenticacao(Servicos s) {
        return new CtrlAutenticacao(s.autenticacao());
    }

    @Bean
    public CtrlResumo ctrlResumo(Servicos s) {
        return new CtrlResumo(s.jogadores(), s.cartas(), s.trocas());
    }

    @Bean
    public CtrlJogadores ctrlJogadores(Servicos s) {
        return new CtrlJogadores(s.jogadores(), s.cartas());
    }

    @Bean
    public CtrlCartasJogadores ctrlCartasJogadores(CtrlJogadores ctrlJogadores, Servicos s) {
        return new CtrlCartasJogadores(ctrlJogadores, s.cartas());
    }

    @Bean
    public CtrlTrocas ctrlTrocas(Servicos s) {
        return new CtrlTrocas(s.trocas(), s.jogadores());
    }
}
