import { useApi } from '../api/ApiContext';
import type { Resumo } from '../api/tipos';
import { useConsulta } from '../api/useConsulta';
import { Consulta } from '../componentes/Consulta';

const itens: { chave: keyof Resumo; rotulo: string }[] = [
  { chave: 'totalJogadores', rotulo: 'Jogadores' },
  { chave: 'totalCartas', rotulo: 'Cartas distribuídas' },
  { chave: 'trocasAbertas', rotulo: 'Trocas em aberto' },
  { chave: 'propostasPendentes', rotulo: 'Propostas pendentes' },
  { chave: 'trocasFinalizadas', rotulo: 'Trocas finalizadas' },
];

export function TelaResumo() {
  const { api } = useApi();
  const consulta = useConsulta(api.resumo);

  return (
    <section>
      <h2 className="titulo-pagina">Resumo geral</h2>
      <Consulta consulta={consulta}>
        {(dados) => (
          <>
            {itens.some((item) => dados[item.chave] === null) && (
              <p className="aviso" role="alert">
                Algum serviço não respondeu. Os totais marcados como indisponíveis não puderam ser carregados.
              </p>
            )}
            <div className="cartoes">
              {itens.map((item) => (
                <div key={item.chave} className="cartao-numero">
                  <strong>{dados[item.chave] ?? 'indisponível'}</strong>
                  <span>{item.rotulo}</span>
                </div>
              ))}
            </div>
          </>
        )}
      </Consulta>
    </section>
  );
}
