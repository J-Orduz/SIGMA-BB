import type { EquipmentType, CreateEquipmentTypeDTO } from '../types/equipment-type.types';

const API_URL = 'http://localhost:8100/v1/api/equipment-types';

// Función auxiliar para obtener las cabeceras con el Token JWT
export const getAuthHeaders = (contentType: string | null = 'application/json') => {
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

export const equipmentTypeService = {
  async getAll(): Promise<EquipmentType[]> {
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: getAuthHeaders(null)
    });
    if (!response.ok) throw new Error('Error al obtener los tipos de equipos.');
    return response.json();
  },

  async getById(id: string): Promise<EquipmentType> {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'GET',
      headers: getAuthHeaders(null)
    });
    if (!response.ok) throw new Error('No se pudo encontrar el tipo de equipo.');
    return response.json();
  },

  async create(data: CreateEquipmentTypeDTO): Promise<EquipmentType> {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al registrar el tipo de equipo.');
    return response.json();
  },

  async update(id: string, data: CreateEquipmentTypeDTO): Promise<EquipmentType> {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al actualizar el tipo de equipo.');
    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_URL}/${id}`, { 
      method: 'DELETE',
      headers: getAuthHeaders(null)
    });
    if (!response.ok) {
      throw new Error('No se puede eliminar. Este tipo de equipo ya está asociado a un equipo cliente.');
    }
  }
};

export const removeTechnicalVerification = async (equipmentTypeId: string, technicalVerificationId: string) => {
  const headers = getAuthHeaders(null);

  const response = await fetch(`http://localhost:8100/v1/api/equipment-types/${equipmentTypeId}/technical-verification`, {
    method: 'DELETE',
    headers: {
      ...headers,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(technicalVerificationId), 
  });

  if (!response.ok) {
    throw new Error('Error al remover la verificación técnica');
  }

  return response.json();
};