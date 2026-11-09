import { useState } from 'react';
import { useApi } from '../api/ApiContext';
import type { StatusProposta } from '../api/tipos';
import { useConsulta } from '../api/useConsulta';
import { Consulta } from '../componentes/Consulta';
import { MiniPokemon } from '../componentes/MiniPokemon';
import { formatarData } from '../componentes/formatar';

type Filtro = StatusProposta | 'TODAS';

const filtros: { valor: Filtro; rotulo: string }[] = [
  { valor: 'TODAS', rotulo: 'Todas' },
  { valor: 'PENDENTE', rotulo: 'Pendentes' },
  { valor: 'ACEITA', rotulo: 'Aceitas' },
  { valor: 'RECUSADA', rotulo: 'Recusadas' },
];

export function TelaPropostas() {
  const { api } = useApi();
  const consulta = useConsulta(api.propostas);
  const [filtro, setFiltro] = useState<Filtro>('TODAS');

  return (
    <section>
      <h2 className="titulo-pagina">Propostas de troca</h2>
      <div className="filtros" role="group" aria-label="Filtrar por status">
        {filtros.map((f) => (
          <button
            key={f.valor}
            className={filtro === f.valor ? 'filtro filtro--ativo' : 'filtro'}
            onClick={() => setFiltro(f.valor)}
          >
            {f.rotulo}
          </button>
        ))}
      </div>
      <Consulta consulta={consulta}>
        {(lista) => {
          const visiveis = lista.filter((proposta) => filtro === 'TODAS' || proposta.status === filtro);
          if (visiveis.length === 0) return <p className="info">Nenhuma proposta encontrada.</p>;
          return (
            <table>
              <thead>
                <tr>
                  <th>Carta proposta</th>
                  <th>Proponente</th>
                  <th>Troca</th>
                  <th>Status</th>
                  <th>Data</th>
                </tr>
              </thead>
              <tbody>
                {visiveis.map((proposta) => (
                  <tr key={proposta.id}>
                    <td>
                      <MiniPokemon carta={proposta.cartaProposta} />
                    </td>
                    <td>{proposta.nickProponente}</td>
                    <td>{proposta.trocaId}</td>
                    <td>
                      <span className={`status status-${proposta.status.toLowerCase()}`}>{proposta.status}</span>
                    </td>
                    <td>{formatarData(proposta.dataProposta)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          );
        }}
      </Consulta>
    </section>
  );
}
