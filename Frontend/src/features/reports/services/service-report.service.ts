import { getAuthHeaders } from '../../equipments/services/equipment-type.service';
import type { CreateServiceReportDTO, ServiceReport, UpdateServiceReportDTO } from '../types/service-report.types';

const API_URL = 'http://localhost:8100/v1/api/service-reports';

const parseError = async (response: Response, fallback: string) => {
  try {
    const data = await response.json();
    return data?.message || fallback;
  } catch {
    return fallback;
  }
};

export const serviceReportService = {
  getAll: async (): Promise<ServiceReport[]> => {
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: getAuthHeaders(null),
    });

    if (!response.ok) {
      throw new Error(await parseError(response, 'Error al cargar los reportes de servicio.'));
    }

    return response.json();
  },

  create: async (data: CreateServiceReportDTO): Promise<ServiceReport> => {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error(await parseError(response, 'Error al crear el reporte de servicio.'));
    }

    return response.json();
  },

  update: async (id: string, data: UpdateServiceReportDTO): Promise<ServiceReport> => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error(await parseError(response, 'Error al actualizar el reporte de servicio.'));
    }

    return response.json();
  },

  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(null),
    });

    if (!response.ok) {
      throw new Error(await parseError(response, 'No se pudo eliminar el reporte de servicio.'));
    }
  },
};
