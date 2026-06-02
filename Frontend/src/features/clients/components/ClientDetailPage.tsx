import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { clientService } from '../services/client.service';
import { headquarterService } from '../services/headquarter.service';
import { serviceAreaService } from '../services/serviceArea.service';
import { useCountries } from '../../locations/hooks/useCountries';
import { useCities } from '../../locations/hooks/useCities';
import { usePersons } from '../../persons/hooks/usePersons';
import { useModels } from '../../equipments/hooks/useModels';
import type {
  Client,
  Headquarter,
  ServiceArea,
  HeadquarterCreateRequest,
  ServiceAreaCreateRequest,
} from '../types/client.types';

// ─── Tipos de pestaña ───────────────────────────────────────────────────────
type Tab = 'info' | 'sedes' | 'areas';

// ─── Helpers ─────────────────────────────────────────────────────────────────
const buildAddress = (hq: Headquarter) =>
  `${hq.direccionCalleSede} ${hq.direccionCarreraSede} #${hq.direccionNumeroSede}`;

const HQ_FORM_INITIAL: HeadquarterCreateRequest = {
  nombreSede: '',
  direccionCalleSede: '',
  direccionCarreraSede: '',
  direccionNumeroSede: '',
  identificadorCiudad: '',
  identificadorCliente: '',
  serviceAreaList: [],
  managerList: [],
};

const SA_FORM_INITIAL: ServiceAreaCreateRequest = {
  nombreAreaServicio: '',
  identificadorSede: '',
  managerList: [],
};

