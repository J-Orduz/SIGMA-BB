import type { ServiceArea, ServiceAreaCreateRequest, ClientEquipmentCreateRequest } from '../types/client.types';

const BASE_URL = 'http://localhost:8100/service-area/v1/api';

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

export const serviceAreaService = {
  // Obtener todas las áreas de servicio
  getAll: async (): Promise<ServiceArea[]> => {
    const response = await fetch(BASE_URL, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('Error al cargar las áreas de servicio.');
    return response.json();
  },

  // Obtener un área de servicio por su UUID
  getById: async (id: string): Promise<ServiceArea> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('No se encontró el área de servicio solicitada.');
    return response.json();
  },

  // Crear una nueva área de servicio
  create: async (data: ServiceAreaCreateRequest): Promise<ServiceArea> => {
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      const errorBody = await response.text().catch(() => '');
      throw new Error(errorBody || 'Error al registrar el área de servicio.');
    }
    return response.json();
  },

  // Actualizar un área de servicio existente
  update: async (id: string, data: ServiceAreaCreateRequest): Promise<ServiceArea> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('No se pudo actualizar el área de servicio. Intente nuevamente.');
    return response.json();
  },

  // Eliminar un área de servicio por su UUID
  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) {
      throw new Error('No es posible eliminar esta área de servicio. Puede tener equipos asociados.');
    }
  },

  // ─── Gestión de Equipos de Cliente (Solución 2) ──────────────────────────
  
  // Agregar un equipo a un área de servicio
  addEquipment: async (areaId: string, data: ClientEquipmentCreateRequest): Promise<ServiceArea> => {
    const response = await fetch(`http://localhost:8100/service-area/equipment/v1/api/${areaId}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      const errMsg = await response.text().catch(() => '');
      throw new Error(errMsg || 'Error al agregar el equipo al área de servicio.');
    }
    return response.json();
  },

  // Actualizar un equipo existente dentro de un área de servicio
  updateEquipment: async (areaId: string, equipId: string, data: ClientEquipmentCreateRequest): Promise<ServiceArea> => {
    const response = await fetch(`http://localhost:8100/service-area/equipment/v1/api/${areaId}/${equipId}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      const errMsg = await response.text().catch(() => '');
      throw new Error(errMsg || 'Error al actualizar el equipo biomédico.');
    }
    return response.json();
  },

  // Eliminar (desactivar) un equipo biomédico
  deleteEquipment: async (areaId: string, equipId: string): Promise<ServiceArea> => {
    const response = await fetch(`http://localhost:8100/service-area/equipment/v1/api/${areaId}/${equipId}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) {
      const errMsg = await response.text().catch(() => '');
      throw new Error(errMsg || 'Error al desvincular el equipo biomédico.');
    }
    return response.json();
  },
};

