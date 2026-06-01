import type { Headquarter, HeadquarterCreateRequest } from '../types/client.types';

const BASE_URL = 'http://localhost:8100/headquarter/v1/api';

// Función auxiliar para generar cabeceras con autenticación JWT
const getAuthHeaders = (contentType: string | null = 'application/json'): Record<string, string> => {
  const token = localStorage.getItem('sigma_token');
  const headers: Record<string, string> = {};

  if (contentType) {
    headers['Content-Type'] = contentType;
  }
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  return headers;
};

export const headquarterService = {
  // Obtener todas las sedes
  getAll: async (): Promise<Headquarter[]> => {
    const response = await fetch(BASE_URL, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('Error al cargar las sedes.');
    return response.json();
  },

  // Obtener una sede por su UUID
  getById: async (id: string): Promise<Headquarter> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('No se encontró la sede solicitada.');
    return response.json();
  },

  // Crear una nueva sede
  create: async (data: HeadquarterCreateRequest): Promise<Headquarter> => {
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      const errorBody = await response.text().catch(() => '');
      throw new Error(errorBody || 'Error al registrar la sede. Verifique los datos ingresados.');
    }
    return response.json();
  },

  // Actualizar una sede existente
  update: async (id: string, data: HeadquarterCreateRequest): Promise<Headquarter> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('No se pudo actualizar la sede. Intente nuevamente.');
    return response.json();
  },

  // Eliminar una sede por su UUID
  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) {
      throw new Error('No es posible eliminar esta sede. Puede tener áreas de servicio o equipos asociados.');
    }
  },
};
