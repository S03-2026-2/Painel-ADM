import type { CartasDoJogador, Jogador, Proposta, Resumo, TrocaAberta, TrocaFinalizada } from './tipos';

export class SessaoExpirada extends Error {
  constructor() {
    super('Sua sessão expirou. Faça login novamente.');
    this.name = 'SessaoExpirada';
  }
}

export class ApiPainel {
  private token?: string;

  constructor(private readonly aoExpirar: () => void) {}

  async entrar(login: string, senha: string): Promise<boolean> {
    const resposta = await this.enviar('/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ login, senha }),
    });
    if (resposta.status === 401) return false;
    if (!resposta.ok) throw new Error(await mensagemDe(resposta));
    this.token = ((await resposta.json()) as { token: string }).token;
    return true;
  }

  async sair(): Promise<void> {
    const token = this.token;
    this.token = undefined;
    if (token) await this.enviar('/api/logout', { method: 'POST', headers: { Authorization: `Bearer ${token}` } });
  }

  resumo = () => this.consultar<Resumo>('/api/resumo');
  jogadores = () => this.consultar<Jogador[]>('/api/jogadores');
  cartas = () => this.consultar<CartasDoJogador[]>('/api/cartas');
  trocasAbertas = () => this.consultar<TrocaAberta[]>('/api/trocas/abertas');
  propostas = () => this.consultar<Proposta[]>('/api/propostas');
  historico = () => this.consultar<TrocaFinalizada[]>('/api/trocas/historico');

  private async consultar<T>(url: string): Promise<T> {
    const resposta = await this.enviar(url, { headers: { Authorization: `Bearer ${this.token ?? ''}` } });
    if (resposta.status === 401) {
      this.token = undefined;
      this.aoExpirar();
      throw new SessaoExpirada();
    }
    if (!resposta.ok) throw new Error(await mensagemDe(resposta));
    return (await resposta.json()) as T;
  }

  private async enviar(url: string, opcoes: RequestInit): Promise<Response> {
    try {
      return await fetch(url, opcoes);
    } catch {
      throw new Error('Não foi possível conectar ao backend do painel. Ele está rodando?');
    }
  }
}

async function mensagemDe(resposta: Response): Promise<string> {
  try {
    const corpo = (await resposta.json()) as { mensagem?: string };
    if (corpo.mensagem) return corpo.mensagem;
  } catch {
  }
  return `Erro ${resposta.status} ao consultar o painel.`;
}
