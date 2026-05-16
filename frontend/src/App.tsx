import { useState } from 'react';
import AuthPage from './features/auth/AuthPage';

export default function App() {
  const [user, setUser] = useState<string | null>(null);

  const handleLogin = (username: string) => {
    setUser(username);
  };

  const handleLogout = () => {
    setUser(null);
  };

  return (
    <>
      {user ? (
        // Pantalla que se ve al iniciar sesión con éxito
        <div className="min-h-screen bg-gray-100 p-8">
          <div className="max-w-4xl mx-auto bg-white p-6 rounded-lg shadow-md flex justify-between items-center">
            <div>
              <h1 className="text-2xl font-bold text-gray-800">¡Bienvenido, {user}!</h1>
              <p className="text-sm text-green-600 font-medium">Autenticación exitosa</p>
            </div>
            <button
              onClick={handleLogout}
              className="px-4 py-2 bg-red-500 hover:bg-red-600 text-white font-medium rounded transition"
            >
              Cerrar Sesión
            </button>
          </div>
        </div>
      ) : (
        // Si no hay usuario, muestra la página de login
        <AuthPage onLogin={handleLogin} />
      )}
    </>
  );
}
