package br.inatel.painel.servicos.decoradores;

import br.inatel.painel.servicos.IServicoPokemon;
import br.inatel.painel.servicos.IServicoPokemon.DadosPokemon;
import br.inatel.painel.servicos.ServicoIndisponivelException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DecoradoresPokeAPITest {

    private static class ServicoContado implements IServicoPokemon {
        final AtomicInteger chamadas = new AtomicInteger();
        final String nome;
        boolean foraDoAr;

        ServicoContado(String nome) {
            this.nome = nome;
        }

        @Override
        public DadosPokemon buscarPokemon(int id) {
            chamadas.incrementAndGet();
            if (foraDoAr) throw new ServicoIndisponivelException(nome);
            return new DadosPokemon(id, nome + "-" + id, List.of("normal"), "");
        }
    }

    @Test
    void cacheBuscaCadaPokemonUmaVezSo() {
        ServicoContado pokeapi = new ServicoContado("pokeapi");
        IServicoPokemon comCache = new PokeAPIComCache(pokeapi);

        comCache.buscarPokemon(25);
        comCache.buscarPokemon(25);
        comCache.buscarPokemon(1);

        assertEquals(2, pokeapi.chamadas.get());
    }

    @Test
    void cacheNaoGuardaFalha() {
        ServicoContado pokeapi = new ServicoContado("pokeapi");
        pokeapi.foraDoAr = true;
        IServicoPokemon comCache = new PokeAPIComCache(pokeapi);

        assertThrows(ServicoIndisponivelException.class, () -> comCache.buscarPokemon(25));
        pokeapi.foraDoAr = false;

        assertEquals("pokeapi-25", comCache.buscarPokemon(25).nome());
    }

    @Test
    void espelhoSoEUsadoQuandoAPrincipalFalha() {
        ServicoContado principal = new ServicoContado("principal");
        ServicoContado espelho = new ServicoContado("espelho");
        IServicoPokemon servico = new PokeAPIComEspelho(principal, espelho);

        assertEquals("principal-25", servico.buscarPokemon(25).nome());
        assertEquals(0, espelho.chamadas.get());

        principal.foraDoAr = true;
        assertEquals("espelho-25", servico.buscarPokemon(25).nome());
    }

    @Test
    void seOsDoisFalharemOErroChegaNoCliente() {
        ServicoContado principal = new ServicoContado("principal");
        ServicoContado espelho = new ServicoContado("espelho");
        principal.foraDoAr = true;
        espelho.foraDoAr = true;

        IServicoPokemon servico = new PokeAPIComEspelho(principal, espelho);

        assertThrows(ServicoIndisponivelException.class, () -> servico.buscarPokemon(25));
    }

    @Test
    void decoradoresPodemSerEmpilhados() {
        ServicoContado principal = new ServicoContado("principal");
        ServicoContado espelho = new ServicoContado("espelho");
        principal.foraDoAr = true;
        IServicoPokemon servico = new PokeAPIComCache(new PokeAPIComEspelho(principal, espelho));

        servico.buscarPokemon(25);
        servico.buscarPokemon(25);

        assertEquals(1, principal.chamadas.get());
        assertEquals(1, espelho.chamadas.get());
    }
}
