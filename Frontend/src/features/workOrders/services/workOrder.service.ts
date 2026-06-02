import type {
  WorkOrder,
  WorkOrderCreateRequest,
  WorkOrderStatusRequest,
} from '../types/workOrder.types';

const BASE_URL = 'http://localhost:8100/work-order/v1/api';

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

export const workOrderService = {
  // Obtener todas las órdenes de trabajo activas
  getAll: async (): Promise<WorkOrder[]> => {
    const response = await fetch(BASE_URL, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('Error al cargar las órdenes de trabajo.');
    return response.json();
  },

  // Obtener una orden de trabajo por su UUID
  getById: async (id: string): Promise<WorkOrder> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('No se encontró la orden de trabajo solicitada.');
    return response.json();
  },

  // Obtener órdenes de trabajo asignadas a un ingeniero
  getByEngineerId: async (engineerId: string): Promise<WorkOrder[]> => {
    const response = await fetch(`${BASE_URL}/engineer/${engineerId}`, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) throw new Error('Error al cargar las órdenes del ingeniero.');
    return response.json();
  },

  // Crear una nueva orden de trabajo
  create: async (data: WorkOrderCreateRequest): Promise<WorkOrder> => {
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      const errorBody = await response.text().catch(() => '');
      throw new Error(errorBody || 'Error al crear la orden de trabajo.');
    }
    return response.json();
  },

  // Actualizar una orden de trabajo existente
  update: async (id: string, data: WorkOrderCreateRequest): Promise<WorkOrder> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      const errorBody = await response.text().catch(() => '');
      throw new Error(errorBody || 'No se pudo actualizar la orden de trabajo.');
    }
    return response.json();
  },

  // Actualizar únicamente el estado de ejecución
  updateStatus: async (id: string, data: WorkOrderStatusRequest): Promise<WorkOrder> => {
    const response = await fetch(`${BASE_URL}/${id}/status`, {
      method: 'PATCH',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      const errorBody = await response.text().catch(() => '');
      throw new Error(errorBody || 'No se pudo actualizar el estado de la orden.');
    }
    return response.json();
  },

  // Eliminar una orden de trabajo (soft delete)
  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });
    if (!response.ok) {
      throw new Error('No es posible eliminar esta orden de trabajo. Puede tener reportes asociados.');
    }
  },
};
