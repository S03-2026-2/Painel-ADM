import { useApi } from '../api/ApiContext';
import { useConsulta } from '../api/useConsulta';
import { Consulta } from '../componentes/Consulta';
import { formatarData } from '../componentes/formatar';

export function TelaJogadores() {
  const { api } = useApi();
  const consulta = useConsulta(api.jogadores);

  return (
    <section>
      <h2 className="titulo-pagina">Jogadores</h2>
      <Consulta consulta={consulta} estaVazio={(lista) => lista.length === 0} mensagemVazio="Nenhum jogador encontrado.">
        {(lista) => (
          <table>
            <thead>
              <tr>
                <th>Nick</th>
                <th>E-mail</th>
                <th>Cadastro</th>
                <th className="numero">Cartas</th>
              </tr>
            </thead>
            <tbody>
              {lista.map((jogador) => (
                <tr key={jogador.id}>
                  <td>{jogador.nick}</td>
                  <td>{jogador.email}</td>
                  <td>{formatarData(jogador.dataCadastro)}</td>
                  <td className="numero">{jogador.quantidadeCartas}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Consulta>
    </section>
  );
}
