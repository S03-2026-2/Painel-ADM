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
