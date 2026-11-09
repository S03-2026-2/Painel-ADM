import { TIPOS } from './tiposPokemon';

export function TipoPokemon({ tipo }: { tipo: string }) {
  const info = TIPOS[tipo] ?? { nome: tipo, cor: '#a4acaf', textoEscuro: true };
  return (
    <span
      className="tipo"
      style={{ background: info.cor, color: info.textoEscuro ? '#212121' : '#ffffff' }}
    >
      {info.nome}
    </span>
  );
}
