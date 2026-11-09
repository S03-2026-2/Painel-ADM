package br.inatel.painel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"painel.usar-fakes=true", "painel.pokeapi.usar-fake=true"})
class PainelAdmApiApplicationTests {

    @Test
    void contextoSobe() {
    }
}
