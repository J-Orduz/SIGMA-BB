import type { City, CreateCityDTO, UpdateCityDTO } from '../types/city.types';
import { getAuthHeaders } from '../../equipments/services/equipment-type.service';

const API_URL = 'http://localhost:8100/v1/api/cities';

export const cityService = {
  getAll: async (): Promise<City[]> => {
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('Error al obtener la lista de ciudades.');
    return response.json();
  },

  create: async (data: CreateCityDTO): Promise<City> => {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(null),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      const errMsg = await response.text().catch(() => '');
      throw new Error(errMsg || 'Error al registrar la ciudad. El código podría estar duplicado.');
    }
    return response.json();
  },

  update: async (id: string, data: UpdateCityDTO): Promise<City> => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PUT',
      headers: {
        ...getAuthHeaders(null),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al actualizar la ciudad.');
    return response.json();
  },

  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('No se pudo eliminar la ciudad.');
  },
};
