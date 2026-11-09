import { useEffect, useState } from 'react';

export type EstadoDaConsulta<T> = {
  carregando: boolean;
  dados?: T;
  erro?: string;
  recarregar: () => void;
};

export function useConsulta<T>(carregar: () => Promise<T>): EstadoDaConsulta<T> {
  const [estado, setEstado] = useState<Omit<EstadoDaConsulta<T>, 'recarregar'>>({ carregando: true });
  const [versao, setVersao] = useState(0);

  useEffect(() => {
    let ativo = true;
    setEstado((anterior) => ({ ...anterior, carregando: true, erro: undefined }));
    carregar()
      .then((dados) => {
        if (ativo) setEstado({ carregando: false, dados });
      })
      .catch((erro: unknown) => {
        if (ativo) {
          setEstado({ carregando: false, erro: erro instanceof Error ? erro.message : 'Erro inesperado.' });
        }
      });
    return () => {
      ativo = false;
    };
  }, [versao]);

  return { ...estado, recarregar: () => setVersao((v) => v + 1) };
}
