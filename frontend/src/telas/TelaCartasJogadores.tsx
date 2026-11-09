import { useState } from 'react';
import { useApi } from '../api/ApiContext';
import type { Carta } from '../api/tipos';
import { useConsulta } from '../api/useConsulta';
import { CartaPokemon } from '../componentes/CartaPokemon';
import { Consulta } from '../componentes/Consulta';

function combina(carta: Carta, busca: string): boolean {
  const termo = busca.trim().toLowerCase();
  if (!termo) return true;
  const numero = Number(termo.replace(/^n[ºo°]?\s*/, ''));
  if (!Number.isNaN(numero) && numero > 0) return carta.pokemonId === numero;
  return carta.nome.toLowerCase().includes(termo);
}

export function TelaCartasJogadores() {
  const { api } = useApi();
  const consulta = useConsulta(api.cartas);
  const [busca, setBusca] = useState('');

  return (
    <section>
      <h2 className="titulo-pagina">Cartas dos jogadores</h2>

      <div className="busca">
        <label htmlFor="busca">Nome ou número</label>
        <div className="busca__campo">
          <input
            id="busca"
            value={busca}
            onChange={(e) => setBusca(e.target.value)}
            placeholder="Ex.: pikachu ou 25"
          />
          {busca && (
            <button className="secundario" onClick={() => setBusca('')}>
              Limpar
            </button>
          )}
        </div>
      </div>

      <Consulta consulta={consulta} estaVazio={(grupos) => grupos.length === 0} mensagemVazio="Nenhum jogador encontrado.">
        {(grupos) => {
          const filtrados = grupos
            .map((grupo) => ({ ...grupo, cartas: grupo.cartas.filter((carta) => combina(carta, busca)) }))
            .filter((grupo) => !busca || grupo.cartas.length > 0);
          if (filtrados.length === 0) return <p className="info">Nenhuma carta encontrada para “{busca}”.</p>;

          return filtrados.map(({ jogador, cartas }) => (
            <div key={jogador.id} className="grupo">
              <h3 className="grupo__titulo">
                {jogador.nick} <span className="contagem">{cartas.length} cartas</span>
              </h3>
              {cartas.length === 0 ? (
                <p className="info">Sem cartas.</p>
              ) : (
                <div className="grade-pokedex">
                  {cartas.map((carta) => (
                    <CartaPokemon key={carta.id} carta={carta} />
                  ))}
                </div>
              )}
            </div>
          ));
        }}
      </Consulta>
    </section>
  );
}
