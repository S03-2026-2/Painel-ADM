export type Carta = {
  readonly id: string;
  readonly pokemonId: number;
  readonly nome: string;
  readonly tipos: readonly string[];
  readonly imagemUrl: string;
};

export type Resumo = {
  readonly totalJogadores: number | null;
  readonly totalCartas: number | null;
  readonly trocasAbertas: number | null;
  readonly propostasPendentes: number | null;
  readonly trocasFinalizadas: number | null;
};

export type Jogador = {
  readonly id: string;
  readonly nick: string;
  readonly email: string;
  readonly dataCadastro: string;
  readonly quantidadeCartas: number;
};

export type CartasDoJogador = {
  readonly jogador: { readonly id: string; readonly nick: string };
  readonly cartas: readonly Carta[];
};

export type TrocaAberta = {
  readonly id: string;
  readonly nickOfertante: string;
  readonly cartaOferecida: Carta;
  readonly dataAbertura: string;
  readonly tempoEmAbertoMs: number;
  readonly quantidadePropostas: number;
};

export type StatusProposta = 'PENDENTE' | 'ACEITA' | 'RECUSADA';

export type Proposta = {
  readonly id: string;
  readonly trocaId: string;
  readonly nickProponente: string;
  readonly cartaProposta: Carta;
  readonly status: StatusProposta;
  readonly dataProposta: string;
};

export type TrocaFinalizada = {
  readonly id: string;
  readonly dataFinalizacao: string;
  readonly nickOfertante: string;
  readonly cartaOferecida: Carta;
  readonly nickReceptor: string;
  readonly cartaRecebida: Carta | null;
};
