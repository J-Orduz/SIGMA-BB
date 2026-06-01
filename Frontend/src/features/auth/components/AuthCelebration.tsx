import React from 'react';

interface AuthCelebrationProps {
  mode: 'welcome' | 'goodbye';
  name: string;
}

export const AuthCelebration: React.FC<AuthCelebrationProps> = ({ mode, name }) => {
  const title = mode === 'welcome' ? 'Bienvenid@' : 'Hasta Pronto';
  const subtitle =
    mode === 'welcome'
      ? 'Preparando tu espacio de trabajo en SIGMA-BB'
      : 'Cerrando tu sesión de forma segura';

  return (
    <div className="sigma-auth-celebration" role="status" aria-live="polite">
      <div className="sigma-auth-celebration-glow" />
      <div className="sigma-auth-celebration-card">
        <div className="sigma-auth-mascot" aria-hidden="true">
          <div className="sigma-auth-mascot-head">
            <span className="sigma-auth-mascot-eye sigma-auth-mascot-eye-left" />
            <span className="sigma-auth-mascot-eye sigma-auth-mascot-eye-right" />
            <span className="sigma-auth-mascot-smile" />
          </div>
          <div className="sigma-auth-mascot-body">
            <span className="sigma-auth-mascot-core" />
          </div>
          <span className="sigma-auth-mascot-arm sigma-auth-mascot-arm-left" />
          <span className="sigma-auth-mascot-arm sigma-auth-mascot-arm-right" />
        </div>

        <div className="text-center space-y-2">
          <p className="text-xs font-bold uppercase tracking-[0.28em] text-blue-600">
            SIGMA-BB
          </p>
          <h1 className="sigma-auth-celebration-title">
            {title}, <span>{name || 'Usuario'}</span>
          </h1>
          <p className="text-sm text-slate-500">{subtitle}</p>
        </div>

        <div className="sigma-auth-progress" aria-hidden="true">
          <span />
        </div>
      </div>
    </div>
  );
};
