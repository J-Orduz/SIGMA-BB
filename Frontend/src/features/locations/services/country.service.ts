import type { Country, CreateCountryDTO, UpdateCountryDTO } from '../types/country.types';
import { getAuthHeaders } from '../../equipments/services/equipment-type.service';

const API_URL = 'http://localhost:8100/v1/api/countries';

export const countryService = {
  getAll: async (): Promise<Country[]> => {
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('Error al obtener la lista de países.');
    return response.json();
  },

  create: async (data: CreateCountryDTO): Promise<Country> => {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(null),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al registrar el país. El código podría estar duplicado.');
    return response.json();
  },

  update: async (id: string, data: UpdateCountryDTO): Promise<Country> => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PATCH',
      headers: {
        ...getAuthHeaders(null),
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al actualizar el país.');
    return response.json();
  },

  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('No se pudo eliminar el país.');
  },
};