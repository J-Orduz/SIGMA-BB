import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useClients } from '../hooks/useClients';
import type { ClientCreateRequest } from '../types/client.types';
import { TIPO_IDENTIFICACION_OPTIONS } from '../types/client.types';
import { useCountries } from '../../locations/hooks/useCountries';
import { usePersons } from '../../persons/hooks/usePersons';

// ─── Estado del modal de confirmación de borrado ────────────────────────────
interface DeleteModalState {
  isOpen: boolean;
  clientId: string | null;
  clientName: string;
}

// ─── Formulario de nuevo cliente (estado inicial) ───────────────────────────
const FORM_INITIAL: ClientCreateRequest = {
  identificadorCliente: '',
  tipoIdentifiacion: 'NIT_juridico',
  razonSocial: '',
  identificadorPais: 'COL',
  emailClientList: [],
  phoneClientList: [],
  headquarterList: [],
};

// ─── Componente Principal ────────────────────────────────────────────────────
export const ClientManager: React.FC = () => {
  const { clients, isLoading, error, createClient, deleteClient, setError } = useClients();
  const { countries } = useCountries();
  const { persons } = usePersons();
  const navigate = useNavigate();

  const [form, setForm] = useState<ClientCreateRequest>(FORM_INITIAL);
  const [successMsg, setSuccessMsg] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [repLegal, setRepLegal] = useState('');

  // Set default country once countries are loaded
  React.useEffect(() => {
    if (countries.length > 0 && (!form.identificadorPais || form.identificadorPais === 'COL')) {
      const hasCol = countries.some(c => c.id === 'COL');
      if (!hasCol) {
        setForm((prev) => ({ ...prev, identificadorPais: countries[0].id }));
      }
    }
  }, [countries, form.identificadorPais]);

  // Campos dinámicos de contacto en el formulario de creación
  const [emailInputs, setEmailInputs] = useState<string[]>(['']);
  const [phoneInputs, setPhoneInputs] = useState<string[]>(['']);

  const [deleteModal, setDeleteModal] = useState<DeleteModalState>({
    isOpen: false,
    clientId: null,
    clientName: '',
  });

  // ── Helpers ────────────────────────────────────────────────────────────────

  const showSuccess = (msg: string) => {
    setSuccessMsg(msg);
    setTimeout(() => setSuccessMsg(''), 4000);
  };

  const activeClients = clients.filter((c) => c.estadoActivo !== false);

  const filteredClients = activeClients.filter((c) =>
    c.razonSocial.toLowerCase().includes(searchTerm.toLowerCase()) ||
    c.identificadorCliente.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // ── Manejo del formulario ──────────────────────────────────────────────────

  const handleFormChange = (field: keyof ClientCreateRequest, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleEmailChange = (index: number, value: string) => {
    const updated = [...emailInputs];
    updated[index] = value;
    setEmailInputs(updated);
  };

  const handlePhoneChange = (index: number, value: string) => {
    const updated = [...phoneInputs];
    updated[index] = value;
    setPhoneInputs(updated);
  };

  const addEmailField = () => setEmailInputs((prev) => [...prev, '']);
  const removeEmailField = (index: number) =>
    setEmailInputs((prev) => prev.filter((_, i) => i !== index));

  const addPhoneField = () => setPhoneInputs((prev) => [...prev, '']);
  const removePhoneField = (index: number) =>
    setPhoneInputs((prev) => prev.filter((_, i) => i !== index));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.identificadorCliente.trim() || !form.razonSocial.trim()) return;

    const cleanedId = form.identificadorCliente.replace(/[\.\s]/g, '').trim();

    const payload: ClientCreateRequest = {
      ...form,
      identificadorCliente: cleanedId,
      emailClientList: emailInputs
        .filter((e) => e.trim() !== '')
        .map((correoCliente) => ({ correoCliente })),
      phoneClientList: phoneInputs
        .filter((p) => p.trim() !== '')
        .map((telefonoCliente) => ({ telefonoCliente })),
      headquarterList: [],
      identificadorRepresentante: repLegal || undefined,
    };

    try {
      const created = await createClient(payload);
      if (repLegal) {
        localStorage.setItem(`rep_legal_${cleanedId}`, repLegal);
      }
      setForm(FORM_INITIAL);
      setEmailInputs(['']);
      setPhoneInputs(['']);
      setRepLegal('');
      showSuccess(`¡Cliente "${created.razonSocial}" registrado con éxito!`);
      // Navegar al detalle del cliente recién creado
      navigate(`/clients/${created.identificadorCliente}`);
    } catch {
      // El error ya está gestionado por el hook
    }
  };

  // ── Modal de eliminación ────────────────────────────────────────────────────

  const handleDeleteClick = (id: string, name: string) => {
    setDeleteModal({ isOpen: true, clientId: id, clientName: name });
  };

  const handleConfirmDelete = async () => {
    if (!deleteModal.clientId) return;
    const idToDelete = deleteModal.clientId;
    setDeleteModal({ isOpen: false, clientId: null, clientName: '' });
    try {
      await deleteClient(idToDelete);
      showSuccess('Cliente eliminado del sistema.');
    } catch {
      // El error ya está gestionado por el hook
    }
  };

  // ── Render ──────────────────────────────────────────────────────────────────

  return (
    <div className="p-4 md:p-6 max-w-7xl mx-auto space-y-8 relative">

      {/* ── Encabezado ──────────────────────────────────────────────── */}
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Gestión de Clientes</h1>
        <p className="text-sm text-slate-500 mt-1">
          Módulo administrativo para registrar, modificar y dar de baja clientes institucionales del sistema SIGMA-BB.
        </p>
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

      {/* ── Layout principal (grid 1/3 + 2/3) ───────────────────────── */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

        {/* ── Panel izquierdo: Formulario de registro ────────────────── */}
        <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm h-fit">
          <h2 className="text-lg font-semibold text-slate-700 mb-4">Nuevo Cliente</h2>
          <form onSubmit={handleSubmit} className="space-y-4">

            {/* Tipo de Identificación */}
            <div>
              <label className="block text-xs font-medium text-slate-600 uppercase tracking-wider mb-1">
                Tipo de Identificación
              </label>
              <select
                id="tipoIdentifiacion"
                value={form.tipoIdentifiacion}
                onChange={(e) => handleFormChange('tipoIdentifiacion', e.target.value)}
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              >
                {TIPO_IDENTIFICACION_OPTIONS.map((opt) => (
                  <option key={opt.value} value={opt.value}>{opt.label}</option>
                ))}
              </select>
            </div>

            {/* NIT / Identificador */}
            <div>
              <label className="block text-xs font-medium text-slate-600 uppercase tracking-wider mb-1">
                NIT / Identificador
              </label>
              <input
                id="identificadorCliente"
                type="text"
                value={form.identificadorCliente}
                onChange={(e) => handleFormChange('identificadorCliente', e.target.value)}
                placeholder="Ej. 900123456-7"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                required
              />
            </div>

            {/* Razón Social */}
            <div>
              <label className="block text-xs font-medium text-slate-600 uppercase tracking-wider mb-1">
                Razón Social
              </label>
              <input
                id="razonSocial"
                type="text"
                value={form.razonSocial}
                onChange={(e) => handleFormChange('razonSocial', e.target.value)}
                placeholder="Ej. Clínica San Rafael S.A.S."
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                required
              />
            </div>

            {/* País */}
            <div>
              <label className="block text-xs font-medium text-slate-600 uppercase tracking-wider mb-1">
                País
              </label>
              <select
                id="identificadorPais"
                value={form.identificadorPais}
                onChange={(e) => handleFormChange('identificadorPais', e.target.value)}
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              >
                {countries.length === 0 ? (
                  <>
                    <option value="COL">Colombia</option>
                    <option value="USA">Estados Unidos</option>
                    <option value="MEX">México</option>
                    <option value="VEN">Venezuela</option>
                    <option value="ECU">Ecuador</option>
                    <option value="PER">Perú</option>
                  </>
                ) : (
                  countries.map((c) => (
                    <option key={c.id} value={c.id}>{c.name}</option>
                  ))
                )}
              </select>
            </div>

            {/* Representante Legal */}
            <div>
              <label className="block text-xs font-medium text-slate-600 uppercase tracking-wider mb-1">
                Representante Legal
              </label>
              <select
                id="representanteLegal"
                value={repLegal}
                onChange={(e) => setRepLegal(e.target.value)}
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              >
                <option value="">-- Seleccione Representante --</option>
                {persons.filter(p => p.estadoActivo !== false).map((p) => (
                  <option key={p.cedula} value={p.cedula}>
                    {p.primerNombre} {p.primerApellido} ({p.cedula})
                  </option>
                ))}
              </select>
            </div>

            {/* Correos electrónicos */}
            <div>
              <div className="flex items-center justify-between mb-1">
                <label className="block text-xs font-medium text-slate-600 uppercase tracking-wider">
                  Correos Electrónicos
                </label>
                <button
                  type="button"
                  onClick={addEmailField}
                  className="text-xs text-blue-600 hover:text-blue-700 font-medium"
                >
                  + Agregar
                </button>
              </div>
              <div className="space-y-2">
                {emailInputs.map((email, idx) => (
                  <div key={idx} className="flex gap-2">
                    <input
                      type="email"
                      value={email}
                      onChange={(e) => handleEmailChange(idx, e.target.value)}
                      placeholder="correo@empresa.com"
                      className="flex-1 px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                    />
                    {emailInputs.length > 1 && (
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
                <label className="block text-xs font-medium text-slate-600 uppercase tracking-wider">
                  Teléfonos
                </label>
                <button
                  type="button"
                  onClick={addPhoneField}
                  className="text-xs text-blue-600 hover:text-blue-700 font-medium"
                >
                  + Agregar
                </button>
              </div>
              <div className="space-y-2">
                {phoneInputs.map((phone, idx) => (
                  <div key={idx} className="flex gap-2">
                    <input
                      type="tel"
                      value={phone}
                      onChange={(e) => handlePhoneChange(idx, e.target.value)}
                      placeholder="3123120302"
                      className="flex-1 px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                    />
                    {phoneInputs.length > 1 && (
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

            {/* Botón submit */}
            <button
              type="submit"
              id="btn-crear-cliente"
              disabled={isLoading}
              className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg transition-colors disabled:bg-blue-400 disabled:cursor-not-allowed"
            >
              {isLoading ? 'Guardando...' : 'Crear Cliente'}
            </button>
          </form>
        </div>

        {/* ── Panel derecho: Tabla de clientes ──────────────────────── */}
        <div className="md:col-span-2 bg-white p-5 rounded-xl border border-slate-200 shadow-sm space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
            <h2 className="text-lg font-semibold text-slate-700">Clientes Registrados</h2>

            {clients.length > 0 && (
              <div className="relative w-full sm:w-72">
                <span className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-slate-400 text-sm">🔍</span>
                <input
                  type="text"
                  id="search-clientes"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  placeholder="Buscar por nombre o NIT..."
                  className="w-full pl-9 pr-3 py-1.5 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all placeholder:text-slate-400 bg-slate-50/50"
                />
              </div>
            )}
          </div>

          {/* ── Estados de la tabla ──────────────────────────────────── */}
          {isLoading && clients.length === 0 ? (
            <div className="flex items-center gap-3 py-6 text-slate-400">
              <svg className="w-5 h-5 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z" />
              </svg>
              <span className="text-sm">Sincronizando con el servidor de SIGMA-BB...</span>
            </div>
          ) : clients.length === 0 ? (
            <div className="py-10 text-center space-y-2">
              <span className="text-4xl">🏢</span>
              <p className="text-sm text-slate-500">No hay clientes registrados.</p>
              <p className="text-xs text-slate-400">Usa el formulario de la izquierda para agregar el primero.</p>
            </div>
          ) : filteredClients.length === 0 ? (
            <p className="text-sm text-slate-400 bg-slate-50 p-4 rounded-lg text-center border border-dashed border-slate-200">
              No se encontraron resultados para "<span className="font-medium">{searchTerm}</span>"
            </p>
          ) : (
            <div className="overflow-x-auto rounded-lg border border-slate-100">
              <table className="w-full text-left text-sm text-slate-600">
                <thead className="bg-slate-50 text-xs text-slate-700 uppercase tracking-wider border-b border-slate-100">
                  <tr>
                    <th className="px-4 py-3">NIT</th>
                    <th className="px-4 py-3">Razón Social</th>
                    <th className="px-4 py-3">Tipo ID</th>
                    <th className="px-4 py-3 text-right">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {filteredClients.map((client) => (
                    <tr
                      key={client.identificadorCliente}
                      className="hover:bg-slate-50/70 transition-colors group"
                    >
                      {/* NIT */}
                      <td className="px-4 py-3">
                        <span className="font-mono text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded">
                          {client.identificadorCliente}
                        </span>
                      </td>

                      {/* Razón Social */}
                      <td className="px-4 py-3">
                        <div className="font-medium text-slate-800">{client.razonSocial}</div>
                        {/* Contacto resumido */}
                        {(client.correosCliente?.length > 0 || client.telefonosCliente?.length > 0) && (
                          <div className="text-xs text-slate-400 mt-0.5 flex flex-wrap gap-x-2">
                            {client.correosCliente?.[0] && (
                              <span>✉️ {client.correosCliente[0].correoCliente}</span>
                            )}
                            {client.telefonosCliente?.[0] && (
                              <span>📞 {client.telefonosCliente[0].telefonoCliente}</span>
                            )}
                          </div>
                        )}
                      </td>

                      {/* Tipo ID */}
                      <td className="px-4 py-3">
                        <span className="text-xs text-slate-500">{client.tipoIdentifiacion}</span>
                      </td>

                      {/* Acciones */}
                      <td className="px-4 py-3 text-right whitespace-nowrap">
                        <div className="flex justify-end gap-2">
                          <button
                            onClick={() => navigate(`/clients/${client.identificadorCliente}`)}
                            className="px-2.5 py-1 text-blue-600 hover:text-blue-700 hover:bg-blue-50 text-xs font-semibold rounded-md border border-blue-200 transition-colors"
                          >
                            Ver Detalle
                          </button>
                          <button
                            onClick={() => handleDeleteClick(client.identificadorCliente, client.razonSocial)}
                            className="px-2.5 py-1 text-red-600 hover:text-red-700 hover:bg-red-50 text-xs font-semibold rounded-md border border-red-100 transition-colors"
                          >
                            Eliminar
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {/* Contador de resultados */}
          {activeClients.length > 0 && (
            <p className="text-xs text-slate-400 text-right">
              {filteredClients.length} de {activeClients.length} cliente{activeClients.length !== 1 ? 's' : ''}
            </p>
          )}
        </div>
      </div>

      {/* ── Modal de confirmación de eliminación ─────────────────────── */}
      {deleteModal.isOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-sm">
          <div className="bg-white rounded-xl shadow-xl border border-slate-200 max-w-md w-full p-6 space-y-6">
            <div className="flex items-start gap-4">
              <div className="p-3 bg-red-50 text-red-600 rounded-full shrink-0">
                <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                  <path strokeLinecap="round" strokeLinejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </div>
              <div className="space-y-1">
                <h3 className="text-lg font-bold text-slate-800">¿Eliminar cliente?</h3>
                <p className="text-sm text-slate-500">
                  Está a punto de dar de baja al cliente{' '}
                  <span className="font-semibold text-slate-700">"{deleteModal.clientName}"</span>.
                  Esta acción no se puede deshacer si el cliente no tiene históricos vinculados.
                </p>
              </div>
            </div>
            <div className="flex justify-end gap-3 pt-2">
              <button
                type="button"
                onClick={() => setDeleteModal({ isOpen: false, clientId: null, clientName: '' })}
                className="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium text-sm rounded-lg transition-colors"
              >
                No, cancelar
              </button>
              <button
                type="button"
                onClick={handleConfirmDelete}
                className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white font-medium text-sm rounded-lg transition-colors shadow-sm"
              >
                Sí, eliminar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
