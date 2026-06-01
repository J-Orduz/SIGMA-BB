export interface ServiceReport {
  id: string;
  workOrderId: string;
  clientEquipmentId: string;
  observations?: string | null;
  technicalVerificationResult?: string | null;
}

export interface CreateServiceReportDTO {
  workOrderId: string;
  clientEquipmentId: string;
  observations?: string;
  technicalVerificationResult?: string;
}

export interface UpdateServiceReportDTO extends CreateServiceReportDTO {}
