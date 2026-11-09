import { Navigate, Outlet } from 'react-router-dom';
import { useApi } from '../api/ApiContext';

export function RotaProtegida() {
  const { logado } = useApi();
  return logado ? <Outlet /> : <Navigate to="/login" replace />;
}
