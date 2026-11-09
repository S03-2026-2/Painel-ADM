import { createContext, useContext, useMemo, useState, type ReactNode } from 'react';
import { ApiPainel } from './ApiPainel';

type ContextoApi = {
  api: ApiPainel;
  logado: boolean;
  entrar: (login: string, senha: string) => Promise<boolean>;
  sair: () => void;
};

const Contexto = createContext<ContextoApi | null>(null);

export function ApiProvider({ children }: { children: ReactNode }) {
  const [logado, setLogado] = useState(false);
  const api = useMemo(() => new ApiPainel(() => setLogado(false)), []);

  const valor = useMemo<ContextoApi>(
    () => ({
      api,
      logado,
      entrar: async (login, senha) => {
        const liberado = await api.entrar(login, senha);
        setLogado(liberado);
        return liberado;
      },
      sair: () => {
        void api.sair();
        setLogado(false);
      },
    }),
    [api, logado],
  );

  return <Contexto.Provider value={valor}>{children}</Contexto.Provider>;
}

export function useApi(): ContextoApi {
  const contexto = useContext(Contexto);
  if (!contexto) throw new Error('useApi precisa estar dentro de <ApiProvider>.');
  return contexto;
}
