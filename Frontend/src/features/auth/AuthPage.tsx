import React from 'react';
import { useAuthStore, type AppRole } from '../../store/useAuthStore';
import { useNavigate } from 'react-router-dom';
import LoginForm from './components/LoginForm';
import { AuthCelebration } from './components/AuthCelebration';

export const AuthPage: React.FC = () => {
  const loginGlobal = useAuthStore((state) => state.login);
  const navigate = useNavigate();
  const [welcomeName, setWelcomeName] = React.useState<string | null>(null);
  const welcomeTimerRef = React.useRef<number | null>(null);

  React.useEffect(() => {
    return () => {
      if (welcomeTimerRef.current) {
        window.clearTimeout(welcomeTimerRef.current);
      }
    };
  }, []);

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
    const loggedUser = {
      id: decodedToken.sub || '',
      username: decodedToken.preferred_username || 'Usuario',
      name: decodedToken.name || 'Usuario SIGMA',
      role: assignedRole,
      token: token
    };

    loginGlobal(loggedUser);
    setWelcomeName(loggedUser.name);

    welcomeTimerRef.current = window.setTimeout(() => {
      navigate('/');
    }, 2500);
  };

  return (
    <div className="min-h-screen relative overflow-hidden bg-slate-950 flex items-center justify-center p-4">
      <div className="sigma-login-aurora absolute inset-0 bg-[radial-gradient(circle_at_20%_20%,rgba(37,99,235,0.42),transparent_28rem),radial-gradient(circle_at_85%_10%,rgba(16,185,129,0.24),transparent_24rem),linear-gradient(135deg,#020617_0%,#0f172a_50%,#111827_100%)]" />
      <div className="sigma-login-grid absolute inset-0 opacity-30" />
      <div className="sigma-login-orbit sigma-login-orbit-one" />
      <div className="sigma-login-orbit sigma-login-orbit-two" />
      <div className="sigma-login-orbit sigma-login-orbit-three" />
      <div className="absolute inset-x-0 top-0 h-px bg-gradient-to-r from-transparent via-blue-300/70 to-transparent" />
      <div className="relative w-full flex items-center justify-center">
        <LoginForm onLoginSuccess={handleLoginSuccess} />
      </div>
      {welcomeName && <AuthCelebration mode="welcome" name={welcomeName} />}
    </div>
  );
};
