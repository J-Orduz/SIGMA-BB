import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';

interface ProtectedRouteProps {
  allowedRoles?: ('SuperUsuario' | 'Administrador' | 'Ingeniero Técnico')[];
}

// Si no esta logueado, manda al /login
export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ allowedRoles }) => {
  const { isAuthenticated, user } = useAuthStore();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && user && !allowedRoles.includes(user.role)) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />; // Renderiza las rutas hijas si pasa las validaciones
};