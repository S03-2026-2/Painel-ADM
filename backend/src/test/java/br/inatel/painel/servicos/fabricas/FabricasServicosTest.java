package br.inatel.painel.servicos.fabricas;

import br.inatel.painel.entidades.Perfil;
import br.inatel.painel.servicos.clientes.ClienteServicoCartas;
import br.inatel.painel.servicos.clientes.ClienteServicoJogadores;
import br.inatel.painel.servicos.clientes.ClienteServicoTrocas;
import br.inatel.painel.servicos.fakes.FakePokeAPI;
import br.inatel.painel.servicos.montagem.CatalogoPokemon;
import br.inatel.painel.servicos.montagem.ConversorRespostas;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class FabricasServicosTest {

    private final ConversorRespostas conversor = new ConversorRespostas(new CatalogoPokemon(new FakePokeAPI()));

    @Test
    void fabricaDeFakesCriaUmaFamiliaQueFuncionaJunta() {
        FabricaServicos fabrica = new FabricaFakes(conversor);

        assertEquals(Perfil.ADMIN, fabrica.criarAutenticacao().autenticar("admin", "admin").getPerfil());
        assertFalse(fabrica.criarServicoJogadores().buscarTodos().isEmpty());
        assertFalse(fabrica.criarServicoCartas().buscarTodas().isEmpty());
        assertFalse(fabrica.criarServicoTrocas().buscarTrocas(null).isEmpty());
    }

    @Test
    void fabricaDeClientesReaisCriaSoClientesHttp() {
        FabricaServicos fabrica = new FabricaClientesReais(() -> "token",
                new FabricaClientesReais.Urls("http://jogadores", "http://cartas", "http://trocas"), conversor);

        assertInstanceOf(ClienteServicoJogadores.class, fabrica.criarAutenticacao());
        assertInstanceOf(ClienteServicoJogadores.class, fabrica.criarServicoJogadores());
        assertInstanceOf(ClienteServicoCartas.class, fabrica.criarServicoCartas());
        assertInstanceOf(ClienteServicoTrocas.class, fabrica.criarServicoTrocas());
    }
}
