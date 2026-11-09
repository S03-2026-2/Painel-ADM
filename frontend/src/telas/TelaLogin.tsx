import { useState, type FormEvent } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { useApi } from '../api/ApiContext';

export function TelaLogin() {
  const { logado, entrar } = useApi();
  const navegar = useNavigate();
  const [login, setLogin] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState<string>();
  const [enviando, setEnviando] = useState(false);

  if (logado) return <Navigate to="/" replace />;

  async function informarCredenciais(evento: FormEvent) {
    evento.preventDefault();
    setEnviando(true);
    setErro(undefined);
    try {
      const liberado = await entrar(login, senha);
      if (liberado) navegar('/', { replace: true });
      else setErro('Acesso negado: login ou senha inválidos, ou usuário sem perfil de administrador.');
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Erro inesperado.');
    } finally {
      setEnviando(false);
    }
  }

  return (
    <div className="tela-login">
      <form onSubmit={informarCredenciais}>
        <h1>Painel de Administração</h1>
        <label>
          Login
          <input value={login} onChange={(e) => setLogin(e.target.value)} autoComplete="username" required />
        </label>
        <label>
          Senha
          <input
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            autoComplete="current-password"
            required
          />
        </label>
        {erro && (
          <p className="aviso" role="alert">
            {erro}
          </p>
        )}
        <button type="submit" disabled={enviando}>
          {enviando ? 'Entrando…' : 'Entrar'}
        </button>
      </form>
    </div>
  );
}
