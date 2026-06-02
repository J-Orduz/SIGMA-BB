import React, { useState } from 'react';
import type { WorkOrder } from '../types/workOrder.types';
import {
  ESTADO_EJECUCION_OPTIONS,
  ESTADO_EJECUCION_TRANSITIONS,
  PERIODICIDAD_OPTIONS,
} from '../types/workOrder.types';
import type { AppRole } from '../../../store/useAuthStore';

interface ClientEquipmentDetail {
  id: string;
  serie: string;
  inventario?: string;
  modeloDisplay?: string;
}

interface WorkOrderDetailProps {
  workOrder: WorkOrder;
  engineerName?: string;
  equipmentsDetails?: ClientEquipmentDetail[];
  onClose: () => void;
  onStatusChange?: (newStatus: 'IN_PROGRESS' | 'EXECUTED') => Promise<void>;
  userRole: AppRole;
  isLoading?: boolean;
}

export const WorkOrderDetail: React.FC<WorkOrderDetailProps> = ({
  workOrder,
  engineerName,
  equipmentsDetails,
  onClose,
  onStatusChange,
  userRole,
  isLoading = false,
}) => {
  const [statusError, setStatusError] = useState<string | null>(null);

  const currentStatus = ESTADO_EJECUCION_OPTIONS.find(
    s => s.value === workOrder.estadoEjecucion
  );
  const nextStatus = ESTADO_EJECUCION_TRANSITIONS[workOrder.estadoEjecucion];
  const nextStatusLabel = nextStatus
    ? ESTADO_EJECUCION_OPTIONS.find(s => s.value === nextStatus)?.label
    : null;

  const periodicidadLabel =
    PERIODICIDAD_OPTIONS.find(p => p.value === workOrder.periodicidad)?.label ??
    workOrder.periodicidad;

  const canChangeStatus =
    onStatusChange &&
    nextStatus &&
    (userRole === 'Administrador' || userRole === 'SuperUsuario' || userRole === 'Ingeniero Técnico');

  const handleStatusChange = async () => {
    if (!onStatusChange || !nextStatus) return;
    setStatusError(null);
    try {
      await onStatusChange(nextStatus as 'IN_PROGRESS' | 'EXECUTED');
    } catch (err) {
      setStatusError(err instanceof Error ? err.message : 'Error al cambiar el estado.');
    }
  };

  return (
    <div className="wo-detail">
      {/* Header */}
      <div className="wo-detail-header">
        <div>
          <h2 className="wo-detail-title">Orden de Trabajo</h2>
          <p className="wo-detail-id">
            ID: <span>{workOrder.identificadorOrdenTrabajo}</span>
          </p>
        </div>
        <button className="wo-detail-close" onClick={onClose} title="Cerrar">✕</button>
      </div>

      {/* Estado actual */}
      <div className="wo-detail-status-row">
        <span className={`status-badge ${currentStatus?.color}`}>
          {currentStatus?.label ?? workOrder.estadoEjecucion}
        </span>
        {canChangeStatus && (
          <button
            className="wo-btn wo-btn--status"
            onClick={handleStatusChange}
            disabled={isLoading}
          >
            {isLoading ? (
              <><span className="wo-spinner-sm" /> Actualizando...</>
            ) : (
              <>▶ Avanzar a {nextStatusLabel}</>
            )}
          </button>
        )}
      </div>
      {statusError && (
        <div className="wo-form-error">
          <span>⚠️</span>
          <span>{statusError}</span>
        </div>
      )}

      {/* Información general */}
      <div className="wo-detail-grid">
        <div className="wo-detail-card">
          <div className="wo-detail-card-icon">📅</div>
          <div>
            <p className="wo-detail-card-label">Fecha de Mantenimiento</p>
            <p className="wo-detail-card-value">
              {new Date(workOrder.fechaMantenimiento).toLocaleString('es-CO', {
                dateStyle: 'long',
                timeStyle: 'short',
              })}
            </p>
          </div>
        </div>

        <div className="wo-detail-card">
          <div className="wo-detail-card-icon">🔄</div>
          <div>
            <p className="wo-detail-card-label">Periodicidad</p>
            <p className="wo-detail-card-value">{periodicidadLabel}</p>
          </div>
        </div>

        <div className="wo-detail-card">
          <div className="wo-detail-card-icon">👷</div>
          <div>
            <p className="wo-detail-card-label">Ingeniero Asignado</p>
            <p className="wo-detail-card-value">
              {engineerName ?? workOrder.identificadorIngeniero}
            </p>
          </div>
        </div>

        <div className="wo-detail-card">
          <div className="wo-detail-card-icon">🔧</div>
          <div>
            <p className="wo-detail-card-label">Equipos a Intervenir</p>
            <p className="wo-detail-card-value">
              {workOrder.equipoClienteIds.length} equipo
              {workOrder.equipoClienteIds.length !== 1 ? 's' : ''}
            </p>
          </div>
        </div>
      </div>

      {/* Lista de equipos */}
      <div className="wo-detail-section">
        <h3 className="wo-detail-section-title">Equipos del Cliente</h3>
        <div className="space-y-2 mt-2">
          {equipmentsDetails && equipmentsDetails.length > 0 ? (
            equipmentsDetails.map((eq, idx) => (
              <div key={eq.id} className="flex flex-col items-start p-3 bg-slate-50 border border-slate-200 rounded-xl gap-1 shadow-sm">
                <div className="flex items-center gap-1.5">
                  <span className="w-5 h-5 rounded-full bg-slate-200 flex items-center justify-center text-xs font-bold text-slate-600 shrink-0">{idx + 1}</span>
                  <span className="text-[10px] font-bold text-slate-400 uppercase">Serie:</span>
                  <span className="text-xs font-semibold text-slate-800">{eq.serie}</span>
                </div>
                {eq.modeloDisplay && (
                  <div className="flex items-center gap-1.5 pl-6">
                    <span className="text-[9px] font-bold text-slate-400 uppercase">Modelo:</span>
                    <span className="text-[11px] font-medium text-slate-700 bg-blue-50/60 text-blue-700 px-1.5 py-0.5 rounded">{eq.modeloDisplay}</span>
                  </div>
                )}
                {eq.inventario && (
                  <div className="flex items-center gap-1.5 pl-6">
                    <span className="text-[9px] font-bold text-slate-400 uppercase">Inventario:</span>
                    <span className="text-xs font-mono font-medium text-slate-600">{eq.inventario}</span>
                  </div>
                )}
                <div className="pl-6">
                  <span className="text-[9px] font-mono text-slate-400">ID: {eq.id}</span>
                </div>
              </div>
            ))
          ) : (
            workOrder.equipoClienteIds.length === 0 ? (
              <p className="wo-field-hint">No hay equipos asociados a esta orden.</p>
            ) : (
              workOrder.equipoClienteIds.map((id, idx) => (
                <div key={id} className="wo-equip-id-row">
                  <span className="wo-equip-id-num">{idx + 1}</span>
                  <span className="wo-equip-id-value">{id}</span>
                </div>
              ))
            )
          )}
        </div>
      </div>

      {/* Acciones */}
      <div className="wo-form-actions">
        <button className="wo-btn wo-btn--secondary" onClick={onClose}>
          Cerrar
        </button>
      </div>
    </div>
  );
};
