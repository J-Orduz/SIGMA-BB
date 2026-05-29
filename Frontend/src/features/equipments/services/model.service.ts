import type { EquipmentModel, CreateModelDTO, UpdateModelDTO } from '../types/model.types';
import { getAuthHeaders } from './equipment-type.service';

const API_URL = 'http://localhost:8100/v1/api/models';

export const modelService = {
  getAll: async (): Promise<EquipmentModel[]> => {
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('Error al obtener la lista de modelos.');
    return response.json();
  },

  create: async (data: CreateModelDTO): Promise<EquipmentModel> => {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: {
        ...getAuthHeaders(null),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al registrar el modelo biomédico.');
    return response.json();
  },

  update: async (id: string, data: UpdateModelDTO): Promise<EquipmentModel> => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PATCH',
      headers: {
        ...getAuthHeaders(null),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al actualizar el modelo.');
    return response.json();
  },

  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('No se pudo eliminar el modelo seleccionado.');
  },
};