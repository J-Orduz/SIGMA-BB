import type { Brand, CreateBrandDTO } from '../types/brand.types';

const BASE_URL = 'http://localhost:8100/v1/api/brands';

export const brandService = {
  // Obtener todas las marcas
  getAll: async (): Promise<Brand[]> => {
    const response = await fetch(BASE_URL);
    if (!response.ok) throw new Error('Error al cargar las marcas');
    return response.json();
  },

  // Crear una nueva marca
  create: async (data: CreateBrandDTO): Promise<Brand> => {
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al crear la marca');
    return response.json();
  },

  // Modificar Marca
  update: async (id: string, name: string): Promise<Brand> => {
    const response = await fetch(`http://localhost:8100/v1/api/brands/${id}`, {
        method: 'PUT',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name }),
    });

    if (!response.ok) {
        throw new Error('No se pudo actualizar la marca');
    }

    return response.json();
  },

  // Eliminar Marca
  delete: async (id: string): Promise<void> => {
    const response = await fetch(`http://localhost:8100/v1/api/brands/${id}`, {
        method: 'DELETE',
    });

    if (!response.ok) {
        throw new Error('No se pudo eliminar la marca. Verifique si está asociada a algún equipo.');
    }
   }
};