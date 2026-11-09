import type { CSSProperties } from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import { useApi } from '../api/ApiContext';

const abas = [
  { para: '/', rotulo: 'Resumo', cor: '#919191' },
  { para: '/jogadores', rotulo: 'Jogadores', cor: '#30a7d7' },
  { para: '/cartas', rotulo: 'Cartas', cor: '#e3350d' },
  { para: '/trocas', rotulo: 'Trocas em aberto', cor: '#4dad5b' },
  { para: '/propostas', rotulo: 'Propostas', cor: '#e6bc2f' },
  { para: '/historico', rotulo: 'Histórico', cor: '#9b6ed5' },
];

export function Layout() {
  const { sair } = useApi();
  return (
    <div className="layout">
      <header className="topo">
        <div className="topo__barra">
          <span className="topo__titulo">Painel de Administração</span>
          <button className="secundario" onClick={sair}>
            Sair
          </button>
        </div>
        <nav className="menu">
          {abas.map((aba) => (
            <NavLink
              key={aba.para}
              to={aba.para}
              end={aba.para === '/'}
              className="menu__aba"
              style={{ '--cor-aba': aba.cor } as CSSProperties}
            >
              {aba.rotulo}
            </NavLink>
          ))}
        </nav>
      </header>
      <main className="conteudo">
        <Outlet />
      </main>
    </div>
  );
}
