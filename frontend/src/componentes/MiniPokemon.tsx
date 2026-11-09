import type { Carta } from '../api/tipos';
import { TipoPokemon } from './TipoPokemon';
import { numeroPokedex } from './tiposPokemon';

export function MiniPokemon({ carta }: { carta: Carta }) {
  return (
    <div className="mini-pokemon">
      <div className="mini-pokemon__imagem">
        {carta.imagemUrl && <img src={carta.imagemUrl} alt={carta.nome} loading="lazy" />}
      </div>
      <div>
        <span className="mini-pokemon__numero">{numeroPokedex(carta.pokemonId)}</span>
        <strong className="mini-pokemon__nome">{carta.nome}</strong>
        <div className="tipos">
          {carta.tipos.map((tipo) => (
            <TipoPokemon key={tipo} tipo={tipo} />
          ))}
        </div>
      </div>
    </div>
  );
}
