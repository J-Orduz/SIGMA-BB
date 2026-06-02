import React, { useState, useEffect, useMemo } from 'react';
import type { WorkOrderCreateRequest, Periodicidad } from '../types/workOrder.types';
import { PERIODICIDAD_OPTIONS } from '../types/workOrder.types';
import type { Client, Headquarter, ServiceArea } from '../../clients/types/client.types';
import type { Person } from '../../persons/types/person.types';
import type { EquipmentModel } from '../../equipments/types/model.types';
import type { Equipment } from '../../equipments/types/equipment.types';
import { clientService } from '../../clients/services/client.service';
import { personService } from '../../persons/services/person.service';
import { modelService } from '../../equipments/services/model.service';
import { equipmentService } from '../../equipments/services/equipment.service';

interface WorkOrderFormProps {
  onSubmit: (data: WorkOrderCreateRequest) => Promise<void>;
  onCancel: () => void;
  isLoading: boolean;
  initialData?: Partial<WorkOrderCreateRequest>;
}

interface ClientEquipmentOption {
  id: string;
  label: string;
  areaId: string;
  areaNombre: string;
}

export const WorkOrderForm: React.FC<WorkOrderFormProps> = ({
  onSubmit,
  onCancel,
  isLoading,
  initialData,
}) => {
  // ── Datos cargados del backend ─────────────────────────────
  const [clients, setClients] = useState<Client[]>([]);
  const [engineers, setEngineers] = useState<Person[]>([]);
  const [models, setModels] = useState<EquipmentModel[]>([]);
  const [equipments, setEquipments] = useState<Equipment[]>([]);
  const [loadingData, setLoadingData] = useState(true);

  // ── Selecciones en cascada ─────────────────────────────────
  const [selectedClientId, setSelectedClientId] = useState('');
  const [selectedHeadquarterId, setSelectedHeadquarterId] = useState('');
  const [selectedAreaIds, setSelectedAreaIds] = useState<string[]>([]);
  const [selectedEquipmentIds, setSelectedEquipmentIds] = useState<string[]>(
    initialData?.equipoClienteIds ?? []
  );

  // ── Campos del formulario ──────────────────────────────────
  const [fechaMantenimiento, setFechaMantenimiento] = useState(
    initialData?.fechaMantenimiento
      ? initialData.fechaMantenimiento.substring(0, 16)
      : ''
  );
  const [periodicidad, setPeriodicidad] = useState<Periodicidad>(
    initialData?.periodicidad ?? 'QUARTERLY'
  );
  const [identificadorIngeniero, setIdentificadorIngeniero] = useState(
    initialData?.identificadorIngeniero ?? ''
  );

  // ── Error del formulario ───────────────────────────────────
  const [formError, setFormError] = useState<string | null>(null);

  // ── Cargar clientes e ingenieros al montar ─────────────────
  useEffect(() => {
    const loadData = async () => {
      setLoadingData(true);
      try {
        const [clientsData, personsData, modelsData, equipmentsData] = await Promise.all([
          clientService.getAll(),
          personService.getAll(),
          modelService.getAll(),
          equipmentService.getAll(),
        ]);
        setClients(clientsData.filter(c => c.estadoActivo));
        setEngineers(personsData.filter(p => p.tipoPersona === 'ENGINEER' && p.estadoActivo));
        setModels(modelsData);
        setEquipments(equipmentsData);
      } catch {
        setFormError('No se pudieron cargar los datos necesarios. Intente recargar.');
      } finally {
        setLoadingData(false);
      }
    };
    loadData();
  }, []);

  // ── Cálculo derivado: Sedes del cliente seleccionado ──────
  const availableHeadquarters = useMemo<Headquarter[]>(() => {
    if (!selectedClientId) return [];
    const client = clients.find(c => c.identificadorCliente === selectedClientId);
    return (client?.headquarterClientList ?? [])
      .filter(h => h.estadoActivo)
      .sort((a, b) => a.nombreSede.localeCompare(b.nombreSede));
  }, [selectedClientId, clients]);

  // ── Cálculo derivado: Áreas de la sede seleccionada ───────
  const availableAreas = useMemo<ServiceArea[]>(() => {
    if (!selectedHeadquarterId) return [];
    const client = clients.find(c => c.identificadorCliente === selectedClientId);
    const hq = client?.headquarterClientList.find(h => h.identificadorSede === selectedHeadquarterId);
    return (hq?.serviceAreaList ?? [])
      .filter(a => a.estadoActivo)
      .sort((a, b) => a.nombreAreaServicio.localeCompare(b.nombreAreaServicio));
  }, [selectedHeadquarterId, selectedClientId, clients]);

  // ── Cálculo derivado: Equipos de las áreas seleccionadas ──
  const availableEquipments = useMemo<ClientEquipmentOption[]>(() => {
    if (selectedAreaIds.length === 0) return [];
    const client = clients.find(c => c.identificadorCliente === selectedClientId);
    const hq = client?.headquarterClientList.find(h => h.identificadorSede === selectedHeadquarterId);
    if (!hq) return [];

    const equipmentsList: ClientEquipmentOption[] = [];
    hq.serviceAreaList
      .filter(a => selectedAreaIds.includes(a.identificadorAreaServicio))
      .sort((a, b) => a.nombreAreaServicio.localeCompare(b.nombreAreaServicio))
      .forEach(area => {
        const areaAny = area as any;
        (areaAny.clientEquipmentList ?? [])
          .filter((e: any) => e.estadoActivo !== false)
          .forEach((eq: any) => {
            const eqModel = models.find(m => m.id === eq.identificadorModelo);
            let eqRealName = '';
            if (eqModel) {
              const matchingEq = equipments.find(e => e.id === eqModel.equipmentId);
              const brandName = matchingEq?.brand?.brandName || (matchingEq as any)?.brandResponse?.name || '';
              const typeName = matchingEq?.equipmentType?.equipmentTypeName || (matchingEq as any)?.equipmentTypeResponse?.equipmentTypeName || '';
              eqRealName = brandName && typeName ? `${brandName} ${typeName}` : (eqModel.invima || '');
            }

            const labelParts = [`Serie: ${eq.serie}`];
            if (eqRealName) labelParts.push(`Equipo: ${eqRealName}`);
            if (eq.numeroInventario) labelParts.push(`Inv: ${eq.numeroInventario}`);

            equipmentsList.push({
              id: eq.identificadorEquipoCliente,
              label: labelParts.join(' | '),
              areaId: area.identificadorAreaServicio,
              areaNombre: area.nombreAreaServicio,
            });
          });
      });
    return equipmentsList;
  }, [selectedAreaIds, selectedHeadquarterId, selectedClientId, clients, models, equipments]);

  // ── Limpiar selecciones en cascada al cambiar padre ───────
  const handleClientChange = (clientId: string) => {
    setSelectedClientId(clientId);
    setSelectedHeadquarterId('');
    setSelectedAreaIds([]);
    setSelectedEquipmentIds([]);
  };

  const handleHeadquarterChange = (hqId: string) => {
    setSelectedHeadquarterId(hqId);
    setSelectedAreaIds([]);
    setSelectedEquipmentIds([]);
  };

  const toggleArea = (areaId: string) => {
    setSelectedAreaIds(prev => {
      const isSelected = prev.includes(areaId);
      const next = isSelected ? prev.filter(id => id !== areaId) : [...prev, areaId];
      // Quitar equipos de áreas deseleccionadas
      if (isSelected) {
        const removedEquips = availableEquipments
          .filter(e => e.areaId === areaId)
          .map(e => e.id);
        setSelectedEquipmentIds(sel => sel.filter(id => !removedEquips.includes(id)));
      }
      return next;
    });
  };

  const toggleEquipment = (equipId: string) => {
    setSelectedEquipmentIds(prev =>
      prev.includes(equipId) ? prev.filter(id => id !== equipId) : [...prev, equipId]
    );
  };

  // ── Envío del formulario ───────────────────────────────────
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFormError(null);

    if (!fechaMantenimiento) {
      setFormError('La fecha de mantenimiento es obligatoria.');
      return;
    }
    if (!identificadorIngeniero) {
      setFormError('Debe seleccionar un ingeniero.');
      return;
    }
    if (selectedEquipmentIds.length === 0) {
      setFormError('Debe seleccionar al menos un equipo del cliente.');
      return;
    }

    const payload: WorkOrderCreateRequest = {
      fechaMantenimiento: new Date(fechaMantenimiento).toISOString(),
      periodicidad,
      identificadorIngeniero,
      equipoClienteIds: selectedEquipmentIds,
    };

    try {
      await onSubmit(payload);
    } catch (err) {
      setFormError(err instanceof Error ? err.message : 'Error al guardar la orden de trabajo.');
    }
  };

  if (loadingData) {
    return (
      <div className="wo-form-loading">
        <div className="wo-spinner" />
        <p>Cargando datos del formulario...</p>
      </div>
    );
  }

  // ── Agrupar equipos por área para mostrar en la UI ────────
  const equipmentsByArea = availableAreas
    .filter(a => selectedAreaIds.includes(a.identificadorAreaServicio))
    .map(area => ({
      area,
      equipments: availableEquipments.filter(e => e.areaId === area.identificadorAreaServicio),
    }));

  return (
    <form className="wo-form" onSubmit={handleSubmit} noValidate>
      {/* Error global */}
      {formError && (
        <div className="wo-form-error">
          <span>⚠️</span>
          <span>{formError}</span>
        </div>
      )}

      {/* ── Sección 1: Cliente → Sede ─────────────────────────── */}
      <div className="wo-form-section">
        <h3 className="wo-form-section-title">
          <span className="wo-section-number">1</span>
          Cliente y Sede
        </h3>

        <div className="wo-form-row">
          {/* Cliente */}
          <div className="wo-field">
            <label htmlFor="wo-client" className="wo-label">Cliente *</label>
            <select
              id="wo-client"
              className="wo-select"
              value={selectedClientId}
              onChange={e => handleClientChange(e.target.value)}
              required
            >
              <option value="">Seleccione un cliente...</option>
              {clients
                .sort((a, b) => a.razonSocial.localeCompare(b.razonSocial))
                .map(c => (
                  <option key={c.identificadorCliente} value={c.identificadorCliente}>
                    {c.razonSocial}
                  </option>
                ))}
            </select>
          </div>

          {/* Sede */}
          <div className="wo-field">
            <label htmlFor="wo-headquarter" className="wo-label">Sede *</label>
            <select
              id="wo-headquarter"
              className="wo-select"
              value={selectedHeadquarterId}
              onChange={e => handleHeadquarterChange(e.target.value)}
              disabled={!selectedClientId}
              required
            >
              <option value="">Seleccione una sede...</option>
              {availableHeadquarters.map(h => (
                <option key={h.identificadorSede} value={h.identificadorSede}>
                  {h.nombreSede}
                </option>
              ))}
            </select>
            {selectedClientId && availableHeadquarters.length === 0 && (
              <p className="wo-field-hint">Este cliente no tiene sedes registradas.</p>
            )}
          </div>
        </div>
      </div>

      {/* ── Sección 2: Áreas de Servicio ─────────────────────── */}
      {selectedHeadquarterId && (
        <div className="wo-form-section">
          <h3 className="wo-form-section-title">
            <span className="wo-section-number">2</span>
            Áreas de Servicio
          </h3>
          {availableAreas.length === 0 ? (
            <p className="wo-field-hint">Esta sede no tiene áreas de servicio registradas.</p>
          ) : (
            <div className="wo-chips-grid">
              {availableAreas.map(area => {
                const isSelected = selectedAreaIds.includes(area.identificadorAreaServicio);
                return (
                  <button
                    key={area.identificadorAreaServicio}
                    type="button"
                    className={`wo-chip ${isSelected ? 'wo-chip--selected' : ''}`}
                    onClick={() => toggleArea(area.identificadorAreaServicio)}
                  >
                    <span className="wo-chip-check">{isSelected ? '✓' : '+'}</span>
                    {area.nombreAreaServicio}
                  </button>
                );
              })}
            </div>
          )}
          {selectedAreaIds.length > 0 && (
            <p className="wo-field-hint wo-field-hint--success">
              {selectedAreaIds.length} área{selectedAreaIds.length !== 1 ? 's' : ''} seleccionada{selectedAreaIds.length !== 1 ? 's' : ''}.
            </p>
          )}
        </div>
      )}

      {/* ── Sección 3: Equipos por área ──────────────────────── */}
      {selectedAreaIds.length > 0 && (
        <div className="wo-form-section">
          <h3 className="wo-form-section-title">
            <span className="wo-section-number">3</span>
            Equipos a Intervenir
          </h3>
          {equipmentsByArea.length === 0 ? (
            <p className="wo-field-hint">Las áreas seleccionadas no tienen equipos registrados.</p>
          ) : (
            equipmentsByArea.map(({ area, equipments }) => (
              <div key={area.identificadorAreaServicio} className="wo-area-group">
                <div className="wo-area-group-header">
                  <span className="wo-area-icon">🏥</span>
                  <span className="wo-area-name">{area.nombreAreaServicio}</span>
                  <span className="wo-area-count">
                    {equipments.filter(e => selectedEquipmentIds.includes(e.id)).length}/{equipments.length}
                  </span>
                </div>
                {equipments.length === 0 ? (
                  <p className="wo-field-hint wo-field-hint--indent">
                    Sin equipos registrados en esta área.
                  </p>
                ) : (
                  <div className="wo-equip-list">
                    {equipments.map(eq => {
                      const isSelected = selectedEquipmentIds.includes(eq.id);
                      return (
                        <label key={eq.id} className={`wo-equip-item ${isSelected ? 'wo-equip-item--selected' : ''}`}>
                          <input
                            type="checkbox"
                            className="wo-equip-checkbox"
                            checked={isSelected}
                            onChange={() => toggleEquipment(eq.id)}
                          />
                          <span className="wo-equip-label">{eq.label}</span>
                        </label>
                      );
                    })}
                  </div>
                )}
              </div>
            ))
          )}
          {selectedEquipmentIds.length > 0 && (
            <p className="wo-field-hint wo-field-hint--success">
              {selectedEquipmentIds.length} equipo{selectedEquipmentIds.length !== 1 ? 's' : ''} seleccionado{selectedEquipmentIds.length !== 1 ? 's' : ''}.
            </p>
          )}
        </div>
      )}

      {/* ── Sección 4: Detalles de la Orden ──────────────────── */}
      <div className="wo-form-section">
        <h3 className="wo-form-section-title">
          <span className="wo-section-number">4</span>
          Detalles de la Orden
        </h3>

        <div className="wo-form-row">
          {/* Fecha */}
          <div className="wo-field">
            <label htmlFor="wo-fecha" className="wo-label">Fecha de Mantenimiento *</label>
            <input
              id="wo-fecha"
              type="datetime-local"
              className="wo-input"
              value={fechaMantenimiento}
              onChange={e => setFechaMantenimiento(e.target.value)}
              required
            />
          </div>

          {/* Periodicidad */}
          <div className="wo-field">
            <label htmlFor="wo-periodicidad" className="wo-label">Periodicidad *</label>
            <select
              id="wo-periodicidad"
              className="wo-select"
              value={periodicidad}
              onChange={e => setPeriodicidad(e.target.value as Periodicidad)}
              required
            >
              {PERIODICIDAD_OPTIONS.map(opt => (
                <option key={opt.value} value={opt.value}>
                  {opt.label}
                </option>
              ))}
            </select>
          </div>
        </div>

        {/* Ingeniero */}
        <div className="wo-field">
          <label htmlFor="wo-ingeniero" className="wo-label">Ingeniero Asignado *</label>
          <select
            id="wo-ingeniero"
            className="wo-select"
            value={identificadorIngeniero}
            onChange={e => setIdentificadorIngeniero(e.target.value)}
            required
          >
            <option value="">Seleccione un ingeniero...</option>
            {engineers
              .sort((a, b) =>
                `${a.primerNombre} ${a.primerApellido}`.localeCompare(
                  `${b.primerNombre} ${b.primerApellido}`
                )
              )
              .map(eng => (
                <option key={eng.identificador} value={eng.identificador}>
                  {eng.primerNombre} {eng.segundoNombre ?? ''} {eng.primerApellido} {eng.segundoApellido ?? ''}
                </option>
              ))}
          </select>
          {engineers.length === 0 && (
            <p className="wo-field-hint">No hay ingenieros registrados en el sistema.</p>
          )}
        </div>
      </div>

      {/* ── Acciones ──────────────────────────────────────────── */}
      <div className="wo-form-actions">
        <button
          type="button"
          className="wo-btn wo-btn--secondary"
          onClick={onCancel}
          disabled={isLoading}
        >
          Cancelar
        </button>
        <button
          type="submit"
          className="wo-btn wo-btn--primary"
          disabled={isLoading || selectedEquipmentIds.length === 0}
        >
          {isLoading ? (
            <><span className="wo-spinner-sm" /> Guardando...</>
          ) : (
            'Guardar Orden de Trabajo'
          )}
        </button>
      </div>
    </form>
  );
};