// ─── Componente Principal ────────────────────────────────────────────────────
export const ClientDetailPage: React.FC = () => {
  const { clientId } = useParams<{ clientId: string }>();
  const navigate = useNavigate();

  const { countries } = useCountries();
  const { cities } = useCities();
  const { persons } = usePersons();
  const { models } = useModels();

  const [client, setClient] = useState<Client | null>(null);
  const [allHqs, setAllHqs] = useState<Headquarter[]>([]);
  const [allAreas, setAllAreas] = useState<ServiceArea[]>([]);

  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState('');
  const [activeTab, setActiveTab] = useState<Tab>('info');

  // Formularios para agregar sedes / áreas
  const [hqForm, setHqForm] = useState<HeadquarterCreateRequest>(HQ_FORM_INITIAL);
  const [saForm, setSaForm] = useState<ServiceAreaCreateRequest>(SA_FORM_INITIAL);
  const [showHqForm, setShowHqForm] = useState(false);
  const [showSaForm, setShowSaForm] = useState(false);
  const [isSaving, setIsSaving] = useState(false);

  // Estados de edición de información general
  const [isEditingInfo, setIsEditingInfo] = useState(false);
  const [editRazonSocial, setEditRazonSocial] = useState('');
  const [editPais, setEditPais] = useState('');
  const [editEmails, setEditEmails] = useState<string[]>(['']);
  const [editPhones, setEditPhones] = useState<string[]>(['']);
  const [editRepLegal, setEditRepLegal] = useState('');

  // ── Estados para Gestión de Equipos biomédicos (Solución 2) ───────────────────
  const [activeAreaIdForEquipmentForm, setActiveAreaIdForEquipmentForm] = useState<string | null>(null);
  const [eqSerie, setEqSerie] = useState('');
  const [eqInventario, setEqInventario] = useState('');
  const [eqFechaCompra, setEqFechaCompra] = useState('');
  const [eqValorCompra, setEqValorCompra] = useState('');
  const [eqModeloId, setEqModeloId] = useState('');

  // ── Carga de datos ──────────────────────────────────────────────────────────

  const showSuccess = (msg: string) => {
    setSuccessMsg(msg);
    setTimeout(() => setSuccessMsg(''), 4000);
  };

  const fetchData = useCallback(async () => {
    if (!clientId) return;
    setIsLoading(true);
    setError(null);
    try {
      const [clientData, hqData, areaData] = await Promise.all([
        clientService.getById(clientId),
        headquarterService.getAll(),
        serviceAreaService.getAll(),
      ]);
      setClient(clientData);

      // Filtrar sedes que pertenecen a este cliente y estén activas
      const activeHqsFromClient = clientData.headquarterClientList?.filter((h) => h.estadoActivo !== false) || [];
      const clientHqIds = new Set(activeHqsFromClient.map((h) => h.identificadorSede));
      const clientHqs = hqData.filter((h) => clientHqIds.has(h.identificadorSede) && h.estadoActivo !== false);
      setAllHqs(clientHqs);

      // Filtrar áreas de servicio activas asociadas a las sedes de este cliente
      const hqAreaIds = new Set(clientHqs.flatMap((h) => (h.serviceAreaList || []).filter((a) => a.estadoActivo !== false).map((a) => a.identificadorAreaServicio)));
      const activeAreas = areaData.filter((a) => hqAreaIds.has(a.identificadorAreaServicio) && a.estadoActivo !== false);
      setAllAreas(activeAreas);
    } catch {
      setError('No se pudo cargar la información del cliente. Verifique la conexión con el servidor.');
    } finally {
      setIsLoading(false);
    }
  }, [clientId]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  // Sincronizar select de ciudad por defecto en formulario de sedes
  useEffect(() => {
    if (cities.length > 0 && !hqForm.identificadorCiudad) {
      setHqForm((prev) => ({ ...prev, identificadorCiudad: cities[0].id }));
    }
  }, [cities, hqForm.identificadorCiudad]);

  // Sincronizar select de sede por defecto en formulario de áreas de servicio
  useEffect(() => {
    if (allHqs.length > 0 && !saForm.identificadorSede) {
      setSaForm((prev) => ({ ...prev, identificadorSede: allHqs[0].identificadorSede }));
    }
  }, [allHqs, saForm.identificadorSede]);

  // ── Modificar datos generales del cliente ────────────────────────────────────

  const startEditing = () => {
    if (!client) return;
    setEditRazonSocial(client.razonSocial);
    setEditPais(client.identificadorPais);
    setEditEmails(client.correosCliente?.filter(e => e.estadoActivo !== false).map(e => e.correoCliente) || ['']);
    setEditPhones(client.telefonosCliente?.filter(p => p.estadoActivo !== false).map(p => p.telefonoCliente) || ['']);
    setEditRepLegal(client.identificadorRepresentante || localStorage.getItem(`rep_legal_${client.identificadorCliente}`) || '');
    setIsEditingInfo(true);
  };

  const handleEmailChange = (index: number, value: string) => {
    const updated = [...editEmails];
    updated[index] = value;
    setEditEmails(updated);
  };

  const handlePhoneChange = (index: number, value: string) => {
    const updated = [...editPhones];
    updated[index] = value;
    setEditPhones(updated);
  };

  const addEmailField = () => setEditEmails((prev) => [...prev, '']);
  const removeEmailField = (index: number) => setEditEmails((prev) => prev.filter((_, i) => i !== index));

  const addPhoneField = () => setEditPhones((prev) => [...prev, '']);
  const removePhoneField = (index: number) => setEditPhones((prev) => prev.filter((_, i) => i !== index));

  const handleSaveInfo = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!client) return;
    setIsSaving(true);
    setError(null);
    try {
      const payload = {
        identificadorCliente: client.identificadorCliente,
        tipoIdentifiacion: client.tipoIdentifiacion,
        razonSocial: editRazonSocial.trim(),
        identificadorPais: editPais,
        emailClientList: editEmails.filter((email) => email.trim() !== '').map((correoCliente) => ({ correoCliente })),
        phoneClientList: editPhones.filter((phone) => phone.trim() !== '').map((telefonoCliente) => ({ telefonoCliente })),
        headquarterList: [],
        identificadorRepresentante: editRepLegal || undefined,
      };

      await clientService.update(client.identificadorCliente, payload);

      // Guardar el Representante Legal
      if (editRepLegal) {
        localStorage.setItem(`rep_legal_${client.identificadorCliente}`, editRepLegal);
      } else {
        localStorage.removeItem(`rep_legal_${client.identificadorCliente}`);
      }

      setIsEditingInfo(false);
      showSuccess('¡Información del cliente actualizada exitosamente!');
      await fetchData();
    } catch (err: any) {
      setError(err.message || 'Error al actualizar el cliente.');
    } finally {
      setIsSaving(false);
    }
  };

  // ── Agregar Sede ────────────────────────────────────────────────────────────

  const handleAddHeadquarter = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSaving(true);
    setError(null);
    try {
      await headquarterService.create({
        ...hqForm,
        identificadorCliente: clientId!,
      });
      setHqForm({
        ...HQ_FORM_INITIAL,
        identificadorCiudad: cities[0]?.id || '',
      });
      setShowHqForm(false);
      showSuccess('¡Sede registrada con éxito!');
      await fetchData();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al registrar la sede.');
    } finally {
      setIsSaving(false);
    }
  };

  // ── Agregar Área de Servicio ────────────────────────────────────────────────

  const handleAddServiceArea = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!saForm.identificadorSede) {
      setError('Por favor, seleccione una Sede para vincular el área de servicio.');
      return;
    }
    setIsSaving(true);
    setError(null);
    try {
      await serviceAreaService.create(saForm);
      setSaForm({
        ...SA_FORM_INITIAL,
        identificadorSede: allHqs[0]?.identificadorSede || '',
      });
      setShowSaForm(false);
      showSuccess('¡Área de servicio registrada con éxito!');
      await fetchData();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al registrar el área de servicio.');
    } finally {
      setIsSaving(false);
    }
  };

  // ── Eliminar Sede ───────────────────────────────────────────────────────────

  const handleDeleteHq = async (id: string, name: string) => {
    if (!confirm(`¿Eliminar la sede "${name}"? Esta acción no se puede deshacer.`)) return;
    try {
      await headquarterService.delete(id);
      showSuccess('Sede eliminada del sistema.');
      await fetchData();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo eliminar la sede.');
    }
  };

  // ── Eliminar Área de Servicio ───────────────────────────────────────────────

  const handleDeleteArea = async (id: string, name: string) => {
    if (!confirm(`¿Eliminar el área de servicio "${name}"? Esta acción no se puede deshacer.`)) return;
    try {
      await serviceAreaService.delete(id);
      showSuccess('Área de servicio eliminada del sistema.');
      await fetchData();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo eliminar el área de servicio.');
    }
  };

  // ── Gestión de Equipos biomédicos (Solución 2) ───────────────────────────────

  const handleAddEquipment = async (e: React.FormEvent, areaId: string) => {
    e.preventDefault();
    if (!eqSerie.trim()) {
      setError('El número de serie es obligatorio.');
      return;
    }
    setIsSaving(true);
    setError(null);
    try {
      const payload = {
        serie: eqSerie.trim(),
        numeroInventario: eqInventario.trim() || undefined,
        fechaCompra: eqFechaCompra ? new Date(eqFechaCompra).toISOString() : undefined,
        valorCompra: eqValorCompra ? parseInt(eqValorCompra, 10) : undefined,
        identificadorModelo: eqModeloId || undefined,
      };

      await serviceAreaService.addEquipment(areaId, payload);
      setEqSerie('');
      setEqInventario('');
      setEqFechaCompra('');
      setEqValorCompra('');
      setEqModeloId('');
      setActiveAreaIdForEquipmentForm(null);
      showSuccess('¡Equipo biomédico vinculado con éxito!');
      await fetchData();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al registrar el equipo biomédico.');
    } finally {
      setIsSaving(false);
    }
  };

  const handleDeleteEquipment = async (areaId: string, equipId: string, serial: string) => {
    if (!confirm(`¿Desvincular el equipo biomédico con serie "${serial}"? Esta acción lo dará de baja.`)) return;
    try {
      await serviceAreaService.deleteEquipment(areaId, equipId);
      showSuccess('Equipo biomédico desvinculado con éxito.');
      await fetchData();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo desvincular el equipo.');
    }
  };

  // ── Render: Loading / Error ─────────────────────────────────────────────────

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="text-center space-y-3">
          <svg className="w-8 h-8 animate-spin mx-auto text-blue-500" fill="none" viewBox="0 0 24 24">
            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z" />
          </svg>
          <p className="text-sm text-slate-500">Cargando información del cliente...</p>
        </div>
      </div>
    );
  }

  if (!client) {
    return (
      <div className="p-6 max-w-2xl mx-auto text-center space-y-4 mt-12">
        <span className="text-5xl">🔍</span>
        <h2 className="text-xl font-bold text-slate-700">Cliente no encontrado</h2>
        <p className="text-sm text-slate-500">El cliente con NIT <code>{clientId}</code> no existe o fue eliminado.</p>
        <Link to="/clients" className="inline-block bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium py-2 px-4 rounded-lg transition-colors">
          ← Volver a la lista
        </Link>
      </div>
    );
  }

  // ── Render Principal ────────────────────────────────────────────────────────

  return (
    <div className="p-4 md:p-6 max-w-6xl mx-auto space-y-6">

      {/* ── Breadcrumb ───────────────────────────────────────────────── */}
      <nav className="flex items-center gap-2 text-sm text-slate-500">
        <button onClick={() => navigate('/clients')} className="hover:text-blue-600 transition-colors">
          Clientes
        </button>
        <span>/</span>
        <span className="font-medium text-slate-700">{client.razonSocial}</span>
      </nav>

      {/* ── Tarjeta de encabezado del cliente ────────────────────────── */}
      <div className="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
        <div className="bg-gradient-to-r from-slate-800 to-slate-700 px-6 py-5 flex flex-col sm:flex-row sm:items-start sm:justify-between gap-4">
          <div className="space-y-1">
            <h1 className="text-xl font-bold text-white">{client.razonSocial}</h1>
            <p className="text-sm text-slate-400 font-mono">NIT ({client.tipoIdentifiacion}): {client.identificadorCliente}</p>
            <p className="text-xs text-slate-500">
              País: {countries.find((c) => c.id === client.identificadorPais)?.name || client.identificadorPais}
            </p>
          </div>
          <div className="flex gap-2 shrink-0">
            <button
              onClick={() => navigate('/clients')}
              className="px-3 py-1.5 bg-slate-700 hover:bg-slate-600 text-slate-300 text-sm font-medium rounded-lg transition-colors border border-slate-600"
            >
              ← Volver
            </button>
          </div>
        </div>

        {/* Info rápida de contacto */}
        {(client.correosCliente?.length > 0 || client.telefonosCliente?.length > 0) && (
          <div className="px-6 py-3 border-t border-slate-100 bg-slate-50 flex flex-wrap gap-x-6 gap-y-1">
            {client.correosCliente?.filter(e => e.estadoActivo !== false).map((e) => (
              <span key={e.identificadorCorreoCliente} className="text-xs text-slate-500 flex items-center gap-1">
                <span className="text-slate-400">✉️</span> {e.correoCliente}
              </span>
            ))}
            {client.telefonosCliente?.filter(p => p.estadoActivo !== false).map((p) => (
              <span key={p.identificadorTelefonoCliente} className="text-xs text-slate-500 flex items-center gap-1">
                <span className="text-slate-400">📞</span> {p.telefonoCliente}
              </span>
            ))}
          </div>
        )}
      </div>

      {/* ── Alertas de Feedback ──────────────────────────────────────── */}
      {error && (
        <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg flex justify-between items-center">
          <span>⚠️ {error}</span>
          <button onClick={() => setError(null)} className="text-red-500 hover:text-red-700 font-bold ml-2">✕</button>
        </div>
      )}
      {successMsg && (
        <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg">
          💡 {successMsg}
        </div>
      )}

      {/* ── Pestañas (Tabs) ───────────────────────────────────────────── */}
      <div className="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
        {/* Barra de pestañas */}
        <div className="flex border-b border-slate-200 bg-slate-50">
          {[
            { key: 'info' as Tab, label: '📋 Información General', count: null },
            { key: 'sedes' as Tab, label: '🏢 Sedes', count: allHqs.length },
            { key: 'areas' as Tab, label: '🗂️ Áreas de Servicio', count: allAreas.length },
          ].map((tab) => (
            <button
              key={tab.key}
              id={`tab-${tab.key}`}
              onClick={() => setActiveTab(tab.key)}
              className={`px-4 md:px-6 py-3 text-sm font-medium transition-colors whitespace-nowrap flex items-center gap-2 ${
                activeTab === tab.key
                  ? 'text-blue-600 border-b-2 border-blue-600 bg-white'
                  : 'text-slate-500 hover:text-slate-700 hover:bg-white/60'
              }`}
            >
              {tab.label}
              {tab.count !== null && (
                <span className={`text-xs px-1.5 py-0.5 rounded-full font-semibold ${
                  activeTab === tab.key ? 'bg-blue-100 text-blue-700' : 'bg-slate-200 text-slate-600'
                }`}>
                  {tab.count}
                </span>
              )}
            </button>
          ))}
        </div>

        {/* ── Contenido de la pestaña: Información General ─────────── */}
        {activeTab === 'info' && (
          <div className="p-6">
            {!isEditingInfo ? (
              <div className="space-y-6 animate-fade-in">
                <div className="flex justify-between items-center border-b border-slate-100 pb-3">
                  <h3 className="text-base font-semibold text-slate-700">Información del Cliente</h3>
                  <button
                    onClick={startEditing}
                    className="px-3 py-1.5 bg-blue-50 text-blue-600 hover:bg-blue-100 text-xs font-semibold rounded-lg border border-blue-100 transition-colors flex items-center gap-1"
                  >
                    ✏️ Editar Información
                  </button>
                </div>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                  <div className="space-y-4">
                    <h4 className="text-xs font-bold text-slate-400 uppercase tracking-wider">Datos Institucionales</h4>
                    <InfoRow label="NIT / Identificador" value={client.identificadorCliente} mono />
                    <InfoRow label="Tipo de Identificación" value={client.tipoIdentifiacion === 'NIT_juridico' ? 'NIT (Persona Jurídica)' : 'NIT (Persona Natural)'} />
                    <InfoRow label="Razón Social" value={client.razonSocial} />
                    <InfoRow 
                      label="País" 
                      value={countries.find((c) => c.id === client.identificadorPais)?.name || client.identificadorPais} 
                    />
                    {(() => {
                      const repCedula = client.identificadorRepresentante || localStorage.getItem(`rep_legal_${client.identificadorCliente}`);
                      const repPerson = persons.find(p => p.cedula === repCedula);
                      const repName = repPerson ? `${repPerson.primerNombre} ${repPerson.primerApellido}` : 'No asignado';
                      return (
                        <InfoRow 
                          label="Representante Legal" 
                          value={repCedula ? `${repName} (${repCedula})` : 'No asignado'} 
                        />
                      );
                    })()}
                  </div>
                  <div className="space-y-4">
                    <h4 className="text-xs font-bold text-slate-400 uppercase tracking-wider">Información de Contacto</h4>
                    {client.correosCliente?.filter(e => e.estadoActivo !== false).length > 0 ? (
                      <div>
                        <p className="text-xs text-slate-500 mb-1">Correos Electrónicos</p>
                        {client.correosCliente.filter(e => e.estadoActivo !== false).map((e) => (
                          <p key={e.identificadorCorreoCliente} className="text-sm text-slate-800 font-medium">✉️ {e.correoCliente}</p>
                        ))}
                      </div>
                    ) : (
                      <p className="text-sm text-slate-400 italic">Sin correos registrados.</p>
                    )}
                    {client.telefonosCliente?.filter(p => p.estadoActivo !== false).length > 0 ? (
                      <div>
                        <p className="text-xs text-slate-500 mb-1">Teléfonos</p>
                        {client.telefonosCliente.filter(p => p.estadoActivo !== false).map((p) => (
                          <p key={p.identificadorTelefonoCliente} className="text-sm text-slate-800 font-medium">📞 {p.telefonoCliente}</p>
                        ))}
                      </div>
                    ) : (
                      <p className="text-sm text-slate-400 italic">Sin teléfonos registrados.</p>
                    )}
                  </div>
                </div>
              </div>
            ) : (
              <form onSubmit={handleSaveInfo} className="space-y-6 animate-fade-in">
                <div className="border-b border-slate-100 pb-3">
                  <h3 className="text-base font-semibold text-slate-700">Modificar Datos del Cliente</h3>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                  {/* Formulario izquierdo */}
                  <div className="space-y-4">
                    <div>
                      <FormLabel>Razón Social</FormLabel>
                      <input
                        required
                        type="text"
                        value={editRazonSocial}
                        onChange={(e) => setEditRazonSocial(e.target.value)}
                        className={inputCls}
                      />
                    </div>
                    <div>
                      <FormLabel>País de Operación</FormLabel>
                      <select
                        value={editPais}
                        onChange={(e) => setEditPais(e.target.value)}
                        className={inputCls}
                      >
                        {countries.length === 0 ? (
                          <option value={client.identificadorPais}>{client.identificadorPais}</option>
                        ) : (
                          countries.map((c) => (
                            <option key={c.id} value={c.id}>{c.name}</option>
                          ))
                        )}
                      </select>
                    </div>
                    <div>
                      <FormLabel>Representante Legal</FormLabel>
                      <select
                        value={editRepLegal}
                        onChange={(e) => setEditRepLegal(e.target.value)}
                        className={inputCls}
                      >
                        <option value="">-- Seleccione Representante --</option>
                        {persons.filter(p => p.estadoActivo !== false && p.tipoPersona === 'CEO_CLIENT').map((p) => (
                          <option key={p.cedula} value={p.cedula}>
                            {p.primerNombre} {p.primerApellido} ({p.cedula})
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>

                  {/* Formulario derecho (Contactos) */}
                  <div className="space-y-4">
                    {/* Correos */}
                    <div>
                      <div className="flex items-center justify-between mb-1">
                        <FormLabel>Correos Electrónicos</FormLabel>
                        <button
                          type="button"
                          onClick={addEmailField}
                          className="text-xs text-blue-600 hover:text-blue-700 font-medium"
                        >
                          + Agregar
                        </button>
                      </div>
                      <div className="space-y-2">
                        {editEmails.map((email, idx) => (
                          <div key={idx} className="flex gap-2">
                            <input
                              required
                              type="email"
                              value={email}
                              onChange={(e) => handleEmailChange(idx, e.target.value)}
                              placeholder="correo@empresa.com"
                              className={inputCls}
                            />
                            {editEmails.length > 1 && (
                              <button
                                type="button"
                                onClick={() => removeEmailField(idx)}
                                className="text-red-400 hover:text-red-600 px-1 font-bold text-sm"
                              >
                                ✕
                              </button>
                            )}
                          </div>
                        ))}
                      </div>
                    </div>

                    {/* Teléfonos */}
                    <div>
                      <div className="flex items-center justify-between mb-1">
                        <FormLabel>Teléfonos</FormLabel>
                        <button
                          type="button"
                          onClick={addPhoneField}
                          className="text-xs text-blue-600 hover:text-blue-700 font-medium"
                        >
                          + Agregar
                        </button>
                      </div>
                      <div className="space-y-2">
                        {editPhones.map((phone, idx) => (
                          <div key={idx} className="flex gap-2">
                            <input
                              required
                              type="tel"
                              value={phone}
                              onChange={(e) => handlePhoneChange(idx, e.target.value)}
                              placeholder="Ej. 3123120302"
                              className={inputCls}
                            />
                            {editPhones.length > 1 && (
                              <button
                                type="button"
                                onClick={() => removePhoneField(idx)}
                                className="text-red-400 hover:text-red-600 px-1 font-bold text-sm"
                              >
                                ✕
                              </button>
                            )}
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>

                <div className="flex justify-end gap-2 border-t border-slate-100 pt-4">
                  <button
                    type="button"
                    onClick={() => setIsEditingInfo(false)}
                    className={btnSecondary}
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    disabled={isSaving}
                    className={btnPrimary}
                  >
                    {isSaving ? 'Guardando...' : 'Guardar Cambios'}
                  </button>
                </div>
              </form>
            )}
          </div>
        )}

        {/* ── Contenido de la pestaña: Sedes ───────────────────────── */}
        {activeTab === 'sedes' && (
          <div className="p-6 space-y-5">
            <div className="flex items-center justify-between">
              <p className="text-sm text-slate-500">
                {allHqs.length > 0
                  ? `${allHqs.length} sede${allHqs.length !== 1 ? 's' : ''} registrada${allHqs.length !== 1 ? 's' : ''}.`
                  : 'Este cliente aún no tiene sedes registradas.'}
              </p>
              {cities.length === 0 ? (
                <span className="text-xs text-amber-600 bg-amber-50 border border-amber-100 rounded-lg p-2">
                  ⚠️ Registre ciudades en Ubicaciones para habilitar creación
                </span>
              ) : (
                <button
                  id="btn-agregar-sede"
                  onClick={() => setShowHqForm((v) => !v)}
                  className="px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors flex items-center gap-1.5"
                >
                  {showHqForm ? '✕ Cancelar' : '+ Agregar Sede'}
                </button>
              )}
            </div>

            {/* Formulario de nueva sede */}
            {showHqForm && (
              <form onSubmit={handleAddHeadquarter} className="bg-slate-50 border border-slate-200 rounded-xl p-5 space-y-4">
                <h3 className="text-sm font-semibold text-slate-700">Nueva Sede</h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  <div className="sm:col-span-2">
                    <FormLabel>Nombre de la Sede</FormLabel>
                    <input
                      required
                      type="text"
                      value={hqForm.nombreSede}
                      onChange={(e) => setHqForm((p) => ({ ...p, nombreSede: e.target.value }))}
                      placeholder="Ej. Sede Principal"
                      className={inputCls}
                    />
                  </div>
                  <div>
                    <FormLabel>Calle</FormLabel>
                    <input
                      required
                      type="text"
                      value={hqForm.direccionCalleSede}
                      onChange={(e) => setHqForm((p) => ({ ...p, direccionCalleSede: e.target.value }))}
                      placeholder="Ej. Calle 123"
                      className={inputCls}
                    />
                  </div>
                  <div>
                    <FormLabel>Carrera</FormLabel>
                    <input
                      required
                      type="text"
                      value={hqForm.direccionCarreraSede}
                      onChange={(e) => setHqForm((p) => ({ ...p, direccionCarreraSede: e.target.value }))}
                      placeholder="Ej. Carrera 45"
                      className={inputCls}
                    />
                  </div>
                  <div>
                    <FormLabel>Número</FormLabel>
                    <input
                      required
                      type="text"
                      value={hqForm.direccionNumeroSede}
                      onChange={(e) => setHqForm((p) => ({ ...p, direccionNumeroSede: e.target.value }))}
                      placeholder="Ej. 67-89"
                      className={inputCls}
                    />
                  </div>
                  <div>
                    <FormLabel>Ciudad</FormLabel>
                    <select
                      required
                      value={hqForm.identificadorCiudad}
                      onChange={(e) => setHqForm((p) => ({ ...p, identificadorCiudad: e.target.value }))}
                      className={inputCls}
                    >
                      {cities.map((city) => (
                        <option key={city.id} value={city.id}>
                          {city.name} ({city.id})
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
                <div className="flex justify-end gap-2 pt-1">
                  <button type="button" onClick={() => setShowHqForm(false)} className={btnSecondary}>Cancelar</button>
                  <button type="submit" disabled={isSaving} className={btnPrimary}>
                    {isSaving ? 'Guardando...' : 'Registrar Sede'}
                  </button>
                </div>
              </form>
            )}

            {/* Lista de sedes */}
            {allHqs.length === 0 && !showHqForm && (
              <EmptyState icon="🏢" message="No hay sedes registradas para este cliente." />
            )}
            <div className="space-y-3">
              {allHqs.map((hq) => {
                const foundCity = cities.find((city) => city.id === hq.identificadorCiudad);
                return (
                  <div key={hq.identificadorSede} className="bg-white border border-slate-200 rounded-xl p-4 hover:border-slate-300 transition-colors">
                    <div className="flex items-start justify-between gap-4">
                      <div className="space-y-1">
                        <h4 className="font-semibold text-slate-800 text-sm">{hq.nombreSede}</h4>
                        <p className="text-xs text-slate-500">📍 {buildAddress(hq)}</p>
                        <p className="text-xs text-slate-400">
                          Ciudad: <span className="font-medium text-slate-600">{foundCity ? foundCity.name : hq.identificadorCiudad}</span>
                        </p>
                        {hq.serviceAreaList?.filter(a => a.estadoActivo !== false).length > 0 && (
                          <p className="text-xs text-blue-600">
                            {hq.serviceAreaList.filter(a => a.estadoActivo !== false).length} área(s) de servicio
                          </p>
                        )}
                      </div>
                      <button
                        onClick={() => handleDeleteHq(hq.identificadorSede, hq.nombreSede)}
                        className="px-2.5 py-1 text-red-600 hover:text-red-700 hover:bg-red-50 text-xs font-semibold rounded-md border border-red-100 transition-colors shrink-0"
                      >
                        Eliminar
                      </button>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* ── Contenido de la pestaña: Áreas de Servicio ───────────── */}
        {activeTab === 'areas' && (
          <div className="p-6 space-y-5">
            <div className="flex items-center justify-between">
              <p className="text-sm text-slate-500">
                {allAreas.length > 0
                  ? `${allAreas.length} área${allAreas.length !== 1 ? 's' : ''} de servicio registrada${allAreas.length !== 1 ? 's' : ''}.`
                  : 'Este cliente aún no tiene áreas de servicio.'}
              </p>
              {allHqs.length === 0 ? (
                <span className="text-xs text-amber-600 bg-amber-50 border border-amber-100 rounded-lg p-2">
                  ⚠️ Registre una sede primero para agregar áreas de servicio
                </span>
              ) : (
                <button
                  id="btn-agregar-area"
                  onClick={() => setShowSaForm((v) => !v)}
                  className="px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors flex items-center gap-1.5"
                >
                  {showSaForm ? '✕ Cancelar' : '+ Agregar Área'}
                </button>
              )}
            </div>

            {/* Formulario de nueva área */}
            {showSaForm && (
              <form onSubmit={handleAddServiceArea} className="bg-slate-50 border border-slate-200 rounded-xl p-5 space-y-4">
                <h3 className="text-sm font-semibold text-slate-700">Nueva Área de Servicio</h3>
                
                <div className="grid grid-cols-1 gap-4">
                  {/* Select Sede */}
                  <div>
                    <FormLabel>Sede Vinculada</FormLabel>
                    <select
                      required
                      value={saForm.identificadorSede}
                      onChange={(e) => setSaForm((p) => ({ ...p, identificadorSede: e.target.value }))}
                      className={inputCls}
                    >
                      {allHqs.map((hq) => (
                        <option key={hq.identificadorSede} value={hq.identificadorSede}>
                          {hq.nombreSede}
                        </option>
                      ))}
                    </select>
                  </div>

                  {/* Nombre */}
                  <div>
                    <FormLabel>Nombre del Área</FormLabel>
                    <input
                      required
                      type="text"
                      value={saForm.nombreAreaServicio}
                      onChange={(e) => setSaForm((p) => ({ ...p, nombreAreaServicio: e.target.value }))}
                      placeholder="Ej. Consultorio Médico, UCI, Urgencias"
                      className={inputCls}
                    />
                  </div>
                </div>
                
                <div className="flex justify-end gap-2 pt-1">
                  <button type="button" onClick={() => setShowSaForm(false)} className={btnSecondary}>Cancelar</button>
                  <button type="submit" disabled={isSaving} className={btnPrimary}>
                    {isSaving ? 'Guardando...' : 'Registrar Área'}
                  </button>
                </div>
              </form>
            )}

            {/* Lista de áreas */}
            {allAreas.length === 0 && !showSaForm && (
              <EmptyState icon="🗂️" message="No hay áreas de servicio registradas para este cliente." />
            )}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {allAreas.map((area) => {
                const associatedHq = allHqs.find((h) => h.identificadorSede === area.identificadorSede);
                const activeEquipments = (area.clientEquipmentList ?? []).filter(e => e.estadoActivo !== false);
                const isFormOpen = activeAreaIdForEquipmentForm === area.identificadorAreaServicio;

                return (
                  <div key={area.identificadorAreaServicio} className="bg-white border border-slate-200 rounded-xl p-5 hover:border-slate-300 transition-all flex flex-col justify-between shadow-sm">
                    <div>
                      <div className="flex items-start justify-between gap-4 border-b border-slate-100 pb-3 mb-3">
                        <div className="space-y-1">
                          <h4 className="font-bold text-slate-800 text-base">{area.nombreAreaServicio}</h4>
                          <p className="text-xs text-slate-500 font-medium flex items-center gap-1">
                            <span>🏢 Sede:</span>
                            <span className="font-semibold text-slate-700">{associatedHq ? associatedHq.nombreSede : 'Sede Desconocida'}</span>
                          </p>
                        </div>
                        <button
                          onClick={() => handleDeleteArea(area.identificadorAreaServicio, area.nombreAreaServicio)}
                          className="px-2 py-1 text-red-500 hover:text-red-700 hover:bg-red-50 text-xs font-bold rounded-lg border border-transparent hover:border-red-100 transition-colors shrink-0"
                        >
                          ✕ Eliminar Área
                        </button>
                      </div>

                      {/* LISTADO DE EQUIPOS BIOMÉDICOS */}
                      <div className="space-y-3">
                        <div className="flex justify-between items-center">
                          <h5 className="text-xs font-bold text-slate-400 uppercase tracking-wider">Equipos Biomédicos ({activeEquipments.length})</h5>
                          <button
                            onClick={() => {
                              if (isFormOpen) {
                                setActiveAreaIdForEquipmentForm(null);
                              } else {
                                setActiveAreaIdForEquipmentForm(area.identificadorAreaServicio);
                                setEqSerie('');
                                setEqInventario('');
                                setEqFechaCompra('');
                                setEqValorCompra('');
                                setEqModeloId('');
                              }
                            }}
                            className="text-xs text-blue-600 hover:text-blue-700 font-semibold flex items-center gap-1"
                          >
                            {isFormOpen ? '✕ Cancelar' : '+ Vincular Equipo'}
                          </button>
                        </div>

                        {/* Formulario para agregar equipo */}
                        {isFormOpen && (
                          <form onSubmit={(e) => handleAddEquipment(e, area.identificadorAreaServicio)} className="bg-slate-50/70 border border-slate-200 rounded-xl p-3.5 space-y-3 animate-fade-in my-2">
                            <p className="text-xs font-bold text-slate-700">Registrar Equipo Físico</p>
                            <div className="grid grid-cols-2 gap-2">
                              <div className="col-span-2">
                                <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-0.5">Modelo del Equipo</label>
                                <select
                                  value={eqModeloId}
                                  onChange={(e) => setEqModeloId(e.target.value)}
                                  className="w-full px-2.5 py-1.5 border border-slate-300 rounded-lg text-xs bg-white focus:ring-2 focus:ring-blue-500 focus:border-transparent text-slate-700 font-medium"
                                >
                                  <option value="">-- Sin Modelo (Opcional) --</option>
                                  {models.map((m) => {
                                    const mfg = m.manufacturerResponse?.name || '';
                                    const eqName = m.equipmentResponse?.name || '';
                                    const label = mfg && eqName ? `${mfg} - ${eqName} (${m.invima})` : `${m.invima} (ID: ${m.id.substring(0,8)})`;
                                    return (
                                      <option key={m.id} value={m.id}>
                                        {label}
                                      </option>
                                    );
                                  })}
                                </select>
                              </div>
                              <div className="col-span-2">
                                <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-0.5">Serie *</label>
                                <input
                                  required
                                  type="text"
                                  placeholder="Ej. SN-B10293"
                                  value={eqSerie}
                                  onChange={(e) => setEqSerie(e.target.value)}
                                  className="w-full px-2.5 py-1.5 border border-slate-300 rounded-lg text-xs bg-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                />
                              </div>
                              <div>
                                <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-0.5">No. Inventario</label>
                                <input
                                  type="text"
                                  placeholder="Ej. INV-9982"
                                  value={eqInventario}
                                  onChange={(e) => setEqInventario(e.target.value)}
                                  className="w-full px-2.5 py-1.5 border border-slate-300 rounded-lg text-xs bg-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                />
                              </div>
                              <div>
                                <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-0.5">Valor de Compra</label>
                                <input
                                  type="number"
                                  placeholder="Ej. 1200000"
                                  value={eqValorCompra}
                                  onChange={(e) => setEqValorCompra(e.target.value)}
                                  className="w-full px-2.5 py-1.5 border border-slate-300 rounded-lg text-xs bg-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                />
                              </div>
                              <div className="col-span-2">
                                <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-0.5">Fecha de Compra</label>
                                <input
                                  type="date"
                                  value={eqFechaCompra}
                                  onChange={(e) => setEqFechaCompra(e.target.value)}
                                  className="w-full px-2.5 py-1.5 border border-slate-300 rounded-lg text-xs bg-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                />
                              </div>
                            </div>
                            <div className="flex justify-end gap-1.5 pt-1">
                              <button
                                type="button"
                                onClick={() => setActiveAreaIdForEquipmentForm(null)}
                                className="px-2.5 py-1.5 bg-slate-100 hover:bg-slate-200 text-slate-600 text-[11px] font-semibold rounded-lg transition-colors"
                              >
                                Cancelar
                              </button>
                              <button
                                type="submit"
                                disabled={isSaving}
                                className="px-2.5 py-1.5 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white text-[11px] font-semibold rounded-lg transition-colors"
                              >
                                {isSaving ? 'Guardando...' : 'Vincular'}
                              </button>
                            </div>
                          </form>
                        )}

                        {/* Listado de equipos asignados */}
                        {activeEquipments.length === 0 ? (
                          <p className="text-xs text-slate-400 italic py-1">No hay equipos vinculados a este área de servicio.</p>
                        ) : (
                          <div className="space-y-1.5 max-h-[180px] overflow-y-auto pr-1">
                            {activeEquipments.map((equip) => {
                              const eqModel = models.find(m => m.id === equip.identificadorModelo);
                              const mfg = eqModel?.manufacturerResponse?.name || '';
                              const eqName = eqModel?.equipmentResponse?.name || '';
                              const modelDisplay = mfg && eqName ? `${mfg} ${eqName}` : (eqModel?.invima || '');

                              return (
                                <div key={equip.identificadorEquipoCliente} className="flex justify-between items-center p-2 rounded-lg border border-slate-100 bg-slate-50/50 hover:bg-blue-50/30 transition-all">
                                  <div className="space-y-0.5">
                                    <div className="flex items-center gap-1.5">
                                      <span className="text-[10px] font-bold text-slate-400 uppercase">Serie:</span>
                                      <span className="text-xs font-semibold text-slate-800">{equip.serie}</span>
                                    </div>
                                    {modelDisplay && (
                                      <div className="flex items-center gap-1.5">
                                        <span className="text-[9px] font-bold text-slate-400 uppercase">Modelo:</span>
                                        <span className="text-[11px] font-medium text-slate-700 bg-blue-50/60 text-blue-700 px-1.5 py-0.5 rounded">{modelDisplay}</span>
                                      </div>
                                    )}
                                    {equip.numeroInventario && (
                                      <div className="flex items-center gap-1.5">
                                        <span className="text-[9px] font-bold text-slate-400 uppercase">Inventario:</span>
                                        <span className="text-xs font-mono font-medium text-slate-600">{equip.numeroInventario}</span>
                                      </div>
                                    )}
                                  </div>
                                  <button
                                    type="button"
                                    onClick={() => handleDeleteEquipment(area.identificadorAreaServicio, equip.identificadorEquipoCliente, equip.serie)}
                                    className="text-red-400 hover:text-red-600 p-1.5 font-bold text-sm shrink-0 hover:bg-red-50 rounded-lg transition-colors"
                                    title="Desvincular Equipo"
                                  >
                                    ✕
                                  </button>
                                </div>
                              );
                            })}
                          </div>
                        )}
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

// ─── Sub-componentes de utilidad ─────────────────────────────────────────────

const InfoRow: React.FC<{ label: string; value: string; mono?: boolean }> = ({ label, value, mono }) => (
  <div>
    <p className="text-xs text-slate-400">{label}</p>
    <p className={`text-sm text-slate-700 font-semibold ${mono ? 'font-mono' : ''}`}>{value}</p>
  </div>
);

const FormLabel: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">{children}</label>
);

const EmptyState: React.FC<{ icon: string; message: string }> = ({ icon, message }) => (
  <div className="py-10 text-center space-y-2">
    <span className="text-4xl">{icon}</span>
    <p className="text-sm text-slate-500">{message}</p>
    <p className="text-xs text-slate-400">Usa el botón de arriba para agregar el primero.</p>
  </div>
);

// ─── Clases de utilidad reutilizables ────────────────────────────────────────
const inputCls = 'w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all bg-white';
const btnPrimary = 'px-4 py-2 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-medium text-sm rounded-lg transition-colors';
const btnSecondary = 'px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium text-sm rounded-lg transition-colors';
