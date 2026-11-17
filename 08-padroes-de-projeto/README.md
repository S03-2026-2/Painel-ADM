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
