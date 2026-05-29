export interface Equipment {
  id: string;
  equipmentTypeId: string;
  brandId: string;
  equipmentType?: {
    id: string;
    equipmentTypeName: string;
  };
  brand?: {
    id: string;
    brandName: string;
  };
}

export interface CreateEquipmentDTO {
  equipmentTypeId: string;
  brandId: string;
}