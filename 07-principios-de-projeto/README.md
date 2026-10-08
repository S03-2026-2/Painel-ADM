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

## Segregação de interfaces (ISP)

**Teoria:** é melhor ter **interfaces pequenas e específicas** do que uma interface grande que
obrigue os clientes a depender de métodos que não usam.

**No painel:** antes, `IServicoJogadores` tinha dois métodos:

| Método | Quem usa |
|---|---|
| `autenticar(login, senha)` | só o `CtrlAutenticacao` |
| `buscarTodos()` | só o `CtrlResumo` e o `CtrlJogadores` |

Cada controle dependia de um método que não usava. A interface foi **dividida em duas**:
- **`IAutenticacao`**, com `autenticar()`, usada pelo `CtrlAutenticacao`;
- **`IServicoJogadores`**, com `buscarTodos()`, usada pelo `CtrlResumo` e pelo `CtrlJogadores`.

As duas continuam falando com o mesmo serviço de Jogadores, mas cada controle conhece só o que
precisa. O [diagrama de classes](../03-diagrama-de-classes/README.md) e o diagrama de sequência
de [autenticação](../04-diagramas-de-sequencia/README.md#sq01--autenticar-administrador) foram
atualizados.

## Inversão de dependência (DIP)

**Teoria:** as dependências devem ser estabelecidas **com abstrações, e não com implementações
concretas**. Em outras palavras: prefira interfaces a classes e seja o mais abstrato possível.

**No painel:**
- **Os controles dependem só das interfaces:** `CtrlAutenticacao` usa `IAutenticacao`,
  `CtrlJogadores` usa `IServicoJogadores`, `CtrlCartasJogadores` usa `IServicoCartas`, e assim
  por diante. Nenhum controle conhece a classe que fala com o outro grupo.
- **As classes concretas ficam do outro lado da interface:** `ClienteServicoJogadores`,
  `ClienteServicoCartas` e `ClienteServicoTrocas` **realizam** as interfaces (seta tracejada com
  triângulo vazio no [diagrama de classes](../03-diagrama-de-classes/README.md)). Só elas sabem
  como acessar cada serviço.
- **Uma classe, duas interfaces pequenas:** `ClienteServicoJogadores` implementa `IAutenticacao`
  e `IServicoJogadores`. Os dois contratos ficam separados (ISP) mesmo vindo do mesmo serviço.
- **Nos diagramas de sequência**, as linhas de vida dos serviços são as interfaces
  (`«interface» :IServicoCartas`…), porque é só isso que o controle conhece.

**Ganho:** se um grupo mudar a forma de entregar os dados, ou se for preciso uma implementação
de teste, só muda a classe `Cliente*`. Os controles e as telas continuam iguais. Isso é o que
torna possíveis o **OCP** e o **LSP** descritos acima.

## Princípio de Demeter

**Teoria:** também chamado de princípio do menor conhecimento. Um método só deve chamar métodos:
1. da própria classe;
2. dos objetos recebidos como parâmetro;
3. dos objetos criados pelo próprio método;
4. dos atributos da própria classe.

Ele evita "conversar com estranhos", como em `a.getB().getC().fazAlgo()`. No exemplo clássico,
o entregador pede o pagamento ao cliente, e não pega a carteira do cliente para tirar o dinheiro.

**No painel:**
- **O controle entrega os dados prontos para a tela.** Para mostrar o histórico, a tela **não**
  faz `troca.getPropostaAceita().getProponente().getNick()`. O `CtrlTrocas` já associa os nicks
  antes de devolver a lista, como mostram as auto-mensagens "associa nicks…" nos
  [diagramas de sequência](../04-diagramas-de-sequencia/README.md). A tela só fala com o
  controle, que ela recebe como atributo.
- **Os controles só chamam seus atributos:** cada controle conversa apenas com as interfaces de
  serviço que guarda como atributo (caso 4) e com os objetos que ele mesmo monta, como o
  `ResumoPainel` (caso 3).
- **As entidades se referenciam pelos dados, não por cadeias de objetos:** quem precisa do nick de
  um jogador recebe o nick pronto, sem navegar de troca para proposta e de proposta para jogador.

## Resumo

| Princípio | Propriedade que reforça | Onde aparece no painel |
|---|---|---|
| Integridade conceitual | Coerência | Padrão de nomes, cores e estrutura tela → controle → serviço |
| Ocultamento de informação | Ocultamento | Atributos privados, entidades só com getters |
| Responsabilidade única (SRP) | Coesão | Tela exibe, controle organiza, cliente acessa o serviço |
| Segregação de interfaces (ISP) | Coesão | `IAutenticacao` separada de `IServicoJogadores` |
| Inversão de dependência (DIP) | Acoplamento | Controles dependem das interfaces; `Cliente*` as implementam |
| Aberto/Fechado (OCP) | Extensibilidade | Nova aba ou nova implementação sem alterar o que existe |
| Substituição de Liskov (LSP) | Extensibilidade | Qualquer implementação de uma interface cumpre o mesmo contrato |
| Demeter | Ocultamento de informação | Controle entrega dados prontos; a tela não navega entre objetos |
