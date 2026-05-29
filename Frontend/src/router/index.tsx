import { createBrowserRouter, Navigate } from 'react-router-dom';
import { AuthPage } from '../features/auth/AuthPage';
import { BrandManager } from '../features/equipments/components/BrandManager';
import { EquipmentTypeManager } from '../features/equipments/components/EquipmentTypeManager';
import { TechnicalVerificationManager } from '../features/equipments/components/TechnicalVerificationManager';
import { ProtectedRoute } from './ProtectedRoute';
import { useAuthStore } from '../store/useAuthStore';
import { Link, Outlet } from 'react-router-dom';
import React from 'react';

const DashboardLayout = () => {
  const { user, logout } = useAuthStore();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = React.useState(false);

  return (
    <div className="flex flex-col md:flex-row min-h-screen bg-slate-100 text-slate-800">
      {/* BARRA DE NAVEGACIÓN SUPERIOR (Móviles) */}
      <header className="md:hidden bg-slate-900 text-white p-4 flex justify-between items-center shadow-md">
        <h2 className="text-xl font-bold text-blue-400 tracking-wider">SIGMA-BB</h2>
        <button 
          onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
          className="text-slate-200 p-2 focus:outline-none bg-slate-800 rounded-lg"
        >
          {isMobileMenuOpen ? '✕ Cerrar' : '☰ Menú'}
        </button>
      </header>

      {/* BARRA LATERAL */}
      <aside className={`
        ${isMobileMenuOpen ? 'block' : 'hidden'} 
        md:flex w-full md:w-64 bg-slate-900 text-slate-200 flex-col justify-between p-4 fixed md:sticky top-0 h-[calc(100vh-60px)] md:h-screen z-50 transition-all duration-300
      `}>
        <div className="space-y-6">
          <div className="hidden md:block px-2 py-3 border-b border-slate-800">
            <h2 className="text-xl font-bold text-blue-400 tracking-wider">SIGMA-BB</h2>
            <p className="text-xs text-slate-400">Bioingeniería</p>
          </div>
          <nav className="space-y-1">
            <Link 
              to="/equipments/brands" 
              onClick={() => setIsMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-lg text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white transition-colors"
            >
              Gestión de Marcas
            </Link>
            
            {/* Control visual de Sidebar: Sólo Administrador o SuperUsuario ven la opción */}
            {(user?.role === 'Administrador' || user?.role === 'SuperUsuario') && (
              <>
                <Link 
                  to="/equipments/types" 
                  onClick={() => setIsMobileMenuOpen(false)}
                  className="block px-3 py-2 rounded-lg text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white transition-colors"
                >
                  Tipos de Equipos
                </Link>
                <Link 
                  to="/equipments/technical-verifications" 
                  onClick={() => setIsMobileMenuOpen(false)}
                  className="block px-3 py-2 rounded-lg text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white transition-colors"
                >
                  Verificaciones Técnicas
                </Link>
              </>
            )}
          </nav>
        </div>
        
        <div className="border-t border-slate-800 pt-4 space-y-3 bg-slate-900">
          <div className="px-2 text-xs text-slate-400">
            Usuario: <span className="text-slate-200 font-medium block truncate">{user?.name}</span>
            Rol: <span className="text-blue-400 font-semibold uppercase block">{user?.role}</span>
          </div>
          <button 
            onClick={logout} 
            className="w-full px-3 py-2 bg-red-600 hover:bg-red-700 text-white text-sm font-medium rounded-lg transition-colors"
          >
            Cerrar Sesión
          </button>
        </div>
      </aside>

      <main className="flex-1 p-4 md:p-8 overflow-y-auto">
        <Outlet />
      </main>
    </div>
  );
};

export const router = createBrowserRouter([
  {
    path: '/login',
    element: <AuthPage />
  },
  {
    path: '/',
    element: <ProtectedRoute />, // Valida que haya sesión iniciada a nivel general
    children: [
      {
        element: <DashboardLayout />,
        children: [
          {
            path: '',
            element: <Navigate to="/equipments/brands" replace />
          },
          {
            path: 'equipments/brands',
            element: <BrandManager /> // Accesible por todos los roles autenticados
          },
          // Encapsular rutas de administración técnica protegiéndolas explícitamente por rol
          {
            element: <ProtectedRoute allowedRoles={['SuperUsuario', 'Administrador']} />, 
            children: [
              {
                path: 'equipments/types',
                element: <EquipmentTypeManager />
              },
              // Ruta hija protegida por rol
              {
                path: 'equipments/technical-verifications',
                element: <TechnicalVerificationManager />
              }
            ]
          }
        ]
      }
    ]
  },
  {
    path: '/unauthorized',
    element: (
      <div className="flex flex-col items-center justify-center min-h-screen bg-slate-50 p-4">
        <div className="bg-white p-8 rounded-xl shadow-md border border-slate-200 max-w-md text-center space-y-4">
          <span className="text-4xl">🚫</span>
          <h2 className="text-xl font-bold text-slate-800">Acceso No Autorizado</h2>
          <p className="text-sm text-slate-500">Su cuenta actual no posee los privilegios necesarios para interactuar con este módulo.</p>
          <Link to="/" className="inline-block bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium py-2 px-4 rounded-lg transition-colors">
            Regresar al Inicio
          </Link>
        </div>
      </div>
    )
  },
  {
    path: '*',
    element: <Navigate to="/" replace />
  }
]);