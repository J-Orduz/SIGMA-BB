import type { Person, PersonCreateRequest, PersonUpdateRequest } from '../types/person.types';

const BASE_URL = 'http://localhost:8100/person';

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

export const personService = {
  // Obtener todas las personas registradas
  getAll: async (): Promise<Person[]> => {
    const response = await fetch(`${BASE_URL}/v1/api`, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('Error al obtener la lista de personas.');
    return response.json();
  },

  // Obtener una persona por su identificador único
  getById: async (id: string): Promise<Person> => {
    const response = await fetch(`${BASE_URL}/v1/api/${id}`, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('No se encontró la persona solicitada.');
    return response.json();
  },

  // Crear una nueva persona según su tipo/rol
  create: async (data: PersonCreateRequest, role: 'CEO_CLIENT' | 'ENGINEER' | 'ADMIN'): Promise<Person> => {
    let url = `${BASE_URL}/v1/api`; // Básico/CEO_CLIENT por defecto

    if (role === 'ENGINEER') {
      url = `${BASE_URL}/vi/api/register/engineer`; // Nota: el endpoint tiene la tipografía 'vi' en vez de 'v1'
    } else if (role === 'ADMIN') {
      url = `${BASE_URL}/v1/api/register/admin`;
    }

    const response = await fetch(url, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorBody = await response.text().catch(() => '');
      throw new Error(errorBody || 'Error al registrar la persona en el sistema.');
    }
    return response.json();
  },

  // Actualizar una persona existente
  update: async (id: string, data: PersonUpdateRequest): Promise<Person> => {
    const response = await fetch(`${BASE_URL}/v1/api/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('No se pudo actualizar la persona. Intente nuevamente.');
    return response.json();
  },

  // Eliminar (soft-delete, b_estado_activo = false) una persona
  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${BASE_URL}/v1/api/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) {
      throw new Error('No es posible dar de baja a esta persona.');
    }
  },
};
