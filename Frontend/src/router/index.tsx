import { createBrowserRouter, Navigate } from 'react-router-dom';
import { AuthPage } from '../features/auth/AuthPage';
import { BrandManager } from '../features/equipments/components/BrandManager';
import { EquipmentTypeManager } from '../features/equipments/components/EquipmentTypeManager';
import { TechnicalVerificationManager } from '../features/equipments/components/TechnicalVerificationManager';
import { EquipmentManager } from '../features/equipments/components/EquipmentManager';
import { CountryManager } from '../features/locations/components/CountryManager';
import { ManufacturerManager } from '../features/manufacturers/components/ManufacturerManager';
import { ModelManager } from '../features/equipments/components/ModelManager';
import { ClientManager } from '../features/clients/components/ClientManager';
import { ClientDetailPage } from '../features/clients/components/ClientDetailPage';
import { PersonManager } from '../features/persons/components/PersonManager';
import { ServiceReportManager } from '../features/reports/components/ServiceReportManager';
import { ProtectedRoute } from './ProtectedRoute';
import { useAuthStore } from '../store/useAuthStore';
import { Link, Outlet } from 'react-router-dom';
import React from 'react';
import { AuthCelebration } from '../features/auth/components/AuthCelebration';

const DashboardLayout = () => {
  const { user, logout } = useAuthStore();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = React.useState(false);
  const [goodbyeName, setGoodbyeName] = React.useState<string | null>(null);
  const logoutTimerRef = React.useRef<number | null>(null);

  React.useEffect(() => {
    return () => {
      if (logoutTimerRef.current) {
        window.clearTimeout(logoutTimerRef.current);
      }
    };
  }, []);

  const handleLogout = () => {
    if (goodbyeName) return;

    setIsMobileMenuOpen(false);
    setGoodbyeName(user?.name || 'Usuario');

    logoutTimerRef.current = window.setTimeout(() => {
      logout();
    }, 2500);
  };

  const navLinkClass = 'sigma-sidebar-link block px-3 py-2 rounded-xl text-sm font-semibold text-slate-300 hover:text-white transition-all';

  return (
    <div className="flex flex-col md:flex-row min-h-screen bg-slate-100 text-slate-800">
      {/* BARRA DE NAVEGACIÓN SUPERIOR (Móviles) */}
      <header className="sigma-mobile-header md:hidden text-white p-4 flex justify-between items-center shadow-xl">
        <div>
          <h2 className="text-xl font-bold text-white tracking-wider">SIGMA-BB</h2>
          <p className="text-xs text-blue-100">Bioingeniería</p>
        </div>
        <button
          onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
          className="sigma-mobile-menu-button text-slate-100 px-3 py-2 focus:outline-none rounded-xl"
        >
          {isMobileMenuOpen ? 'Cerrar' : 'Menú'}
        </button>
      </header>

      {/* BARRA LATERAL */}
      <aside className={`
        ${isMobileMenuOpen ? 'block' : 'hidden'} 
        sigma-sidebar md:flex w-full md:w-72 text-slate-200 flex-col justify-between p-4 fixed md:sticky top-[72px] md:top-0 h-[calc(100vh-72px)] md:h-screen z-50 transition-all duration-300 overflow-y-auto
      `}>
        <div className="space-y-6">
          <div className="sigma-sidebar-brand hidden md:block px-3 py-4">
            <h2 className="text-2xl font-black text-white tracking-wider">SIGMA-BB</h2>
            <p className="text-xs text-blue-100">Bioingeniería</p>
          </div>
          <nav className="space-y-1.5">
            <Link
              to="/equipments/brands"
              onClick={() => setIsMobileMenuOpen(false)}
              className={navLinkClass}
            >
              Gestión de Marcas
            </Link>

            {/* Control visual de Sidebar: Sólo Administrador o SuperUsuario ven la opción */}
            {(user?.role === 'Administrador' || user?.role === 'SuperUsuario') && (
              <>
                <Link
                  to="/equipments/types"
                  onClick={() => setIsMobileMenuOpen(false)}
                  className={navLinkClass}
                >
                  Tipos de Equipos
                </Link>
                <Link
                  to="/equipments/technical-verifications"
                  onClick={() => setIsMobileMenuOpen(false)}
                  className={navLinkClass}
                >
                  Verificaciones Técnicas
                </Link>
                <Link
                  to="/equipments/list"
                  onClick={() => setIsMobileMenuOpen(false)}
                  className={navLinkClass}
                >
                  Inventario de Equipos
                </Link>
                <Link
                  to="/countries"
                  onClick={() => setIsMobileMenuOpen(false)}
                  className={navLinkClass}
                >
                  Gestión de Ubicaciones
                </Link>
                <Link
                  to="/manufacturers"
                  onClick={() => setIsMobileMenuOpen(false)}
                  className={navLinkClass}
                >
                  Gestión de Fabricantes
                </Link>
                <Link
                  to="/equipments/models"
                  onClick={() => setIsMobileMenuOpen(false)}
                  className={navLinkClass}
                >
                  Gestión de Modelos
                </Link>
              </>
            )}

            {/* Gestión de Clientes: Solo Administrador o SuperUsuario */}
            {(user?.role === 'Administrador' || user?.role === 'SuperUsuario') && (
              <>
                <Link
                  to="/clients"
                  onClick={() => setIsMobileMenuOpen(false)}
                  className={navLinkClass}
                >
                  Gestión de Clientes
                </Link>
                <Link
                  to="/persons"
                  onClick={() => setIsMobileMenuOpen(false)}
                  className={navLinkClass}
                >
                  Gestión de Personas
                </Link>
              </>
            )}
            {(user?.role === 'Administrador' || user?.role === 'SuperUsuario' || user?.role === 'Ingeniero Técnico') && (
              <Link
                to="/service-reports"
                onClick={() => setIsMobileMenuOpen(false)}
                className={navLinkClass}
              >
                Reportes de Servicio
              </Link>
            )}
          </nav>
        </div>

        <div className="sigma-sidebar-user mt-6 space-y-3">
          <div className="px-2 text-xs text-slate-300">
            <span className="text-slate-400 uppercase font-bold">Usuario</span>
            <span className="text-white font-semibold block truncate">{user?.name}</span>
            <span className="text-slate-400 uppercase font-bold block mt-2">Rol</span>
            <span className="text-blue-100 font-semibold uppercase block">{user?.role}</span>
          </div>
          <button
            onClick={handleLogout}
            className="sigma-sidebar-logout w-full px-3 py-2 text-white text-sm font-semibold rounded-xl transition-all"
          >
            Cerrar Sesión
          </button>
        </div>
      </aside>

      <main className="flex-1 p-4 md:p-8 overflow-y-auto">
        <Outlet />
      </main>
      {goodbyeName && <AuthCelebration mode="goodbye" name={goodbyeName} />}
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
              },
              {
                path: 'equipments/list',
                element: <EquipmentManager />
              },
              {
                path: 'countries',
                element: <CountryManager />
              },
              {
                path: 'manufacturers',
                element: <ManufacturerManager />
              },
              {
                path: 'equipments/models',
                element: <ModelManager />
              },
            ]
          },
          // Módulo de Gestión de Clientes — solo SuperUsuario y Administrador
          {
            element: <ProtectedRoute allowedRoles={['SuperUsuario', 'Administrador', 'Ingeniero Técnico']} />,
            children: [
              {
                path: 'service-reports',
                element: <ServiceReportManager />
              }
            ]
          },
          {
            element: <ProtectedRoute allowedRoles={['SuperUsuario', 'Administrador']} />,
            children: [
              {
                path: 'clients',
                element: <ClientManager />
              },
              {
                path: 'clients/:clientId',
                element: <ClientDetailPage />
              },
              {
                path: 'persons',
                element: <PersonManager />
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
