import type { Brand, CreateBrandDTO } from '../types/brand.types';

const BASE_URL = 'http://localhost:8100/v1/api/brands';

// Función auxiliar para generar las cabeceras base que incluyen la autenticación
const getAuthHeaders = (): Record<string, string> => {
  const token = localStorage.getItem('sigma_token');
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
  };
  
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  
  return headers;
};

export const brandService = {
  // Obtener todas las marcas
  getAll: async (): Promise<Brand[]> => {
    const response = await fetch(BASE_URL, {
      method: 'GET',
      headers: getAuthHeaders(), // Agregar headers de autenticación
    });
    if (!response.ok) throw new Error('Error al cargar las marcas');
    return response.json();
  },

  // Crear una nueva marca
  create: async (data: CreateBrandDTO): Promise<Brand> => {
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: getAuthHeaders(), // Agregar headers de autenticación
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al crear la marca');
    return response.json();
  },

  // Modificar Marca
  update: async (id: string, name: string): Promise<Brand> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: 'PUT',
        headers: getAuthHeaders(), // Agregar headers de autenticación
        body: JSON.stringify({ name }),
    });

    if (!response.ok) {
        throw new Error('No se pudo actualizar la marca');
    }

    return response.json();
  },

  // Eliminar Marca
  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: 'DELETE',
        headers: getAuthHeaders(), // Agregar headers de autenticación
    });

    if (!response.ok) {
        throw new Error('No se pudo eliminar la marca. Verifique si está asociada a algún equipo.');
    }
  }
};