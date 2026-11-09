# Painel de Administração – Backend (Spring Boot)

Backend do Painel de Administração. Fica entre a Interface Web (React) e os serviços dos grupos
de **Jogadores**, **Cartas** e **Trocas**: junta os dados, guarda a sessão do administrador e
entrega para as telas só o que elas precisam mostrar. O painel é **somente leitura**.

```
Interface Web (React)  →  /api  →  Backend do Painel  →  serviços de Jogadores, Cartas e Trocas
                                                      →  PokéAPI (nome, tipos e imagem)
```

## Como rodar

Precisa do **Java 21+** e do **Maven**. No IntelliJ, abra a pasta: ele reconhece o `pom.xml`.

```bash
mvn spring-boot:run
```

Sobe em `http://localhost:8080`. No modo de teste (padrão), usa serviços falsos com dados de
exemplo. Login: `admin` / `admin`. O login `ash` / `ash` é um jogador comum (acesso negado).

Para rodar os testes:

```bash
mvn test
```

## Configuração (`src/main/resources/application.properties`)

| Propriedade | Padrão | Para quê |
|---|---|---|
| `painel.usar-fakes` | `true` | `false` = chama os serviços reais dos outros grupos |
| `painel.url.jogadores` / `cartas` / `trocas` | `localhost:8081…8083` | URL de cada serviço |
| `painel.url.pokeapi` | `https://pokeapi.co/api/v2` | PokéAPI |
| `painel.url.pokeapi-espelho` | espelho da PokéAPI no GitHub | Usado quando a PokéAPI está bloqueada ou fora do ar (ex.: rede com firewall que bloqueia `pokeapi.co`) |
| `painel.pokeapi.usar-fake` | `false` | `true` = tabela local com os 15 Pokémon de exemplo (sem internet) |

## API para a Interface Web

Todas as rotas, menos o login, exigem o cabeçalho `Authorization: Bearer <token>`.

| Método e rota | Resposta |
|---|---|
| `POST /api/login` `{login, senha}` | `200 {token}` ou `401` (acesso negado) |
| `POST /api/logout` | `204` |
| `GET /api/resumo` | totais (um total `null` = serviço fora do ar) |
| `GET /api/jogadores` | jogadores com a quantidade de cartas |
| `GET /api/cartas` | cartas agrupadas por jogador |
| `GET /api/trocas/abertas` | trocas em aberto, com nick, carta, tempo e nº de propostas |
| `GET /api/propostas` | propostas com nick do proponente e carta |
| `GET /api/trocas/historico` | trocas finalizadas, com quem recebeu e a carta recebida |

Se um serviço dos outros grupos estiver fora do ar, a resposta é `503 {mensagem}`.

## Organização do código

| Pacote | Conteúdo |
|---|---|
| `api` | Endpoints REST (`AutenticacaoApi`, `PainelApi`), filtro de sessão e tratamento de erros |
| `controles` | `CtrlAutenticacao`, `CtrlResumo`, `CtrlJogadores`, `CtrlCartasJogadores` (reutiliza `CtrlJogadores`), `CtrlTrocas` |
| `servicos` | Interfaces `IAutenticacao`, `IServicoJogadores`, `IServicoCartas`, `IServicoTrocas`, `IServicoPokemon` |
| `servicos.clientes` | `ClienteServicoJogadores` (implementa `IAutenticacao` e `IServicoJogadores`), `ClienteServicoCartas`, `ClienteServicoTrocas`, `ClientePokeAPI` |
| `servicos.fakes` | Implementações falsas das mesmas interfaces, com dados de exemplo |
| `servicos.montagem` | `CatalogoPokemon` (busca na PokéAPI) e `ConversorRespostas` (JSON → entidades) |
| `entidades` | `Jogador`, `Carta`, `Troca`, `Proposta`, `Sessao`, `ResumoPainel` e enumerações |
| `config` | Único lugar que escolhe entre clientes reais e fakes e monta os controles |

**Princípios aplicados**
- **Ocultamento de informação:** entidades com atributos `private final` e só getters; listas
  imutáveis.
- **Composição:** cada `Troca` contém as suas `Proposta`s.
- **Responsabilidade única:** a API só traduz HTTP; os controles só organizam os dados; os
  clientes só falam com um serviço; `CatalogoPokemon` e `ConversorRespostas` separados.
- **Inversão de dependência:** os controles recebem só interfaces e não conhecem o Spring.
- **Segregação de interfaces:** login usa só `IAutenticacao`.
- **Aberto/Fechado e Liskov:** fakes e clientes reais podem ser trocados sem mexer nos controles.
- **Demeter:** os controles entregam nicks, cartas e tempos prontos.
- **Desempenho:** no máximo uma consulta por serviço dos outros grupos em cada tela, e cache dos
  dados da PokéAPI no servidor.

## Contrato esperado do serviço de Trocas

Para cumprir "uma consulta por serviço", o painel espera que `GET /trocas` devolva cada troca
**já com as suas propostas** (campo `propostas`). Se o grupo de Trocas não mandar esse campo, a
troca aparece sem propostas.
