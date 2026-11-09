import type { Carta } from '../api/tipos';
import { TipoPokemon } from './TipoPokemon';
import { numeroPokedex } from './tiposPokemon';

export function CartaPokemon({ carta }: { carta: Carta }) {
  return (
    <figure className="carta-pokemon">
      <div className="carta-pokemon__imagem">
        {carta.imagemUrl ? (
          <img src={carta.imagemUrl} alt={carta.nome} loading="lazy" />
        ) : (
          <span className="carta-pokemon__sem-imagem">?</span>
        )}
      </div>
      <figcaption>
        <p className="carta-pokemon__numero">{numeroPokedex(carta.pokemonId)}</p>
        <h5 className="carta-pokemon__nome">{carta.nome}</h5>
        <div className="tipos">
          {carta.tipos.map((tipo) => (
            <TipoPokemon key={tipo} tipo={tipo} />
          ))}
        </div>
      </figcaption>
    </figure>
  );
}
