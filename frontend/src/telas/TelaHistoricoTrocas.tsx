import { useApi } from '../api/ApiContext';
import { useConsulta } from '../api/useConsulta';
import { Consulta } from '../componentes/Consulta';
import { MiniPokemon } from '../componentes/MiniPokemon';
import { formatarData } from '../componentes/formatar';

export function TelaHistoricoTrocas() {
  const { api } = useApi();
  const consulta = useConsulta(api.historico);

  return (
    <section>
      <h2 className="titulo-pagina">Histórico de trocas</h2>
      <Consulta consulta={consulta} estaVazio={(lista) => lista.length === 0} mensagemVazio="Nenhuma troca finalizada.">
        {(lista) => (
          <table>
            <thead>
              <tr>
                <th>Finalizada em</th>
                <th>Ofertante</th>
                <th>Carta oferecida</th>
                <th>Quem recebeu</th>
                <th>Carta recebida em troca</th>
              </tr>
            </thead>
            <tbody>
              {lista.map((troca) => (
                <tr key={troca.id}>
                  <td>{formatarData(troca.dataFinalizacao)}</td>
                  <td>{troca.nickOfertante}</td>
                  <td>
                    <MiniPokemon carta={troca.cartaOferecida} />
                  </td>
                  <td>{troca.nickReceptor}</td>
                  <td>{troca.cartaRecebida ? <MiniPokemon carta={troca.cartaRecebida} /> : '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Consulta>
    </section>
  );
}
