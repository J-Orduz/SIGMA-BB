import type { 
  TechnicalVerification, 
  CreateTechnicalVerificationDTO, 
  UpdateTechnicalVerificationDTO 
} from '../types/technical-verification.types';

const API_URL = 'http://localhost:8100/v1/api/technical-verifications';

const getAuthHeaders = (contentType: string | null = 'application/json') => {
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

export const technicalVerificationService = {
  async getAll(): Promise<TechnicalVerification[]> {
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: getAuthHeaders(null)
    });
    if (!response.ok) throw new Error('Error al obtener las verificaciones técnicas.');
    return response.json();
  },

  async create(data: CreateTechnicalVerificationDTO): Promise<TechnicalVerification> {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al registrar la verificación técnica.');
    return response.json();
  },

  async update(id: string, data: UpdateTechnicalVerificationDTO): Promise<TechnicalVerification> {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PATCH',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Error al actualizar la verificación técnica.');
    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_URL}/${id}`, { 
      method: 'DELETE',
      headers: getAuthHeaders(null)
    });
    if (!response.ok) {
      throw new Error('No se pudo eliminar la verificación técnica. Puede estar vinculada a un tipo de equipo.');
    }
  }
};