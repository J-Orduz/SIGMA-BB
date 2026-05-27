import React, { useState } from 'react';

interface LoginFormProps {
  onLoginSuccess: (token: string, decodedToken: any) => void;
}

export default function LoginForm({ onLoginSuccess }: LoginFormProps) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
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
    <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-xl shadow-lg border border-gray-100">
      <div className="text-center">
        <h2 className="text-3xl font-extrabold text-gray-900">Iniciar Sesión</h2>
        <p className="mt-2 text-sm text-gray-500">Módulo de Autenticación SIGMA-BB</p>
      </div>
      
      <form className="mt-8 space-y-4" onSubmit={handleSubmit}>
        {error && (
          <div className="p-3 text-sm text-red-700 bg-red-50 rounded-lg border border-red-200">
            {error}
          </div>
        )}

        <div>
          <label className="block text-sm font-medium text-gray-700">Usuario</label>
          <input
            type="text"
            disabled={isLoading}
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500 outline-none transition disabled:bg-gray-50"
            placeholder="Ingrese su usuario corporativo"
          />
        </div>
        
        <div>
          <label className="block text-sm font-medium text-gray-700">Contraseña</label>
          <input
            type="password"
            disabled={isLoading}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full px-4 py-2 mt-1 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500 outline-none transition disabled:bg-gray-50"
            placeholder="••••••••"
          />
        </div>

        <button
          type="submit"
          disabled={isLoading}
          className="w-full py-2.5 px-4 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-md shadow transition-colors flex justify-center items-center disabled:bg-blue-400"
        >
          {isLoading ? 'Autenticando...' : 'Ingresar al Sistema'}
        </button>
      </form>
    </div>
  );
}