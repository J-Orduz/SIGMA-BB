import React from 'react';
import { useAuthStore } from '../../store/useAuthStore';
import { useNavigate } from 'react-router-dom';

export const AuthPage: React.FC = () => {
  const loginGlobal = useAuthStore((state) => state.login);
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    loginGlobal({
      username: "NombreEjemplo",
      role: "Administrador",
      token: "jwt_token_generado"
    });
    
    navigate('/');
  };

  return (
    <div className="min-h-screen bg-slate-900 flex items-center justify-center p-4">
      <div className="bg-white p-8 rounded-xl shadow-lg max-w-sm w-full space-y-6">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-slate-800">SIGMA-BB</h2>
          <p className="text-sm text-slate-500 mt-1">Ingresa tus credenciales de acceso</p>
        </div>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <button
            type="submit"
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 rounded-lg transition-colors"
          >
            Iniciar Sesión (Demo)
          </button>
        </form>
      </div>
    </div>
  );
};