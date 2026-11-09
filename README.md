# Painel de Administração

Projeto da disciplina **S03 – Arquitetura e Desenho de Software** (Inatel).

Este repositório é da aplicação **Painel de Administração**. É um painel **somente leitura** que mostra as
informações gerais da plataforma: jogadores e suas cartas, trocas em aberto, propostas e
histórico de trocas finalizadas. Os dados vêm dos serviços dos grupos de **Jogadores**,
**Cartas** e **Trocas**.

![Diagrama de componentes](06-arquitetura/diagrama-de-componentes.drawio.svg)

## Organização do repositório

| Pasta | Conteúdo |
|---|---|
| [01-requisitos](01-requisitos/README.md) | Enunciado, escopo, requisitos funcionais e não funcionais |
| [02-casos-de-uso](02-casos-de-uso/README.md) | Diagrama de casos de uso e documentação de cada caso (fluxo de eventos) |
| [03-diagrama-de-classes](03-diagrama-de-classes/README.md) | Classes de análise «boundary» / «control» / «entity» |
| [04-diagramas-de-sequencia](04-diagramas-de-sequencia/README.md) | Um diagrama de sequência por caso de uso |
| [05-diagrama-de-atividades](05-diagrama-de-atividades/README.md) | Fluxo completo do administrador, com raias |
| [06-arquitetura](06-arquitetura/README.md) | Diagramas de componentes e de implantação e decisões de arquitetura |
| [07-principios-de-projeto](07-principios-de-projeto/README.md) | Propriedades e princípios de projeto aplicados ao painel |
| [backend](backend/README.md) | Código do backend do painel (Spring Boot): API, controles, serviços, entidades e testes |
| [frontend](frontend/README.md) | Código da Interface Web do painel (React) |
