export interface MetrologicalData {
  value: number;
  type: string;
}

export interface EquipmentType {
  id: string;
  equipmentTypeName: string;
  technicalDefinition: string;
  careRecommendations: string;
  voltage: number | null;
  amperage: number | null;
  predominantTechnology: string;
  verifiable: boolean;
  unitMaintenanceValue: number;
  metrologicalData: MetrologicalData[];
}

// DTO para el POST y PUT
export interface CreateEquipmentTypeDTO {
  equipmentTypeName: string;
  technicalDefinition: string;
  careRecommendations: string;
  voltage: number | null;
  amperage: number | null;
  predominantTechnology: string;
  verifiable: boolean;
  unitMaintenanceValue: number;
  metrologicalData: MetrologicalData[];
}