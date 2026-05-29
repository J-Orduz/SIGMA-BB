import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { clientService } from '../services/client.service';
import { headquarterService } from '../services/headquarter.service';
import { serviceAreaService } from '../services/serviceArea.service';
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
  serviceAreaList: [],
  managerList: [],
};

const SA_FORM_INITIAL: ServiceAreaCreateRequest = {
  nombreAreaServicio: '',
  managerList: [],
};

// ─── Componente Principal ────────────────────────────────────────────────────
export const ClientDetailPage: React.FC = () => {
  const { clientId } = useParams<{ clientId: string }>();
  const navigate = useNavigate();

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
      // Filtrar sedes y áreas que pertenecen a este cliente
      const clientHqIds = new Set(clientData.headquarterClientList.map((h) => h.identificadorSede));
      const clientHqs = hqData.filter((h) => clientHqIds.has(h.identificadorSede));
      setAllHqs(clientHqs);
      const hqAreaIds = new Set(clientHqs.flatMap((h) => h.serviceAreaList.map((a) => a.identificadorAreaServicio)));
      setAllAreas(areaData.filter((a) => hqAreaIds.has(a.identificadorAreaServicio)));
    } catch {
      setError('No se pudo cargar la información del cliente. Verifique la conexión con el servidor.');
    } finally {
      setIsLoading(false);
    }
  }, [clientId]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  // ── Agregar Sede ────────────────────────────────────────────────────────────

  const handleAddHeadquarter = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSaving(true);
    setError(null);
    try {
      await headquarterService.create(hqForm);
      setHqForm(HQ_FORM_INITIAL);
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
    setIsSaving(true);
    setError(null);
    try {
      await serviceAreaService.create(saForm);
      setSaForm(SA_FORM_INITIAL);
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
            <div className="flex items-center gap-3">
              <h1 className="text-xl font-bold text-white">{client.razonSocial}</h1>
              {client.estadoActivo ? (
                <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-xs font-medium bg-green-500/20 text-green-300 border border-green-500/30">
                  <span className="w-1.5 h-1.5 rounded-full bg-green-400 inline-block"></span> Activo
                </span>
              ) : (
                <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-xs font-medium bg-slate-500/30 text-slate-300">
                  <span className="w-1.5 h-1.5 rounded-full bg-slate-400 inline-block"></span> Inactivo
                </span>
              )}
            </div>
            <p className="text-sm text-slate-400 font-mono">{client.tipoIdentifiacion}: {client.identificadorCliente}</p>
            <p className="text-xs text-slate-500">País: {client.identificadorPais}</p>
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
            {client.correosCliente?.filter(e => e.estadoActivo).map((e) => (
              <span key={e.identificadorCorreoCliente} className="text-xs text-slate-500 flex items-center gap-1">
                <span className="text-slate-400">✉️</span> {e.correoCliente}
              </span>
            ))}
            {client.telefonosCliente?.filter(p => p.estadoActivo).map((p) => (
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
            { key: 'sedes' as Tab, label: '🏢 Sedes', count: client.headquarterClientList?.length ?? 0 },
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
          <div className="p-6 space-y-6">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
              <div className="space-y-4">
                <h3 className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Datos Institucionales</h3>
                <InfoRow label="NIT / Identificador" value={client.identificadorCliente} mono />
                <InfoRow label="Tipo de Identificación" value={client.tipoIdentifiacion} />
                <InfoRow label="Razón Social" value={client.razonSocial} />
                <InfoRow label="País" value={client.identificadorPais} />
                <InfoRow label="Estado" value={client.estadoActivo ? 'Activo' : 'Inactivo'} />
              </div>
              <div className="space-y-4">
                <h3 className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Información de Contacto</h3>
                {client.correosCliente?.length > 0 ? (
                  <div>
                    <p className="text-xs text-slate-500 mb-1">Correos Electrónicos</p>
                    {client.correosCliente.filter(e => e.estadoActivo).map((e) => (
                      <p key={e.identificadorCorreoCliente} className="text-sm text-slate-800">✉️ {e.correoCliente}</p>
                    ))}
                  </div>
                ) : (
                  <p className="text-sm text-slate-400 italic">Sin correos registrados.</p>
                )}
                {client.telefonosCliente?.length > 0 ? (
                  <div>
                    <p className="text-xs text-slate-500 mb-1">Teléfonos</p>
                    {client.telefonosCliente.filter(p => p.estadoActivo).map((p) => (
                      <p key={p.identificadorTelefonoCliente} className="text-sm text-slate-800">📞 {p.telefonoCliente}</p>
                    ))}
                  </div>
                ) : (
                  <p className="text-sm text-slate-400 italic">Sin teléfonos registrados.</p>
                )}
              </div>
            </div>
            <div className="pt-2 border-t border-slate-100">
              <p className="text-xs text-slate-400">
                Para modificar la información de este cliente, utilice el módulo de administración del backend o contacte al administrador del sistema.
              </p>
            </div>
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
              <button
                id="btn-agregar-sede"
                onClick={() => setShowHqForm((v) => !v)}
                className="px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors flex items-center gap-1.5"
              >
                {showHqForm ? '✕ Cancelar' : '+ Agregar Sede'}
              </button>
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
                    <input
                      required
                      type="text"
                      value={hqForm.identificadorCiudad}
                      onChange={(e) => setHqForm((p) => ({ ...p, identificadorCiudad: e.target.value }))}
                      placeholder="Ej. BOG, MDE, CTG"
                      className={inputCls}
                    />
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
              {allHqs.map((hq) => (
                <div key={hq.identificadorSede} className="bg-white border border-slate-200 rounded-xl p-4 hover:border-slate-300 transition-colors">
                  <div className="flex items-start justify-between gap-4">
                    <div className="space-y-1">
                      <div className="flex items-center gap-2">
                        <h4 className="font-semibold text-slate-800 text-sm">{hq.nombreSede}</h4>
                        {hq.estadoActivo ? (
                          <span className="text-xs text-green-600 bg-green-50 px-1.5 py-0.5 rounded-full">Activa</span>
                        ) : (
                          <span className="text-xs text-slate-500 bg-slate-100 px-1.5 py-0.5 rounded-full">Inactiva</span>
                        )}
                      </div>
                      <p className="text-xs text-slate-500">📍 {buildAddress(hq)}</p>
                      <p className="text-xs text-slate-400">Ciudad: {hq.identificadorCiudad}</p>
                      {hq.serviceAreaList?.length > 0 && (
                        <p className="text-xs text-blue-600">{hq.serviceAreaList.length} área{hq.serviceAreaList.length !== 1 ? 's' : ''} de servicio</p>
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
              ))}
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
              <button
                id="btn-agregar-area"
                onClick={() => setShowSaForm((v) => !v)}
                className="px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors flex items-center gap-1.5"
              >
                {showSaForm ? '✕ Cancelar' : '+ Agregar Área'}
              </button>
            </div>

            {/* Formulario de nueva área */}
            {showSaForm && (
              <form onSubmit={handleAddServiceArea} className="bg-slate-50 border border-slate-200 rounded-xl p-5 space-y-4">
                <h3 className="text-sm font-semibold text-slate-700">Nueva Área de Servicio</h3>
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
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              {allAreas.map((area) => (
                <div key={area.identificadorAreaServicio} className="bg-white border border-slate-200 rounded-xl p-4 hover:border-slate-300 transition-colors">
                  <div className="flex items-start justify-between gap-4">
                    <div className="space-y-1">
                      <h4 className="font-semibold text-slate-800 text-sm">{area.nombreAreaServicio}</h4>
                      {area.estadoActivo ? (
                        <span className="text-xs text-green-600 bg-green-50 px-1.5 py-0.5 rounded-full">Activa</span>
                      ) : (
                        <span className="text-xs text-slate-500 bg-slate-100 px-1.5 py-0.5 rounded-full">Inactiva</span>
                      )}
                      <p className="text-xs text-slate-400 font-mono mt-1">ID: {area.identificadorAreaServicio.slice(0, 8)}...</p>
                    </div>
                    <button
                      onClick={() => handleDeleteArea(area.identificadorAreaServicio, area.nombreAreaServicio)}
                      className="px-2.5 py-1 text-red-600 hover:text-red-700 hover:bg-red-50 text-xs font-semibold rounded-md border border-red-100 transition-colors shrink-0"
                    >
                      Eliminar
                    </button>
                  </div>
                </div>
              ))}
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
    <p className="text-xs text-slate-500">{label}</p>
    <p className={`text-sm text-slate-800 font-medium ${mono ? 'font-mono' : ''}`}>{value}</p>
  </div>
);

const FormLabel: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <label className="block text-xs font-medium text-slate-600 uppercase tracking-wider mb-1">{children}</label>
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
