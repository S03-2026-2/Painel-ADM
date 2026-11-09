package br.inatel.painel.servicos.clientes;

import br.inatel.painel.servicos.IServicoPokemon;
import br.inatel.painel.servicos.ServicoIndisponivelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientePokeAPI implements IServicoPokemon {

    private static final Logger LOG = LoggerFactory.getLogger(ClientePokeAPI.class);
    private static final JsonMapper JSON = JsonMapper.builder().build();

    private final RestClient rest = RestClient.builder()
            .defaultHeader(HttpHeaders.USER_AGENT, "painel-adm/0.1 (projeto academico)")
            .build();
    private final Map<Integer, DadosPokemon> cache = new ConcurrentHashMap<>();
    private final String urlBase;
    private final String urlEspelho;
    private volatile boolean avisouEspelho;

    public ClientePokeAPI(String urlBase, String urlEspelho) {
        this.urlBase = urlBase;
        this.urlEspelho = urlEspelho;
    }

    @Override
    public DadosPokemon buscarPokemon(int id) {
        DadosPokemon emCache = cache.get(id);
        if (emCache != null) return emCache;
        DadosPokemon dados = requisitar(id);
        cache.put(id, dados);
        return dados;
    }

    private DadosPokemon requisitar(int id) {
        String url = urlBase + "/pokemon/" + id;
        try {
            return converter(buscarJson(url));
        } catch (RestClientException erro) {
            if (urlEspelho == null || urlEspelho.isBlank()) {
                LOG.warn("Falha ao consultar a PokéAPI ({}): {}", url, causaRaiz(erro));
                throw new ServicoIndisponivelException("PokéAPI");
            }
            if (!avisouEspelho) {
                avisouEspelho = true;
                LOG.warn("PokéAPI indisponível ({}). Usando o espelho da PokéAPI no GitHub.", causaRaiz(erro));
            }
        }

        String espelho = urlEspelho + "/pokemon/" + id + "/index.json";
        try {
            return converter(buscarJson(espelho));
        } catch (RestClientException erro) {
            LOG.warn("Falha ao consultar o espelho da PokéAPI ({}): {}", espelho, causaRaiz(erro));
            throw new ServicoIndisponivelException("PokéAPI");
        }
    }

    private Map<String, Object> buscarJson(String url) {
        String texto = rest.get().uri(url).retrieve().body(String.class);
        if (texto == null || texto.isBlank()) throw new RestClientException("resposta vazia de " + url);
        try {
            return JSON.readValue(texto, new TypeReference<>() {
            });
        } catch (JacksonException erro) {
            throw new RestClientException("JSON inválido em " + url, erro);
        }
    }

    @SuppressWarnings("unchecked")
    private static DadosPokemon converter(Map<String, Object> json) {
        List<Map<String, Object>> tipos = (List<Map<String, Object>>) json.getOrDefault("types", List.of());
        List<String> nomesTipos = tipos.stream()
                .sorted(Comparator.comparingInt(t -> ((Number) t.get("slot")).intValue()))
                .map(t -> (String) ((Map<String, Object>) t.get("type")).get("name"))
                .toList();

        return new DadosPokemon(((Number) json.get("id")).intValue(), (String) json.get("name"),
                nomesTipos, imagem((Map<String, Object>) json.get("sprites")));
    }

    private static String imagem(Map<String, Object> sprites) {
        if (sprites == null) return "";
        if (sprites.get("other") instanceof Map<?, ?> outras
                && outras.get("official-artwork") instanceof Map<?, ?> arte
                && arte.get("front_default") instanceof String url) {
            return url;
        }
        return sprites.get("front_default") instanceof String url ? url : "";
    }

    private static String causaRaiz(Throwable erro) {
        Throwable causa = erro;
        while (causa.getCause() != null && causa.getCause() != causa) causa = causa.getCause();
        return causa.getClass().getSimpleName() + ": " + causa.getMessage();
    }
}
