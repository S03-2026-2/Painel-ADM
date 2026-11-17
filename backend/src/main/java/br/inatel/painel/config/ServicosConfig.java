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
import br.inatel.painel.servicos.clientes.ClientePokeAPI;
import br.inatel.painel.servicos.decoradores.PokeAPIComCache;
import br.inatel.painel.servicos.decoradores.PokeAPIComEspelho;
import br.inatel.painel.servicos.fabricas.FabricaClientesReais;
import br.inatel.painel.servicos.fabricas.FabricaFakes;
import br.inatel.painel.servicos.fabricas.FabricaServicos;
import br.inatel.painel.servicos.fakes.FakePokeAPI;
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
        if (usarFake) return new FakePokeAPI();
        IServicoPokemon servico = new ClientePokeAPI("PokéAPI", url + "/pokemon/{id}");
        if (!urlEspelho.isBlank()) {
            servico = new PokeAPIComEspelho(servico,
                    new ClientePokeAPI("espelho da PokéAPI", urlEspelho + "/pokemon/{id}/index.json"));
        }
        return new PokeAPIComCache(servico);
    }

    @Bean
    public ConversorRespostas conversorRespostas(IServicoPokemon pokeapi) {
        return new ConversorRespostas(new CatalogoPokemon(pokeapi));
    }

    @Bean
    public FabricaServicos fabricaServicos(ConversorRespostas conversor,
                                           @Value("${painel.usar-fakes}") boolean usarFakes,
                                           @Value("${painel.url.jogadores}") String urlJogadores,
                                           @Value("${painel.url.cartas}") String urlCartas,
                                           @Value("${painel.url.trocas}") String urlTrocas) {
        if (usarFakes) return new FabricaFakes(conversor);
        return new FabricaClientesReais(TokenDaRequisicao::atual,
                new FabricaClientesReais.Urls(urlJogadores, urlCartas, urlTrocas), conversor);
    }

    @Bean
    public Servicos servicos(FabricaServicos fabrica) {
        return new Servicos(fabrica.criarAutenticacao(), fabrica.criarServicoJogadores(),
                fabrica.criarServicoCartas(), fabrica.criarServicoTrocas());
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
