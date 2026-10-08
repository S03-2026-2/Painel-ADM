# 07 – Princípios de Projeto

O maior desafio de um projeto de software é a **complexidade**. Para vencê-la, o problema é
dividido em partes menores e são criadas **abstrações**: elementos que podem ser usados sem
conhecer todos os seus detalhes.

Esta seção mostra como as propriedades e os princípios de projeto aparecem no Painel de
Administração e quais decisões eles motivaram nos diagramas.

## Integridade conceitual

**Teoria:** o sistema deve ser coerente. A mesma ideia é resolvida sempre do mesmo jeito, o que
facilita o uso e o entendimento. A falta de padronização atrapalha quem usa e quem mantém o
sistema.

**No painel:**
- **Padrão de nomes:** `Tela*` para as telas, `Ctrl*` para os controles e `IServico*` para as
  interfaces com os outros grupos.
- **Mesma estrutura em todo caso de uso:** tela → controle → serviço externo, como mostram os
  [diagramas de sequência](../04-diagramas-de-sequencia/README.md).
- **Mesmas cores em todos os diagramas:** azul para fronteira, amarelo para controle e verde para
  entidade.
- **Mesmo comportamento das telas:** todas as abas tratam "serviço indisponível" e "lista vazia"
  da mesma forma (cenários alternativos dos [casos de uso](../02-casos-de-uso/README.md)).

## Ocultamento de informação

**Teoria:** cada classe esconde as decisões que podem mudar. Os atributos ficam privados e o
acesso acontece por um conjunto de métodos públicos, que formam a interface da classe. Essa
parte visível precisa ser estável, porque qualquer mudança nela afeta todos os clientes.
Getters e setters só devem existir quando fizer sentido: um setter é uma exposição.

**No painel:**
- **Atributos privados e só getters nas entidades:** `Jogador`, `Carta`, `Troca`, `Proposta`,
  `Sessao` e `ResumoPainel` têm todos os atributos `-` (private) e apenas métodos `+get...()`.
  Como o painel é somente leitura, **não faz sentido expor setters**: nenhum dado pode ser
  alterado por fora (ver o [diagrama de classes](../03-diagrama-de-classes/README.md)).
- **Dado calculado sem expor o cálculo:** `Troca.tempoEmAberto()` devolve o tempo pronto. Quem
  usa não precisa saber que ele vem da diferença entre agora e `dataAbertura`.
- **As telas não sabem de onde vêm os dados:** elas só chamam métodos públicos dos controles. Se
  a forma de consultar os outros grupos mudar, as telas não mudam.

## Coesão

**Teoria:** toda classe deve implementar uma única funcionalidade ou serviço, e todos os seus
métodos e atributos trabalham para isso. Uma classe coesa é mais fácil de implementar, entender,
manter e testar, e pode ter um único responsável.

**No painel:**
- **Cada tipo de classe faz uma coisa só:**
  - as **telas** só exibem dados e recebem as ações do administrador;
  - os **controles** só organizam os dados de um caso de uso (juntar, contar, agrupar);
  - as **interfaces de serviço** só conversam com um serviço externo;
  - as **entidades** só carregam dados.
- **Um controle por caso de uso:** `CtrlAutenticacao`, `CtrlResumo`, `CtrlJogadores` e
  `CtrlCartasJogadores`.
- **Decisão sobre o `CtrlTrocas`:** ele atende três casos de uso (trocas em aberto, propostas e
  histórico). Mantivemos um único controle porque os três trabalham com os mesmos dados (trocas
  e propostas) e consultam o mesmo serviço, então os métodos estão fortemente relacionados. Se
  esses casos de uso passarem a mudar por motivos diferentes, o controle pode ser dividido em
  três.

## Acoplamento

**Teoria:** acoplamento é a força da conexão entre duas classes. Ele não é ruim por si só: o
problema é a forma como acontece.
- **Acoplamento bom:** a classe A usa apenas métodos públicos da classe B, e a interface de B é
  estável.
- **Acoplamento ruim:** A acessa direto o arquivo ou o banco de dados de B, as duas compartilham
  uma estrutura de dados global, ou a interface de B muda com frequência.

A regra é **maximizar a coesão e minimizar o acoplamento**.

**No painel:**
- **O painel nunca acessa o banco dos outros grupos.** Ele só usa as operações que os serviços
  de Jogadores, Cartas e Trocas oferecem. É exatamente o contrário do exemplo de acoplamento ruim
  (ler o arquivo ou o banco de outra classe).
- **O painel não tem banco próprio nem dados globais compartilhados:** cada controle recebe os
  dados de que precisa e devolve o resultado para a tela.
- **Dependências em uma direção só:** tela → controle → interface de serviço. Uma camada nunca
  conhece a camada de cima (ver o [diagrama de classes](../03-diagrama-de-classes/README.md)).
- **Interfaces estáveis:** os controles dependem de `IServicoJogadores`, `IServicoCartas` e
  `IServicoTrocas`, e não da forma como cada serviço é acessado.

## Responsabilidade única (SRP)

**Teoria:** um módulo deve ter **um, e apenas um, motivo para mudar**. Os motivos de mudança
são os usuários ou grupos que pedem alterações, então cada módulo deve atender um único grupo.
Una o que muda pelo mesmo motivo e separe o que muda por motivos diferentes. O SRP é uma forma de
obter coesão.

**No painel, cada classe muda por um motivo diferente:**

| Classe | Muda quando… |
|---|---|
| Telas (`Tela*`) | a forma de exibir os dados muda (layout, colunas, filtros) |
| Controles (`Ctrl*`) | a regra de um caso de uso muda (o que contar, como agrupar) |
| Interfaces de serviço (`IServico*`) | o que um dos outros grupos oferece muda |
| Entidades | os dados enviados pelos outros grupos mudam |

Se o grupo de Cartas mudar a forma de entregar as cartas, só a parte ligada a `IServicoCartas`
muda. As telas e os outros controles ficam iguais.

## Aberto/Fechado (OCP)

**Teoria:** as entidades de software devem estar **abertas para extensão e fechadas para
modificação**. Um comportamento novo entra como uma classe nova, normalmente implementando uma
interface, sem alterar o código que já funciona.

**No painel:**
- **Nova aba sem mexer nas existentes:** se o painel passar a mostrar, por exemplo, o histórico
  de partidas de outro grupo, entram uma tela, um controle e uma interface de serviço novos. Nenhuma
  tela ou controle existente precisa ser alterado.
- **Nova forma de acessar um serviço:** como os controles usam interfaces, é possível criar outra
  implementação, por exemplo uma que devolve dados fixos para testar o painel sem os outros grupos
  no ar. Os controles continuam iguais.

## Substituição de Liskov (LSP)

**Teoria:** uma classe derivada deve poder **substituir a classe base** sem que o programa
precise mudar. Se S é um subtipo de T, objetos do tipo T podem ser trocados por objetos do tipo
S mantendo as propriedades esperadas. É o que permite usar polimorfismo com segurança.

**No painel:** qualquer classe que implemente uma interface de serviço precisa cumprir o mesmo
contrato, para que os controles funcionem com qualquer uma delas. Por exemplo, toda
implementação de `IServicoCartas`:
- devolve **todas** as cartas em `buscarTodas()`, cada uma com o dono atual;
- devolve uma **lista vazia**, e não `null`, quando não há cartas;
- **não altera** nenhum dado, porque o painel é somente leitura.

Uma implementação que devolvesse só parte das cartas ou alterasse dados quebraria o
`CtrlCartasJogadores`, mesmo tendo os mesmos métodos. Isso violaria o LSP.
