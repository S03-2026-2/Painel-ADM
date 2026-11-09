package br.inatel.painel.servicos.clientes;

import br.inatel.painel.entidades.Proposta;
import br.inatel.painel.entidades.StatusTroca;
import br.inatel.painel.entidades.Troca;
import br.inatel.painel.servicos.IServicoTrocas;
import br.inatel.painel.servicos.montagem.ConversorRespostas;
import br.inatel.painel.servicos.respostas.Respostas.RespostaProposta;
import br.inatel.painel.servicos.respostas.Respostas.RespostaTroca;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Objects;

public class ClienteServicoTrocas implements IServicoTrocas {

    private static final String SERVICO = "Trocas";

    private final ClienteHttp http;
    private final String urlBase;
    private final ConversorRespostas conversor;

    public ClienteServicoTrocas(ClienteHttp http, String urlBase, ConversorRespostas conversor) {
        this.http = http;
        this.urlBase = urlBase;
        this.conversor = conversor;
    }

    @Override
    public List<Troca> buscarTrocas(StatusTroca status) {
        String filtro = status != null ? "?status=" + status.name() : "";
        List<RespostaTroca> resposta = http.get(SERVICO, urlBase + "/trocas" + filtro,
                new ParameterizedTypeReference<>() {
                });
        return conversor.trocas(Objects.requireNonNullElse(resposta, List.of()));
    }

    @Override
    public List<Proposta> buscarPropostas() {
        List<RespostaProposta> resposta = http.get(SERVICO, urlBase + "/propostas",
                new ParameterizedTypeReference<>() {
                });
        return conversor.propostas(Objects.requireNonNullElse(resposta, List.of()));
    }
}
