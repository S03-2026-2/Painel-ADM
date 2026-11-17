# 08 – Padrões de Projeto

Um padrão de projeto é uma **solução já testada para um problema que se repete em um contexto**.
Ele não é um código pronto: é uma forma de organizar as classes que outras equipes já usaram com
sucesso. Conhecer os padrões cria um **vocabulário comum**: dizer "a PokéAPI tem um Decorator de
cache" já explica a estrutura para quem conhece o padrão.

Os padrões se dividem em três grupos:

- **Criacionais:** como os objetos são criados.
- **Estruturais:** como as classes e os objetos se combinam.
- **Comportamentais:** como os objetos dividem as responsabilidades e se comunicam.

As cores dos diagramas seguem as do [diagrama de classes](../03-diagrama-de-classes/README.md). As
classes em **lilás** são as que formam o padrão.

## Decorator (estrutural) – PokéAPI com espelho e cache

![Decorator](decorator.drawio.svg)

**Problema.** O painel busca nome, tipos e imagem de cada carta na PokéAPI. A classe
`ClientePokeAPI` fazia três coisas: a requisição HTTP, o cache dos Pokémon já buscados e a troca
para o espelho da PokéAPI no GitHub quando o site original estava bloqueado. Eram três motivos
para mudar a mesma classe, e para testar o cache era preciso ter internet.

**Solução.** Cada comportamento virou uma classe que **implementa** `IServicoPokemon` e
**embrulha** outro `IServicoPokemon`:

| Classe | Papel no padrão | O que faz |
|---|---|---|
| `IServicoPokemon` | componente | contrato: `buscarPokemon(id)` |
| `ClientePokeAPI` | componente concreto | só faz a requisição para uma URL (PokéAPI ou espelho) |
| `FakePokeAPI` | componente concreto | tabela local com 15 Pokémon, para rodar sem internet |
| `PokeAPIComEspelho` | decorator | tenta o serviço principal e, se ele falhar, usa o reserva |
| `PokeAPIComCache` | decorator | devolve o que já foi buscado sem chamar o serviço de novo |

As camadas são montadas em um lugar só, o `ServicosConfig`:

```java
new PokeAPIComCache(
        new PokeAPIComEspelho(
                new ClientePokeAPI("PokéAPI", url + "/pokemon/{id}"),
                new ClientePokeAPI("espelho da PokéAPI", urlEspelho + "/pokemon/{id}/index.json")));
```

O `CatalogoPokemon` continua chamando só `buscarPokemon(id)`. Ele não sabe se há cache, espelho
ou as duas coisas.

**Consequências positivas**
- Cada classe tem **uma responsabilidade** (responsabilidade única).
- Um comportamento novo, como um limite de tempo ou um log de chamadas, entra como **outro
  decorator**, sem mexer nas classes que já existem (aberto/fechado).
- As camadas são testadas **sem internet**, com um serviço falso no lugar do cliente HTTP
  (`DecoradoresPokeAPITest`).
- Se o espelho não estiver configurado, a configuração simplesmente não coloca essa camada.

**Consequências negativas**
- São mais classes para entender o que antes estava em uma só.
- A **ordem** das camadas importa e o compilador não avisa se ela estiver errada. Com o cache por
  fora, o que veio do espelho também fica guardado. Com o cache por dentro, só a PokéAPI original
  teria cache.

## Abstract Factory (criacional) – família de serviços fake ou real

![Abstract Factory](abstract-factory.drawio.svg)

**Problema.** O painel conversa com quatro serviços dos outros grupos: autenticação, jogadores,
cartas e trocas. Enquanto os grupos não terminam os seus serviços, o painel usa versões falsas com
dados de exemplo. O `ServicosConfig` tinha um `if (usarFakes)` que montava cada serviço nos dois
ramos. Nada impedia de misturar as famílias, por exemplo um login falso com o serviço de Cartas
real, o que quebraria o token. E cada serviço novo precisava ser lembrado nos dois ramos do `if`.

**Solução.** Uma interface `FabricaServicos` declara um método de criação para cada serviço. Cada
família tem a sua fábrica concreta:

