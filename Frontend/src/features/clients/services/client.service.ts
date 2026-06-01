import type { Client, ClientCreateRequest } from '../types/client.types';

const BASE_URL = 'http://localhost:8100/client/v1/api';

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

export const clientService = {
  // Obtener todos los clientes
  getAll: async (): Promise<Client[]> => {
    const response = await fetch(BASE_URL, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('Error al cargar los clientes.');
    return response.json();
  },

  // Obtener un cliente por su identificador (NIT)
  getById: async (id: string): Promise<Client> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('No se encontró el cliente solicitado.');
    return response.json();
  },

  // Crear un nuevo cliente
  create: async (data: ClientCreateRequest): Promise<Client> => {
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      const errorBody = await response.text().catch(() => '');
      throw new Error(errorBody || 'Error al registrar el cliente. Verifique que el NIT no esté duplicado.');
    }
    return response.json();
  },

  // Actualizar un cliente existente
  update: async (id: string, data: ClientCreateRequest): Promise<Client> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('No se pudo actualizar el cliente. Intente nuevamente.');
    return response.json();
  },

  // Eliminar un cliente por su identificador
  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) {
      throw new Error('No es posible eliminar este cliente. Puede tener registros asociados en el sistema.');
    }
  },
};
