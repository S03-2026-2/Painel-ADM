package br.inatel.painel.servicos.clientes;

import br.inatel.painel.entidades.Carta;
import br.inatel.painel.servicos.IServicoCartas;
import br.inatel.painel.servicos.montagem.ConversorRespostas;
import br.inatel.painel.servicos.respostas.Respostas.RespostaCarta;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Objects;

public class ClienteServicoCartas implements IServicoCartas {

    private final ClienteHttp http;
    private final String urlBase;
    private final ConversorRespostas conversor;

    public ClienteServicoCartas(ClienteHttp http, String urlBase, ConversorRespostas conversor) {
        this.http = http;
        this.urlBase = urlBase;
        this.conversor = conversor;
    }

    @Override
    public List<Carta> buscarTodas() {
        List<RespostaCarta> resposta = http.get("Cartas", urlBase + "/cartas",
                new ParameterizedTypeReference<>() {
                });
        return conversor.cartas(Objects.requireNonNullElse(resposta, List.of()));
    }
}
