package br.inatel.painel.servicos.clientes;

import br.inatel.painel.servicos.CredenciaisInvalidasException;
import br.inatel.painel.servicos.ServicoIndisponivelException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.function.Supplier;

public class ClienteHttp {

    private final RestClient rest = RestClient.create();
    private final Supplier<String> token;

    public ClienteHttp(Supplier<String> token) {
        this.token = token;
    }

    public <T> T get(String servico, String url, ParameterizedTypeReference<T> tipo) {
        return executar(servico, () -> rest.get()
                .uri(url)
                .headers(h -> {
                    String atual = token.get();
                    if (atual != null) h.setBearerAuth(atual);
                })
                .retrieve()
                .body(tipo));
    }

    public <T> T post(String servico, String url, Object corpo, Class<T> tipo) {
        return executar(servico, () -> rest.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(corpo)
                .retrieve()
                .body(tipo));
    }

    private static <T> T executar(String servico, Supplier<T> chamada) {
        try {
            return chamada.get();
        } catch (RestClientResponseException erro) {
            if (erro.getStatusCode().value() == 401) throw new CredenciaisInvalidasException();
            throw new ServicoIndisponivelException(servico);
        } catch (RestClientException erro) {
            throw new ServicoIndisponivelException(servico);
        }
    }
}
