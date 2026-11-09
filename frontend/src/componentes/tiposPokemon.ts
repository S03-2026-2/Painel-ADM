export const TIPOS: Record<string, { nome: string; cor: string; textoEscuro?: boolean }> = {
  normal: { nome: 'Normal', cor: '#a4acaf', textoEscuro: true },
  fire: { nome: 'Fogo', cor: '#fd7d24' },
  water: { nome: 'Água', cor: '#4592c4' },
  grass: { nome: 'Planta', cor: '#9bcc50', textoEscuro: true },
  electric: { nome: 'Elétrico', cor: '#eed535', textoEscuro: true },
  ice: { nome: 'Gelo', cor: '#51c4e7', textoEscuro: true },
  fighting: { nome: 'Lutador', cor: '#d56723' },
  poison: { nome: 'Venenoso', cor: '#b97fc9' },
  ground: { nome: 'Terrestre', cor: '#d6b84a', textoEscuro: true },
  flying: { nome: 'Voador', cor: '#7fc8ed', textoEscuro: true },
  psychic: { nome: 'Psíquico', cor: '#f366b9' },
  bug: { nome: 'Inseto', cor: '#729f3f' },
  rock: { nome: 'Pedra', cor: '#a38c21' },
  ghost: { nome: 'Fantasma', cor: '#7b62a3' },
  dragon: { nome: 'Dragão', cor: '#5a7fd6' },
  dark: { nome: 'Sombrio', cor: '#707070' },
  steel: { nome: 'Aço', cor: '#9eb7b8', textoEscuro: true },
  fairy: { nome: 'Fada', cor: '#fdb9e9', textoEscuro: true },
};

export function numeroPokedex(id: number): string {
  return `Nº ${String(id).padStart(4, '0')}`;
}
