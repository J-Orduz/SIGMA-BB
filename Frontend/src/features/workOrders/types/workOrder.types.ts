// =============================================================================
// TIPOS DE RESPUESTA (Response) — Mapeados del backend work_order_hexagon
// =============================================================================

export type EstadoEjecucion = 'CREATED' | 'IN_PROGRESS' | 'EXECUTED';
export type Periodicidad = 'MONTHLY' | 'QUARTERLY' | 'BIANNUAL' | 'ANUAL';

export interface WorkOrder {
  identificadorOrdenTrabajo: string; // UUID as string
  fechaMantenimiento: string;        // ISO timestamp
  periodicidad: Periodicidad;
  identificadorIngeniero: string;    // UUID → persona (ENGINEER)
  estadoEjecucion: EstadoEjecucion;
  estadoActivo: boolean;
  equipoClienteIds: string[];        // UUIDs of equipo_cliente
}

// =============================================================================
// TIPOS DE CREACIÓN / ACTUALIZACIÓN (Request DTOs)
// =============================================================================

export interface WorkOrderCreateRequest {
  fechaMantenimiento: string;        // ISO timestamp
  periodicidad: Periodicidad;
  identificadorIngeniero: string;    // UUID
  equipoClienteIds: string[];        // UUIDs of equipo_cliente
}

export type WorkOrderUpdateRequest = WorkOrderCreateRequest;

export interface WorkOrderStatusRequest {
  estadoEjecucion: EstadoEjecucion;
}

// =============================================================================
// CONSTANTES DE DOMINIO
// =============================================================================

export const PERIODICIDAD_OPTIONS: { value: Periodicidad; label: string }[] = [
  { value: 'MONTHLY',   label: 'Mensual' },
  { value: 'QUARTERLY', label: 'Trimestral' },
  { value: 'BIANNUAL',  label: 'Semestral' },
  { value: 'ANUAL',     label: 'Anual' },
];

export const ESTADO_EJECUCION_OPTIONS: { value: EstadoEjecucion; label: string; color: string }[] = [
  { value: 'CREATED',     label: 'Creada',        color: 'status-badge--created' },
  { value: 'IN_PROGRESS', label: 'En Ejecución',  color: 'status-badge--in-progress' },
  { value: 'EXECUTED',    label: 'Ejecutada',      color: 'status-badge--executed' },
];

export const ESTADO_EJECUCION_TRANSITIONS: Record<EstadoEjecucion, EstadoEjecucion | null> = {
  CREATED: 'IN_PROGRESS',
  IN_PROGRESS: 'EXECUTED',
  EXECUTED: null,
};
