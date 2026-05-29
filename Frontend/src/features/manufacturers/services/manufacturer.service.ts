import type { Manufacturer, CreateManufacturerDTO, UpdateManufacturerDTO } from '../types/manufacturer.types';
import { getAuthHeaders } from '../../equipments/services/equipment-type.service';

const API_URL = 'http://localhost:8100/v1/api/manufacturers';

export const manufacturerService = {
  getAll: async (): Promise<Manufacturer[]> => {
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('Error al obtener la lista de fabricantes.');
    return response.json();
  },

  create: async (data: CreateManufacturerDTO): Promise<Manufacturer> => {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(null),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al registrar el fabricante.');
    return response.json();
  },

  update: async (id: string, data: UpdateManufacturerDTO): Promise<Manufacturer> => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PATCH',
      headers: {
        ...getAuthHeaders(null),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al actualizar el fabricante.');
    return response.json();
  },

  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('No se pudo eliminar el fabricante.');
  },
};