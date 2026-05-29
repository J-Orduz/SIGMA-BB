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

export interface ServiceArea {
  identificadorAreaServicio: string; // UUID as string
  nombreAreaServicio: string;
  estadoActivo: boolean;
  identificadorSede: string; // UUID as string
  managerList: Manager[];
  // clientEquipmentList omitido en MVP
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
  // clientEquipmentList omitido en MVP
}

export interface HeadquarterCreateRequest {
  nombreSede: string;
  direccionCalleSede: string;
  direccionCarreraSede: string;
  direccionNumeroSede: string;
  identificadorCiudad: string;
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
  { value: 'CC', label: 'Cédula de Ciudadanía' },
  { value: 'CE', label: 'Cédula de Extranjería' },
] as const;

export type TipoIdentificacion = typeof TIPO_IDENTIFICACION_OPTIONS[number]['value'];
