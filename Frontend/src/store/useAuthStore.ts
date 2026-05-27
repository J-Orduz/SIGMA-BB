import { create } from 'zustand';

export type AppRole = 'SuperUsuario' | 'Administrador' | 'Ingeniero Técnico';

interface User {
  username: string;
  name: string;
  role: AppRole;
  token: string;
}

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  login: (userData: User) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  // Intentar recuperar sesión existente al recargar la página
  user: localStorage.getItem('sigma_user') ? JSON.parse(localStorage.getItem('sigma_user')!) : null,
  isAuthenticated: !!localStorage.getItem('sigma_token'),
  
  login: (userData) => {
    localStorage.setItem('sigma_token', userData.token);
    localStorage.setItem('sigma_user', JSON.stringify(userData));
    set({ user: userData, isAuthenticated: true });
  },
  logout: () => {
    localStorage.removeItem('sigma_token');
    localStorage.removeItem('sigma_user');
    set({ user: null, isAuthenticated: false });
  },
}));