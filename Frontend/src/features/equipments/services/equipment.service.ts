
import type { Equipment, CreateEquipmentDTO } from '../types/equipment.types';
import { getAuthHeaders } from './equipment-type.service';

const API_URL = 'http://localhost:8100/v1/api/equipment';

export const equipmentService = {
  async getAll(): Promise<Equipment[]> {
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: getAuthHeaders(null)
    });
    if (!response.ok) throw new Error('Error al obtener el listado de equipos.');
    return response.json();
  },

  async create(data: CreateEquipmentDTO): Promise<Equipment> {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data)
    });
    if (!response.ok) throw new Error('Error al registrar el equipo.');
    return response.json();
  },

  async update(id: string, data: CreateEquipmentDTO): Promise<Equipment> {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PATCH', // Usamos PATCH como requiere tu backend
      headers: getAuthHeaders(),
      body: JSON.stringify(data)
    });
    if (!response.ok) throw new Error('Error al actualizar el equipo.');
    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null)
    });
    if (!response.ok) throw new Error('Error al eliminar el equipo.');
  }
};