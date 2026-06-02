// =============================================================================
// TIPOS DE RESPUESTA (Response) — Mapeados del backend client_hexagon
// =============================================================================

export interface EmailClient {
  identificadorCorreoCliente: string; // UUID as string
  correoCliente: string;
  estadoActivo: boolean;
}

export interface PhoneClient {
  identificadorTelefonoCliente: string; // UUID as string
  telefonoCliente: string;
  estadoActivo: boolean;
}

export interface Manager {
  identificadorEncargado: string; // UUID as string
  tipoEncargado: string;
  estadoActivo: boolean;
}

export interface ClientEquipment {
  identificadorEquipoCliente: string; // UUID
  serie: string;
  fechaCompra?: string;
  valorCompra?: number;
  numeroInventario?: string;
  estadoActivo: boolean;
  identificadorAreaServicio: string;
  identificadorModelo?: string;
}

export interface ClientEquipmentCreateRequest {
  serie: string;
  fechaCompra?: string;
  valorCompra?: number;
  numeroInventario?: string;
  identificadorModelo?: string;
}

export interface ServiceArea {
  identificadorAreaServicio: string; // UUID as string
  nombreAreaServicio: string;
  estadoActivo: boolean;
  identificadorSede: string; // UUID as string
  managerList: Manager[];
  clientEquipmentList?: ClientEquipment[];
}


export interface Headquarter {
  identificadorSede: string; // UUID as string
  nombreSede: string;
  direccionCalleSede: string;
  direccionCarreraSede: string;
  direccionNumeroSede: string;
  estadoActivo: boolean;
  identificadorCiudad: string;
  serviceAreaList: ServiceArea[];
  encargado: Manager | null;
  managerList?: Manager[];
}

export interface Client {
  identificadorCliente: string; // NIT — clave primaria
  tipoIdentifiacion: string;
  razonSocial: string;
  estadoActivo: boolean;
  identificadorPais: string;
  correosCliente: EmailClient[];
  telefonosCliente: PhoneClient[];
  headquarterClientList: Headquarter[];
  identificadorRepresentante?: string;
}

// =============================================================================
// TIPOS DE CREACIÓN (Request DTOs) — Mapeados del backend client_hexagon
// =============================================================================

export interface EmailClientCreateRequest {
  correoCliente: string;
}

export interface PhoneClientCreateRequest {
  telefonoCliente: string;
}

export interface ManagerCreateRequest {
  tipoEncargado: string;
}

export interface ServiceAreaCreateRequest {
  nombreAreaServicio: string;
  managerList?: ManagerCreateRequest[];
  identificadorSede: string; // UUID as string linking to Sede
  clientEquipmentList?: ClientEquipmentCreateRequest[];
}

export interface HeadquarterCreateRequest {
  nombreSede: string;
  direccionCalleSede: string;
  direccionCarreraSede: string;
  direccionNumeroSede: string;
  identificadorCiudad: string;
  identificadorCliente: string; // NIT linking to Client
  serviceAreaList?: ServiceAreaCreateRequest[];
  managerList?: ManagerCreateRequest[];
}

export interface ClientCreateRequest {
  identificadorCliente: string;
  tipoIdentifiacion: string;
  razonSocial: string;
  identificadorPais: string;
  emailClientList?: EmailClientCreateRequest[];
  phoneClientList?: PhoneClientCreateRequest[];
  headquarterList?: HeadquarterCreateRequest[];
  identificadorRepresentante?: string;
}

// =============================================================================
// TIPOS DE ACTUALIZACIÓN — Reutiliza el mismo DTO que creación (como el backend)
// =============================================================================

export type ClientUpdateRequest = ClientCreateRequest;
export type HeadquarterUpdateRequest = HeadquarterCreateRequest;
export type ServiceAreaUpdateRequest = ServiceAreaCreateRequest;

// =============================================================================
// CONSTANTES DE DOMINIO
// =============================================================================

export const TIPO_IDENTIFICACION_OPTIONS = [
  { value: 'NIT_juridico', label: 'NIT (Persona Jurídica)' },
  { value: 'NIT_natural', label: 'NIT (Persona Natural)' },
] as const;

export type TipoIdentificacion = typeof TIPO_IDENTIFICACION_OPTIONS[number]['value'];
