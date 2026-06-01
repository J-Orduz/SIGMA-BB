import React, { useState } from 'react';

interface LoginFormProps {
  onLoginSuccess: (token: string, decodedToken: any) => void;
}

export default function LoginForm({ onLoginSuccess }: LoginFormProps) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Función auxiliar para decodificar la sección intermedia (Payload) del JWT de Keycloak
  const decodeJwt = (token: string) => {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        window.atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (e) {
      return null;
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!username.trim() || !password.trim()) {
      setError('Por favor, llena todos los campos');
      return;
    }

    setIsLoading(true);

    try {
      // Cuerpo de la petición formateado estrictamente como x-www-form-urlencoded
      const details: Record<string, string> = {
        grant_type: 'password',
        client_id: 'sigma-frontend',
        username: username,
        password: password,
      };

      const formBody = Object.keys(details)
        .map((key) => encodeURIComponent(key) + '=' + encodeURIComponent(details[key]))
        .join('&');

      const response = await fetch(
        'http://localhost:8080/realms/sigma-bb-realm/protocol/openid-connect/token',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
          },
          body: formBody,
        }
      );

      if (!response.ok) {
        throw new Error('Credenciales inválidas o error de conexión con Keycloak');
      }

      const data = await response.json();
      const decoded = decodeJwt(data.access_token);

      if (decoded) {
        onLoginSuccess(data.access_token, decoded);
      } else {
        throw new Error('El token de acceso recibido es corrupto o inválido');
      }
    } catch (err: any) {
      setError(err.message || 'Ocurrió un error inesperado al intentar iniciar sesión');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="sigma-login-card w-full max-w-md p-8 space-y-6 bg-white/90 rounded-2xl shadow-2xl border border-white/80 backdrop-blur-xl">
      <div className="text-center space-y-3">
        <div className="sigma-login-mark mx-auto h-12 w-12 rounded-2xl bg-blue-600 text-white grid place-items-center text-sm font-bold shadow-lg shadow-blue-300/50">
          SB
        </div>
        <div>
          <h2 className="sigma-login-title text-3xl font-extrabold text-slate-900">Iniciar Sesión</h2>
          <p className="mt-2 text-sm text-slate-500">Módulo de Autenticación SIGMA-BB</p>
        </div>
      </div>
      
      <form className="mt-8 space-y-4" onSubmit={handleSubmit}>
        {error && (
          <div className="p-3 text-sm text-red-700 bg-red-50 rounded-lg border border-red-200">
            {error}
          </div>
        )}

        <div>
          <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider">Usuario</label>
          <input
            type="text"
            disabled={isLoading}
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full px-4 py-2.5 mt-1 border border-slate-200 rounded-lg bg-white/90 shadow-inner shadow-slate-100/70 focus:ring-2 focus:ring-blue-500 focus:border-blue-400 outline-none transition disabled:bg-slate-50 placeholder:text-slate-400"
            placeholder="Ingrese su usuario corporativo"
          />
        </div>
        
        <div>
          <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider">Contraseña</label>
          <div className="relative mt-1">
            <input
              type={showPassword ? 'text' : 'password'}
              disabled={isLoading}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-2.5 pr-12 border border-slate-200 rounded-lg bg-white/90 shadow-inner shadow-slate-100/70 focus:ring-2 focus:ring-blue-500 focus:border-blue-400 outline-none transition disabled:bg-slate-50 placeholder:text-slate-400"
              placeholder="••••••••"
            />
            <button
              type="button"
              onClick={() => setShowPassword((current) => !current)}
              disabled={isLoading}
              aria-label={showPassword ? 'Ocultar contraseña' : 'Ver contraseña'}
              title={showPassword ? 'Ocultar contraseña' : 'Ver contraseña'}
              className="absolute inset-y-0 right-0 px-3 text-slate-400 hover:text-blue-600 disabled:text-slate-300"
            >
              {showPassword ? (
                <svg className="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
                  <path d="M3 3l18 18" />
                  <path d="M10.6 10.6a2 2 0 0 0 2.8 2.8" />
                  <path d="M9.5 4.5A10.8 10.8 0 0 1 12 4c5 0 8.5 4.5 9.5 6.2a2 2 0 0 1 0 2.1 15 15 0 0 1-2.2 2.8" />
                  <path d="M6.4 6.7A15 15 0 0 0 2.5 10.2a2 2 0 0 0 0 2.1C3.5 14 7 18.5 12 18.5a10.7 10.7 0 0 0 4-.8" />
                </svg>
              ) : (
                <svg className="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
                  <path d="M2.5 10.2C3.5 8.5 7 4 12 4s8.5 4.5 9.5 6.2a2 2 0 0 1 0 2.1C20.5 14 17 18.5 12 18.5s-8.5-4.5-9.5-6.2a2 2 0 0 1 0-2.1Z" />
                  <circle cx="12" cy="11.25" r="2.75" />
                </svg>
              )}
            </button>
          </div>
        </div>

        <button
          type="submit"
          disabled={isLoading}
          className="w-full py-2.5 px-4 text-sm font-semibold text-white bg-blue-600 hover:bg-blue-700 active:bg-blue-800 rounded-lg shadow-lg shadow-blue-200 transition-all flex justify-center items-center disabled:bg-blue-400"
        >
          {isLoading ? 'Autenticando...' : 'Ingresar al Sistema'}
        </button>
      </form>
    </div>
  );
}
