import { useApi } from '../api/ApiContext';
import { useConsulta } from '../api/useConsulta';
import { Consulta } from '../componentes/Consulta';
import { MiniPokemon } from '../componentes/MiniPokemon';
import { formatarData, formatarDuracao } from '../componentes/formatar';

export function TelaTrocasAbertas() {
  const { api } = useApi();
  const consulta = useConsulta(api.trocasAbertas);

  return (
    <section>
      <h2 className="titulo-pagina">Trocas em aberto</h2>
      <Consulta consulta={consulta} estaVazio={(lista) => lista.length === 0} mensagemVazio="Nenhuma troca em aberto.">
        {(lista) => (
          <table>
            <thead>
              <tr>
                <th>Carta oferecida</th>
                <th>Ofertante</th>
                <th>Aberta em</th>
                <th>Tempo em aberto</th>
                <th className="numero">Propostas</th>
              </tr>
            </thead>
            <tbody>
              {lista.map((troca) => (
                <tr key={troca.id}>
                  <td>
                    <MiniPokemon carta={troca.cartaOferecida} />
                  </td>
                  <td>{troca.nickOfertante}</td>
                  <td>{formatarData(troca.dataAbertura)}</td>
                  <td>{formatarDuracao(troca.tempoEmAbertoMs)}</td>
                  <td className="numero">{troca.quantidadePropostas}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Consulta>
    </section>
  );
}
