# Painel de Administração – Backend (Spring Boot)

Backend do Painel de Administração. Fica entre a Interface Web (React) e os serviços dos grupos
de **Jogadores**, **Cartas** e **Trocas**: junta os dados, guarda a sessão do administrador e
entrega para as telas só o que elas precisam mostrar. O painel é **somente leitura**.

```
Interface Web (React)  →  /api  →  Backend do Painel  →  serviços de Jogadores, Cartas e Trocas
                                                      →  PokéAPI (nome, tipos e imagem)
```

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

Os princípios de projeto aplicados estão em [07-principios-de-projeto](../07-principios-de-projeto/README.md).

## Contrato esperado do serviço de Trocas

Para cumprir "uma consulta por serviço", o painel espera que `GET /trocas` devolva cada troca
**já com as suas propostas** (campo `propostas`). Se o grupo de Trocas não mandar esse campo, a
troca aparece sem propostas.