| Classe | Papel no padrão | Cria |
|---|---|---|
| `FabricaServicos` | fábrica abstrata | contrato: `criarAutenticacao()`, `criarServicoJogadores()`, `criarServicoCartas()`, `criarServicoTrocas()` |
| `FabricaFakes` | fábrica concreta | `FakeServicoJogadores`, `FakeServicoCartas`, `FakeServicoTrocas` |
| `FabricaClientesReais` | fábrica concreta | `ClienteServicoJogadores`, `ClienteServicoCartas`, `ClienteServicoTrocas`, todos com o mesmo `ClienteHttp` |
| `IAutenticacao`, `IServicoJogadores`, `IServicoCartas`, `IServicoTrocas` | produtos abstratos | o que os controles conhecem |

O `ServicosConfig` escolhe a fábrica **uma vez**, pela propriedade `painel.usar-fakes`, e pede a
ela todos os serviços:

```java
FabricaServicos fabrica = usarFakes
        ? new FabricaFakes(conversor)
        : new FabricaClientesReais(TokenDaRequisicao::atual, urls, conversor);
```

**Consequências positivas**
- A família fica sempre **coerente**: todos os serviços vêm da mesma fábrica.
- A configuração não conhece nenhuma classe concreta de serviço. Ela só escolhe a fábrica.
- Uma família nova, por exemplo os serviços de homologação de outro grupo, é **uma fábrica nova**,
  sem mexer nos controles nem nas outras fábricas (aberto/fechado).
- Os controles continuam recebendo só as interfaces (inversão de dependência).

**Consequências negativas**
- Um **tipo** novo de serviço, por exemplo um `IServicoNotificacoes`, obriga a mudar a interface
  `FabricaServicos` e **todas** as fábricas.
- Para quatro serviços, é mais código do que o `if` que existia antes.

## Padrões que já estavam no código

Alguns padrões apareceram antes mesmo de terem nome, porque resolviam problemas do painel:

| Padrão | Grupo | Onde está | Problema que resolve |
|---|---|---|---|
| **Adapter** | estrutural | `ClienteServicoJogadores`, `ClienteServicoCartas`, `ClienteServicoTrocas`, junto com o `ConversorRespostas` | Cada grupo devolve o JSON no seu formato. Os clientes **adaptam** essas respostas às interfaces e às entidades do painel. Se um contrato mudar, só o adaptador daquele serviço muda. |
| **Facade** | estrutural | `ApiPainel` (Interface Web) e `PainelApi` (backend) | As telas falam com **uma porta só**, sem saber de URLs, tokens nem dos serviços dos outros grupos. A `PainelApi` faz o mesmo para a Interface Web: uma rota por tela, com os dados prontos. |
| **Strategy** | comportamental | `IServicoJogadores`, `IServicoCartas`, `IServicoTrocas` e `IServicoPokemon`, cada um com uma versão falsa e uma real | Famílias de implementações **intercambiáveis**: os controles usam a interface e a implementação pode ser trocada sem mexer neles. |
| **Chain of Responsibility** | comportamental | `FiltroDeSessao`, registrado na cadeia de interceptadores do Spring | A requisição passa pelo filtro antes de chegar na API. Sem um token válido, a cadeia para ali e a resposta é `401`. |

## Padrões que não usamos, e por quê

- **Singleton:** o Spring já cria uma instância só de cada bean. Escrever um Singleton à mão
  repetiria isso e dificultaria os testes.
- **Observer:** o painel consulta os dados quando o administrador abre uma aba. Não há eventos em
  tempo real para observar.
- **Builder:** `ResumoPainel` tem só cinco campos, e um construtor basta.

## Resumo

| Padrão | Grupo | Classes | Princípios que reforça |
|---|---|---|---|
| Abstract Factory | criacional | `FabricaServicos`, `FabricaFakes`, `FabricaClientesReais` | aberto/fechado, inversão de dependência |
| Decorator | estrutural | `PokeAPIComEspelho`, `PokeAPIComCache` | responsabilidade única, aberto/fechado |
| Adapter | estrutural | `Cliente*`, `ConversorRespostas` | ocultamento de informação, baixo acoplamento |
| Facade | estrutural | `ApiPainel`, `PainelApi` | Demeter, baixo acoplamento |
| Strategy | comportamental | implementações falsas e reais das interfaces de serviço | substituição de Liskov, inversão de dependência |
| Chain of Responsibility | comportamental | `FiltroDeSessao` | responsabilidade única |

Os princípios estão explicados em [07-principios-de-projeto](../07-principios-de-projeto/README.md).
