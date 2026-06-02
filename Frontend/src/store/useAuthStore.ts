import { create } from 'zustand';

export type AppRole = 'SuperUsuario' | 'Administrador' | 'Ingeniero Técnico';

interface User {
  id: string;
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

const isTokenExpired = (token: string | null): boolean => {
  if (!token) return true;
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      window.atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    const decoded = JSON.parse(jsonPayload);
    if (!decoded.exp) return false;
    const currentTime = Math.floor(Date.now() / 1000);
    // Agregamos un margen de 10 segundos
    return decoded.exp < (currentTime + 10);
  } catch (e) {
    return true;
  }
};

const getInitialState = () => {
  try {
    const token = localStorage.getItem('sigma_token');
    const userStr = localStorage.getItem('sigma_user');
    
    if (token && !isTokenExpired(token) && userStr) {
      return {
        user: JSON.parse(userStr),
        isAuthenticated: true
      };
    }
  } catch (e) {
    // Ignorar errores de lectura
  }
  
  return {
    user: null,
    isAuthenticated: false
  };
};

const initialState = getInitialState();

export const useAuthStore = create<AuthState>((set) => ({
  ...initialState,
  
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