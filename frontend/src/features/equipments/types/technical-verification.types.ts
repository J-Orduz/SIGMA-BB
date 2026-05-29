export interface TechnicalVerification {
  id: string;
  description: string;
  verificationType: string;
  createdAt?: string;
}

export interface CreateTechnicalVerificationDTO {
  description: string;
  verificationType: string;
}

export interface UpdateTechnicalVerificationDTO {
  description?: string;
  verificationType?: string;
}