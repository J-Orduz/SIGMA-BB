import React, { useState, useMemo } from 'react';
import { useWorkOrders } from '../hooks/useWorkOrders';
import { WorkOrderForm } from './WorkOrderForm';
import { WorkOrderDetail } from './WorkOrderDetail';
import type { WorkOrder, WorkOrderCreateRequest, EstadoEjecucion } from '../types/workOrder.types';
import { ESTADO_EJECUCION_OPTIONS } from '../types/workOrder.types';
import { useAuthStore } from '../../../store/useAuthStore';
import { useClients } from '../../clients/hooks/useClients';
import { useModels } from '../../equipments/hooks/useModels';
import { usePersons } from '../../persons/hooks/usePersons';
import { useEquipments } from '../../equipments/hooks/useEquipments';

export const WorkOrderManager: React.FC = () => {
  const { user } = useAuthStore();
  const isAdmin = user?.role === 'Administrador' || user?.role === 'SuperUsuario';
  const isEngineer = user?.role === 'Ingeniero Técnico';

  const { clients } = useClients();
  const { models } = useModels();
  const { persons } = usePersons();
  const { equipments } = useEquipments();

  // Si el usuario es ingeniero, solo carga sus órdenes
  const { workOrders, isLoading, error, setError, createWorkOrder, updateWorkOrder, updateWorkOrderStatus, deleteWorkOrder } =
    useWorkOrders(isEngineer && user ? user.id : undefined);

  // ── UI State ───────────────────────────────────────────────
  const [view, setView] = useState<'list' | 'form' | 'detail'>('list');
  const [selectedWorkOrder, setSelectedWorkOrder] = useState<WorkOrder | null>(null);
  const [editingWorkOrder, setEditingWorkOrder] = useState<WorkOrder | null>(null);
  const [filterStatus, setFilterStatus] = useState<EstadoEjecucion | ''>('');
  const [searchText, setSearchText] = useState('');
  const [deleteConfirmId, setDeleteConfirmId] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  // ── Filtrado ───────────────────────────────────────────────
  const filteredOrders = useMemo(() => {
    return workOrders.filter(wo => {
      const matchesStatus = !filterStatus || wo.estadoEjecucion === filterStatus;
      const matchesSearch =
        !searchText ||
        wo.identificadorOrdenTrabajo.toLowerCase().includes(searchText.toLowerCase()) ||
        wo.identificadorIngeniero?.toLowerCase().includes(searchText.toLowerCase());
      return matchesStatus && matchesSearch;
    });
  }, [workOrders, filterStatus, searchText]);

  // ── Helpers ────────────────────────────────────────────────
  const showSuccess = (msg: string) => {
    setSuccessMessage(msg);
    setTimeout(() => setSuccessMessage(null), 3000);
  };

  // ── Handlers ───────────────────────────────────────────────
  const handleCreate = async (data: WorkOrderCreateRequest) => {
    await createWorkOrder(data);
    setView('list');
    showSuccess('✅ Orden de trabajo creada exitosamente.');
  };

  const handleUpdate = async (data: WorkOrderCreateRequest) => {
    if (!editingWorkOrder) return;
    await updateWorkOrder(editingWorkOrder.identificadorOrdenTrabajo, data);
    setEditingWorkOrder(null);
    setView('list');
    showSuccess('✅ Orden de trabajo actualizada exitosamente.');
  };

  const handleStatusChange = async (
    workOrderId: string,
    newStatus: 'IN_PROGRESS' | 'EXECUTED'
  ) => {
    await updateWorkOrderStatus(workOrderId, { estadoEjecucion: newStatus });
    // Refrescar la orden seleccionada si está en vista de detalle
    if (selectedWorkOrder?.identificadorOrdenTrabajo === workOrderId) {
      setSelectedWorkOrder(prev =>
        prev ? { ...prev, estadoEjecucion: newStatus } : prev
      );
    }
    showSuccess(`✅ Estado actualizado a "${ESTADO_EJECUCION_OPTIONS.find(s => s.value === newStatus)?.label}".`);
  };

  const handleDelete = async (id: string) => {
    await deleteWorkOrder(id);
    setDeleteConfirmId(null);
    showSuccess('✅ Orden de trabajo eliminada.');
  };

  // ── Resolutores para Detalle de OT ──────────────────────────
  const selectedEngineerName = useMemo(() => {
    if (!selectedWorkOrder) return undefined;
    const eng = persons.find(p => p.identificador === selectedWorkOrder.identificadorIngeniero);
    if (!eng) return undefined;
    return `${eng.primerNombre} ${eng.segundoNombre ?? ''} ${eng.primerApellido} ${eng.segundoApellido ?? ''}`.replace(/\s+/g, ' ').trim();
  }, [selectedWorkOrder, persons]);

  const resolvedEquipments = useMemo(() => {
    if (!selectedWorkOrder) return [];
    const list: Array<{ id: string; serie: string; inventario?: string; modeloDisplay?: string }> = [];
    selectedWorkOrder.equipoClienteIds.forEach(id => {
      let foundEq: any = null;
      for (const client of clients) {
        for (const hq of client.headquarterClientList || []) {
          for (const area of hq.serviceAreaList || []) {
            const eq = (area.clientEquipmentList || []).find(e => e.identificadorEquipoCliente === id);
            if (eq) {
              foundEq = eq;
              break;
            }
          }
          if (foundEq) break;
        }
        if (foundEq) break;
      }

      if (foundEq) {
        const eqModel = models.find(m => m.id === foundEq.identificadorModelo);
        let modelDisplay = '';
        if (eqModel) {
          const matchingEq = equipments.find(e => e.id === eqModel.equipmentId);
          const brandName = matchingEq?.brand?.brandName || (matchingEq as any)?.brandResponse?.name || '';
          const typeName = matchingEq?.equipmentType?.equipmentTypeName || (matchingEq as any)?.equipmentTypeResponse?.equipmentTypeName || '';
          const eqRealName = brandName && typeName ? `${brandName} ${typeName}` : (eqModel.equipmentResponse?.name || '');
          const mfg = eqModel.manufacturerResponse?.name || '';

          const parts = [];
          if (mfg) parts.push(mfg);
          if (eqRealName) parts.push(eqRealName);
          if (eqModel.invima) parts.push(`(${eqModel.invima})`);

          modelDisplay = parts.join(' ');
        }

        list.push({
          id: foundEq.identificadorEquipoCliente,
          serie: foundEq.serie,
          inventario: foundEq.numeroInventario,
          modeloDisplay: modelDisplay || undefined
        });
      } else {
        list.push({
          id,
          serie: 'Desconocido',
        });
      }
    });
    return list;
  }, [selectedWorkOrder, clients, models, equipments]);

  const handleOpenDetail = (wo: WorkOrder) => {
    setSelectedWorkOrder(wo);
    setView('detail');
  };

  const handleOpenEdit = (wo: WorkOrder) => {
    setEditingWorkOrder(wo);
    setView('form');
  };

  const handleCancelForm = () => {
    setEditingWorkOrder(null);
    setView('list');
    setError(null);
  };

  // ── Etiqueta del estado ────────────────────────────────────
  const getStatusBadge = (status: EstadoEjecucion) => {
    const opt = ESTADO_EJECUCION_OPTIONS.find(s => s.value === status);
    return (
      <span className={`status-badge ${opt?.color ?? ''}`}>
        {opt?.label ?? status}
      </span>
    );
  };

  // ── Vista: Formulario ──────────────────────────────────────
  if (view === 'form') {
    return (
      <div className="wo-manager">
        <div className="wo-manager-header">
          <div>
            <h1 className="wo-manager-title">
              {editingWorkOrder ? 'Editar Orden de Trabajo' : 'Nueva Orden de Trabajo'}
            </h1>
            <p className="wo-manager-subtitle">
              {editingWorkOrder
                ? 'Modifica los datos de la orden existente.'
                : 'Completa el formulario para registrar una nueva orden.'}
            </p>
          </div>
        </div>
        {error && (
          <div className="wo-form-error">
            <span>⚠️</span>
            <span>{error}</span>
          </div>
        )}
        <div className="wo-form-container">
          <WorkOrderForm
            onSubmit={editingWorkOrder ? handleUpdate : handleCreate}
            onCancel={handleCancelForm}
            isLoading={isLoading}
            initialData={
              editingWorkOrder
                ? {
                    fechaMantenimiento: editingWorkOrder.fechaMantenimiento,
                    periodicidad: editingWorkOrder.periodicidad,
                    identificadorIngeniero: editingWorkOrder.identificadorIngeniero,
                    equipoClienteIds: editingWorkOrder.equipoClienteIds,
                  }
                : undefined
            }
          />
        </div>
      </div>
    );
  }

  // ── Vista: Detalle ─────────────────────────────────────────
  if (view === 'detail' && selectedWorkOrder) {
    return (
      <div className="wo-manager">
        <WorkOrderDetail
          workOrder={selectedWorkOrder}
          engineerName={selectedEngineerName}
          equipmentsDetails={resolvedEquipments}
          onClose={() => { setSelectedWorkOrder(null); setView('list'); }}
          onStatusChange={
            (newStatus) => handleStatusChange(selectedWorkOrder.identificadorOrdenTrabajo, newStatus)
          }
          userRole={user!.role}
          isLoading={isLoading}
        />
      </div>
    );
  }

  // ── Vista: Lista ───────────────────────────────────────────
  return (
    <div className="wo-manager">
      {/* Header */}
      <div className="wo-manager-header">
        <div>
          <h1 className="wo-manager-title">Órdenes de Trabajo</h1>
          <p className="wo-manager-subtitle">
            {isAdmin
              ? 'Gestione todas las órdenes de trabajo del sistema.'
              : 'Consulte y gestione sus órdenes de trabajo asignadas.'}
          </p>
        </div>
        {isAdmin && (
          <button
            id="wo-btn-nueva"
            className="wo-btn wo-btn--primary wo-btn--create"
            onClick={() => { setEditingWorkOrder(null); setView('form'); }}
          >
            + Nueva Orden
          </button>
        )}
      </div>

      {/* Mensajes */}
      {successMessage && (
        <div className="wo-success-toast">{successMessage}</div>
      )}
      {error && (
        <div className="wo-form-error">
          <span>⚠️</span>
          <span>{error}</span>
          <button className="wo-error-close" onClick={() => setError(null)}>✕</button>
        </div>
      )}

      {/* Filtros */}
      <div className="wo-filters">
        <input
          id="wo-search"
          type="text"
          className="wo-input wo-search-input"
          placeholder="Buscar por ID u orden..."
          value={searchText}
          onChange={e => setSearchText(e.target.value)}
        />
        <select
          id="wo-filter-status"
          className="wo-select wo-filter-select"
          value={filterStatus}
          onChange={e => setFilterStatus(e.target.value as EstadoEjecucion | '')}
        >
          <option value="">Todos los estados</option>
          {ESTADO_EJECUCION_OPTIONS.map(opt => (
            <option key={opt.value} value={opt.value}>{opt.label}</option>
          ))}
        </select>
      </div>

      {/* Tabla / Tarjetas */}
      {isLoading ? (
        <div className="wo-loading">
          <div className="wo-spinner" />
          <p>Cargando órdenes de trabajo...</p>
        </div>
      ) : filteredOrders.length === 0 ? (
        <div className="wo-empty">
          <span className="wo-empty-icon">📋</span>
          <h3>Sin órdenes de trabajo</h3>
          <p>
            {workOrders.length === 0
              ? isAdmin
                ? 'Crea la primera orden de trabajo usando el botón "+ Nueva Orden".'
                : 'No tienes órdenes de trabajo asignadas.'
              : 'Ninguna orden coincide con los filtros aplicados.'}
          </p>
        </div>
      ) : (
        <div className="wo-table-wrapper">
          <table className="wo-table">
            <thead className="wo-table-head">
              <tr>
                <th>ID</th>
                <th>Fecha Mantenimiento</th>
                <th>Periodicidad</th>
                <th>Equipos</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {filteredOrders.map(wo => (
                <tr key={wo.identificadorOrdenTrabajo} className="wo-table-row">
                  <td>
                    <span className="wo-id-cell" title={wo.identificadorOrdenTrabajo}>
                      {wo.identificadorOrdenTrabajo.substring(0, 8)}...
                    </span>
                  </td>
                  <td>
                    {new Date(wo.fechaMantenimiento).toLocaleDateString('es-CO', {
                      year: 'numeric',
                      month: 'short',
                      day: 'numeric',
                    })}
                  </td>
                  <td>
                    {(() => {
                      const opts: Record<string, string> = {
                        MONTHLY: 'Mensual', QUARTERLY: 'Trimestral',
                        BIANNUAL: 'Semestral', ANUAL: 'Anual',
                      };
                      return opts[wo.periodicidad] ?? wo.periodicidad;
                    })()}
                  </td>
                  <td>
                    <span className="wo-equip-count-badge">
                      {wo.equipoClienteIds.length} equipo{wo.equipoClienteIds.length !== 1 ? 's' : ''}
                    </span>
                  </td>
                  <td>{getStatusBadge(wo.estadoEjecucion)}</td>
                  <td>
                    <div className="wo-table-actions">
                      <button
                        className="wo-action-btn wo-action-btn--view"
                        title="Ver detalle"
                        onClick={() => handleOpenDetail(wo)}
                      >
                        👁
                      </button>
                      {isAdmin && (
                        <button
                          className="wo-action-btn wo-action-btn--edit"
                          title="Editar"
                          onClick={() => handleOpenEdit(wo)}
                          disabled={wo.estadoEjecucion === 'EXECUTED'}
                        >
                          ✏️
                        </button>
                      )}
                      {isEngineer && wo.estadoEjecucion !== 'EXECUTED' && (
                        <button
                          className="wo-action-btn wo-action-btn--status"
                          title="Avanzar estado"
                          onClick={() => {
                            const next = wo.estadoEjecucion === 'CREATED' ? 'IN_PROGRESS' : 'EXECUTED';
                            handleStatusChange(wo.identificadorOrdenTrabajo, next as 'IN_PROGRESS' | 'EXECUTED');
                          }}
                          disabled={isLoading}
                        >
                          ▶
                        </button>
                      )}
                      {isAdmin && (
                        deleteConfirmId === wo.identificadorOrdenTrabajo ? (
                          <div className="wo-delete-confirm">
                            <span>¿Confirmar?</span>
                            <button
                              className="wo-action-btn wo-action-btn--confirm"
                              onClick={() => handleDelete(wo.identificadorOrdenTrabajo)}
                              disabled={isLoading}
                            >
                              ✓
                            </button>
                            <button
                              className="wo-action-btn wo-action-btn--cancel-del"
                              onClick={() => setDeleteConfirmId(null)}
                            >
                              ✕
                            </button>
                          </div>
                        ) : (
                          <button
                            className="wo-action-btn wo-action-btn--delete"
                            title="Eliminar"
                            onClick={() => setDeleteConfirmId(wo.identificadorOrdenTrabajo)}
                          >
                            🗑
                          </button>
                        )
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Resumen */}
      {!isLoading && workOrders.length > 0 && (
        <div className="wo-summary">
          <span>Total: <strong>{filteredOrders.length}</strong> de {workOrders.length} órdenes</span>
          <div className="wo-summary-badges">
            {ESTADO_EJECUCION_OPTIONS.map(opt => {
              const count = workOrders.filter(wo => wo.estadoEjecucion === opt.value).length;
              return count > 0 ? (
                <span key={opt.value} className={`status-badge ${opt.color} wo-summary-badge`}>
                  {opt.label}: {count}
                </span>
              ) : null;
            })}
          </div>
        </div>
      )}
    </div>
  );
};
