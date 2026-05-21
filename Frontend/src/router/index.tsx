// Aquí se definen las rutas reales
import { createBrowserRouter, Navigate } from 'react-router-dom';
import {AuthPage} from '../features/auth/AuthPage';
import { BrandManager } from '../features/equipments/components/BrandManager';
import { ProtectedRoute } from './ProtectedRoute';
import { useAuthStore } from '../store/useAuthStore';
import { Link, Outlet } from 'react-router-dom';
import React from 'react';

// Layout base para la sección privada de la aplicación
const DashboardLayout = () => {
  const { user, logout } = useAuthStore();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = React.useState(false);

  return (
    <div className="flex flex-col md:flex-row min-h-screen bg-slate-100 text-slate-800">
      
      {/* BARRA DE NAVEGACIÓN SUPERIOR (Solo visible en móviles) */}
      <header className="md:hidden bg-slate-900 text-white p-4 flex justify-between items-center shadow-md">
        <h2 className="text-xl font-bold text-blue-400 tracking-wider">SIGMA-BB</h2>
        <button 
          onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
          className="text-slate-200 p-2 focus:outline-none bg-slate-800 rounded-lg"
        >
          {isMobileMenuOpen ? '✕ Cerrar' : '☰ Menú'}
        </button>
      </header>

      {/* BARRA LATERAL (Responsiva: Oculta en móvil a menos que se abra, fija en escritorio) */}
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
              onClick={() => setIsMobileMenuOpen(false)} // Cierra el menú al hacer clic en móviles
              className="block px-3 py-2 rounded-lg text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white transition-colors"
            >
                Gestión de Marcas
            </Link>
            {/* Secciones del MVP */}
          </nav>
        </div>
        
        <div className="border-t border-slate-800 pt-4 space-y-3 bg-slate-900">
          <div className="px-2 text-xs text-slate-400">
            Rol: <span className="text-blue-400 font-semibold uppercase">{user?.role}</span>
          </div>
          <button 
            onClick={logout} 
            className="w-full px-3 py-2 bg-red-600 hover:bg-red-700 text-white text-sm font-medium rounded-lg transition-colors"
          >
              Cerrar Sesión
          </button>
        </div>
      </aside>

      {/* CONTENIDO PRINCIPAL */}
      <main className="flex-1 p-4 md:p-8 overflow-y-auto">
        <Outlet />
      </main>
    </div>
  );
};

export const router = createBrowserRouter([
  {
    path: '/login',
    element: <AuthPage /> // Página de autenticación adaptada
  },
  {
    path: '/',
    element: <ProtectedRoute />, // Solo entran logueados
    children: [
      {
        element: <DashboardLayout />,
        children: [
          {
            path: '',
            element: <Navigate to="/equipments/brands" replace /> // Ruta inicial por defecto
          },
          {
            path: 'equipments/brands',
            element: <BrandManager />
          }
        ]
      }
    ]
  },
  {
    path: '/unauthorized',
    element: <div className="p-8 text-center text-red-600 font-bold">No tiene autorización.</div>
  },
  {
    path: '*',
    element: <Navigate to="/" replace />
  }
]);