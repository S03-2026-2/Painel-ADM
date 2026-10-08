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
