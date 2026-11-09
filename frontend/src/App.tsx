import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { Layout } from './componentes/Layout';
import { RotaProtegida } from './componentes/RotaProtegida';
import { TelaCartasJogadores } from './telas/TelaCartasJogadores';
import { TelaHistoricoTrocas } from './telas/TelaHistoricoTrocas';
import { TelaJogadores } from './telas/TelaJogadores';
import { TelaLogin } from './telas/TelaLogin';
import { TelaPropostas } from './telas/TelaPropostas';
import { TelaResumo } from './telas/TelaResumo';
import { TelaTrocasAbertas } from './telas/TelaTrocasAbertas';

export function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<TelaLogin />} />
        <Route element={<RotaProtegida />}>
          <Route element={<Layout />}>
            <Route index element={<TelaResumo />} />
            <Route path="jogadores" element={<TelaJogadores />} />
            <Route path="cartas" element={<TelaCartasJogadores />} />
            <Route path="trocas" element={<TelaTrocasAbertas />} />
            <Route path="propostas" element={<TelaPropostas />} />
            <Route path="historico" element={<TelaHistoricoTrocas />} />
          </Route>
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
