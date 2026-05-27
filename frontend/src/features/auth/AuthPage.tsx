import React from 'react';
import { useAuthStore, type AppRole } from '../../store/useAuthStore';
import { useNavigate } from 'react-router-dom';
import LoginForm from './components/LoginForm';

export const AuthPage: React.FC = () => {
  const loginGlobal = useAuthStore((state) => state.login);
  const navigate = useNavigate();

  const handleLoginSuccess = (token: string, decodedToken: any) => {
    // Extraer el arreglo de la propiedad "rol" de la raíz del JWT
    const rolesDeRaiz: string[] = Array.isArray(decodedToken.rol) 
      ? decodedToken.rol 
      : [];

    // Inicializar el rol por defecto de la app
    let assignedRole: AppRole = 'Ingeniero Técnico';

    // Evaluar estrictamente basandose en la propiedad "rol"
    if (rolesDeRaiz.includes('engineers')) {
      assignedRole = 'Ingeniero Técnico';
    } else if (rolesDeRaiz.includes('admins')) { 
      // O el nombre exacto que devuelva tu Keycloak en la propiedad "rol" para el administrador
      assignedRole = 'Administrador';
    } else {
      // Por seguridad, si no viene ninguno de los dos, puedes dejar un rol por defecto o manejarlo
      assignedRole = 'Ingeniero Técnico';
    }

    // Guardar en el Store Global de Zustand
    loginGlobal({
      username: decodedToken.preferred_username || 'Usuario',
      name: decodedToken.name || 'Usuario SIGMA',
      role: assignedRole,
      token: token
    });

    navigate('/');
  };

  return (
    <div className="min-h-screen bg-slate-900 flex items-center justify-center p-4">
      <LoginForm onLoginSuccess={handleLoginSuccess} />
    </div>
  );
};