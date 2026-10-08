# 01 – Requisitos

## Enunciado (Painel de Administração)

> Responsável por administrar a posse de cartas na plataforma. Deve fornecer um painel
> administrativo com as informações dos jogadores e as cartas que eles possuem, além das trocas
> em aberto, propostas realizadas e histórico de trocas finalizadas.

## Escopo

O painel é **somente leitura**. Ele junta e mostra informações gerais que já existem nos
serviços dos outros grupos:

| Grupo | O que o painel busca |
|---|---|
| **Jogadores** | autenticação do administrador e lista de jogadores |
| **Cartas** | cartas que cada jogador possui |
| **Trocas** | trocas em aberto, propostas e histórico de trocas finalizadas |

**Fora do escopo:** cadastrar, editar ou apagar jogadores, cartas ou trocas, e guardar dados
próprios. Isso é responsabilidade dos outros grupos.

## Requisitos funcionais

| ID | Requisito | Caso de uso |
|---|---|---|
| RF01 | Só usuários com perfil **ADMIN** (validado pelo serviço de Jogadores) acessam o painel. | UC01 |
| RF02 | Mostrar um resumo geral: total de jogadores, de cartas distribuídas, de trocas em aberto, de propostas pendentes e de trocas finalizadas. | UC02 |
| RF03 | Listar os jogadores com nick, e-mail, data de cadastro e quantidade de cartas. | UC03 |
| RF04 | Mostrar as cartas de cada jogador (nome, tipos e imagem do Pokémon). | UC04 |
| RF05 | Listar as trocas em aberto, com ofertante, carta oferecida, tempo em aberto e quantidade de propostas. | UC05 |
| RF06 | Listar as propostas realizadas, com troca, proponente, carta proposta, status e data. | UC06 |
| RF07 | Listar o histórico de trocas finalizadas, com data, os dois jogadores e as cartas trocadas. | UC07 |

## Requisitos não funcionais

| ID | Categoria | Requisito |
|---|---|---|
| RNF01 | Segurança | Só o administrador autenticado consegue consultar os dados. |
| RNF02 | Segurança | O painel não altera dados de nenhum serviço, só consulta. |
| RNF03 | Usabilidade | Se um serviço estiver fora do ar, o painel mostra um aviso e as outras abas continuam funcionando. |
| RNF04 | Desempenho | Cada tela faz **no máximo uma consulta por serviço**. Exemplo: as cartas vêm todas de uma vez, não uma consulta por jogador. |
| RNF05 | Manutenibilidade | Cada serviço externo é acessado por uma interface própria (`IAutenticacao`, `IServicoJogadores`, `IServicoCartas`, `IServicoTrocas`). Se um serviço mudar, só a classe dessa interface muda. |

## Dependências e pontos em aberto

- O que cada serviço devolve (a combinar com cada grupo).
- Como o perfil ADMIN é atribuído (Jogadores).
- Quem atualiza o dono da carta depois de uma troca (Cartas × Trocas).
