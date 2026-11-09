package br.inatel.painel.servicos.fakes;

import br.inatel.painel.servicos.respostas.Respostas.RespostaCarta;
import br.inatel.painel.servicos.respostas.Respostas.RespostaCartaResumida;
import br.inatel.painel.servicos.respostas.Respostas.RespostaJogador;
import br.inatel.painel.servicos.respostas.Respostas.RespostaProposta;
import br.inatel.painel.servicos.respostas.Respostas.RespostaTroca;

import java.util.List;

public final class DadosExemplo {

    private DadosExemplo() {
    }

    public static final List<RespostaJogador> JOGADORES = List.of(
            new RespostaJogador("j-001", "ashketchum", "ash@exemplo.com", "2026-09-10T12:00:00Z"),
            new RespostaJogador("j-002", "misty", "misty@exemplo.com", "2026-09-11T09:30:00Z"),
            new RespostaJogador("j-003", "brock", "brock@exemplo.com", "2026-09-12T15:45:00Z"));

    public static final List<RespostaCarta> CARTAS = List.of(
            new RespostaCarta("c-101", "j-001", 25, "2026-09-10T12:00:05Z"),
            new RespostaCarta("c-140", "j-001", 7, "2026-09-28T15:42:00Z"),
            new RespostaCarta("c-103", "j-001", 63, "2026-09-10T12:00:05Z"),
            new RespostaCarta("c-104", "j-001", 129, "2026-09-10T12:00:05Z"),
            new RespostaCarta("c-105", "j-001", 74, "2026-09-10T12:00:05Z"),
            new RespostaCarta("c-102", "j-002", 4, "2026-09-28T15:42:00Z"),
            new RespostaCarta("c-141", "j-002", 1, "2026-09-11T09:30:05Z"),
            new RespostaCarta("c-142", "j-002", 54, "2026-09-11T09:30:05Z"),
            new RespostaCarta("c-143", "j-002", 39, "2026-09-11T09:30:05Z"),
            new RespostaCarta("c-144", "j-002", 52, "2026-09-11T09:30:05Z"),
            new RespostaCarta("c-180", "j-003", 143, "2026-09-12T15:45:05Z"),
            new RespostaCarta("c-181", "j-003", 133, "2026-09-12T15:45:05Z"),
            new RespostaCarta("c-182", "j-003", 66, "2026-09-12T15:45:05Z"),
            new RespostaCarta("c-183", "j-003", 92, "2026-09-12T15:45:05Z"),
            new RespostaCarta("c-184", "j-003", 147, "2026-09-12T15:45:05Z"));

    private static final RespostaProposta P900 = new RespostaProposta("p-900", "t-480", "j-001",
            new RespostaCartaResumida("c-102", 4), "ACEITA", "2026-09-28T12:10:00Z");
    private static final RespostaProposta P901 = new RespostaProposta("p-901", "t-500", "j-002",
            new RespostaCartaResumida("c-141", 1), "PENDENTE", "2026-09-30T19:10:00Z");
    private static final RespostaProposta P902 = new RespostaProposta("p-902", "t-500", "j-003",
            new RespostaCartaResumida("c-181", 133), "PENDENTE", "2026-10-01T08:45:00Z");
    private static final RespostaProposta P903 = new RespostaProposta("p-903", "t-501", "j-001",
            new RespostaCartaResumida("c-103", 63), "RECUSADA", "2026-10-02T11:00:00Z");

    public static final List<RespostaProposta> PROPOSTAS = List.of(P900, P901, P902, P903);

    public static final List<RespostaTroca> TROCAS = List.of(
            new RespostaTroca("t-500", "j-001", new RespostaCartaResumida("c-101", 25), "ABERTA",
                    "2026-09-30T18:00:00Z", null, List.of(P901, P902)),
            new RespostaTroca("t-501", "j-003", new RespostaCartaResumida("c-180", 143), "ABERTA",
                    "2026-10-02T10:20:00Z", null, List.of(P903)),
            new RespostaTroca("t-480", "j-002", new RespostaCartaResumida("c-140", 7), "FINALIZADA",
                    "2026-09-28T10:00:00Z", "2026-09-28T15:42:00Z", List.of(P900)));
}
