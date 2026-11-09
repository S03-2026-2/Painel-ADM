package br.inatel.painel.entidades;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrocaTest {

    private final Carta carta = new Carta("c-1", "j-1", 25, "pikachu", List.of("electric"), "", null);

    private Proposta proposta(String id, StatusProposta status) {
        return new Proposta(id, "t-1", "j-2", carta, status, "2026-10-01T10:00:00Z");
    }

    @Test
    void tempoEmAbertoDeUmaTrocaAbertaContaAteAgora() {
        Troca troca = new Troca("t-1", "j-1", carta, StatusTroca.ABERTA, "2026-10-01T10:00:00Z", null, List.of());
        Duration tempo = troca.tempoEmAberto(Instant.parse("2026-10-01T12:30:00Z"));
        assertEquals(Duration.ofMinutes(150), tempo);
    }

    @Test
    void tempoEmAbertoDeUmaTrocaFinalizadaParaNaFinalizacao() {
        Troca troca = new Troca("t-1", "j-1", carta, StatusTroca.FINALIZADA,
                "2026-10-01T10:00:00Z", "2026-10-01T11:00:00Z", List.of());
        assertEquals(Duration.ofHours(1), troca.tempoEmAberto(Instant.parse("2030-01-01T00:00:00Z")));
    }

    @Test
    void propostaAceitaEhAQueTemStatusAceita() {
        Troca troca = new Troca("t-1", "j-1", carta, StatusTroca.FINALIZADA, "2026-10-01T10:00:00Z",
                "2026-10-01T11:00:00Z",
                List.of(proposta("p-1", StatusProposta.RECUSADA), proposta("p-2", StatusProposta.ACEITA)));
        assertEquals("p-2", troca.getPropostaAceita().orElseThrow().getId());
    }

    @Test
    void trocaSemPropostaAceitaDevolveVazio() {
        Troca troca = new Troca("t-1", "j-1", carta, StatusTroca.ABERTA, "2026-10-01T10:00:00Z", null,
                List.of(proposta("p-1", StatusProposta.PENDENTE)));
        assertTrue(troca.getPropostaAceita().isEmpty());
    }

    @Test
    void propostasNaoPodemSerAlteradasPorFora() {
        Troca troca = new Troca("t-1", "j-1", carta, StatusTroca.ABERTA, "2026-10-01T10:00:00Z", null, List.of());
        assertThrows(UnsupportedOperationException.class,
                () -> troca.getPropostas().add(proposta("p-1", StatusProposta.PENDENTE)));
    }
}
