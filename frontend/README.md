# Painel de Administração – Interface Web (React)

Telas do Painel de Administração. O painel é **somente leitura**: mostra jogadores, cartas,
trocas em aberto, propostas e histórico.

Esta é só a **Interface Web**. Os dados vêm do **backend do painel** (pasta `backend`,
em Spring Boot), que consulta os serviços dos grupos de Jogadores, Cartas e Trocas e a PokéAPI.

```
Interface Web (este projeto)  →  /api  →  Backend do Painel (Spring Boot)  →  outros grupos
```

## Como rodar

1. Suba o backend (pasta `backend`): `mvn spring-boot:run`, na porta 8080.
2. Aqui, com o **Node.js 20+**:

```bash
npm install
```

```bash
npm run dev
```

3. Abra `http://localhost:5173`. No modo de teste do backend, o login é `admin` / `admin`.

As chamadas para `/api` são repassadas para `http://localhost:8080` pelo proxy do Vite
(`vite.config.ts`), sem problema de CORS.

## Organização do código

| Pasta | Conteúdo |
|---|---|
| `src/telas/` | Uma tela por aba do painel, mais a tela de login |
| `src/api/` | `ApiPainel` (única porta de entrada para o backend), o contexto de sessão e os tipos do JSON |
| `src/componentes/` | Peças visuais reaproveitadas (card do Pokémon, tipos, layout, estados de consulta) |

- As telas não sabem de URLs, tokens nem dos serviços dos outros grupos: só chamam a `ApiPainel`.
- Todas as telas usam o mesmo componente `Consulta` para mostrar "carregando", aviso de erro e
  lista vazia.
- Se a sessão expirar, a tela volta para o login.

O visual (cards com imagem em fundo cinza, número "Nº 0025", tipos coloridos e busca por nome ou
número) é inspirado na Pokédex oficial.
