export interface PhonePerson {
  idTelefonoPersona: string; // UUID as string
  telefonoPersona: string;
}

export interface EmailPerson {
  idCorreoPersona: string; // UUID as string
  correoPersona: string;
}

export interface Person {
  identificador: string; // UUID as string
  cedula: string;
  primerNombre: string;
  segundoNombre?: string;
  primerApellido: string;
  segundoApellido?: string;
  tipoPersona: string;
  segundoTipoPersona?: string;
  estadoActivo: boolean;
  phonePersonList: PhonePerson[];
  emailPersonList: EmailPerson[];
}

export interface PhonePersonCreateRequest {
  telefonoPersona: string;
}

export interface EmailPersonCreateRequest {
  correoPersona: string;
}

export interface PersonCreateRequest {
  cedula: string;
  primerNombre: string;
  segundoNombre?: string;
  primerApellido: string;
  segundoApellido?: string;
  nombreUsuario: string;
  password: string;
  tipoPersona?: string;
  phonePersonList?: PhonePersonCreateRequest[];
  emailPersonList?: EmailPersonCreateRequest[];
}

export interface PersonUpdateRequest {
  cedula: string;
  primerNombre: string;
  segundoNombre?: string;
  primerApellido: string;
  segundoApellido?: string;
  tipoPersona: string;
  segundoTipoPersona?: string;
  phonePersonList?: PhonePersonCreateRequest[];
  emailPersonList?: EmailPersonCreateRequest[];
}
