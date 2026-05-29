export interface Manufacturer {
  id: string;
  name: string;
  countryId: string;
  countryResponse?: {
    id: string;
    name: string;
  };
}

export interface CreateManufacturerDTO {
  name: string;
  countryId: string;
}

export interface UpdateManufacturerDTO {
  name: string;
  countryId: string;
}