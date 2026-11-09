import type { ReactNode } from 'react';
import type { EstadoDaConsulta } from '../api/useConsulta';

type Props<T> = {
  consulta: EstadoDaConsulta<T>;
  estaVazio?: (dados: T) => boolean;
  mensagemVazio?: string;
  children: (dados: T) => ReactNode;
};

export function Consulta<T>({ consulta, estaVazio, mensagemVazio, children }: Props<T>) {
  if (consulta.carregando) return <p className="info">Carregando…</p>;

  if (consulta.erro) {
    return (
      <div className="aviso" role="alert">
        <p>{consulta.erro}</p>
        <button onClick={consulta.recarregar}>Tentar de novo</button>
      </div>
    );
  }

  if (consulta.dados === undefined) return null;
  if (estaVazio?.(consulta.dados)) return <p className="info">{mensagemVazio ?? 'Nada encontrado.'}</p>;
  return <>{children(consulta.dados)}</>;
}
