export interface EquipmentModel {
  id: string;
  invima: string;
  manufacturerId: string;
  equipmentId: string;
  manufacturerResponse?: {
    id: string;
    name: string;
  };
  equipmentResponse?: {
    id: string;
    name: string;
  };
}

export interface CreateModelDTO {
  invima: string;
  manufacturerId: string;
  equipmentId: string;
}

export interface UpdateModelDTO {
  invima: string;
  manufacturerId: string;
  equipmentId: string;
}