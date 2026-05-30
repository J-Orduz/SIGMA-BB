import React, { useState } from 'react';
import { usePersons } from '../hooks/usePersons';
import type { PersonCreateRequest, PersonUpdateRequest, Person } from '../types/person.types';

interface DeleteModalState {
  isOpen: boolean;
  personId: string | null;
  personName: string;
}

export const PersonManager: React.FC = () => {
  const { persons, isLoading, error, createPerson, updatePerson, deletePerson, setError } = usePersons();

  // Estados del Formulario
  const [cedula, setCedula] = useState('');
  const [primerNombre, setPrimerNombre] = useState('');
  const [segundoNombre, setSegundoNombre] = useState('');
  const [primerApellido, setPrimerApellido] = useState('');
  const [segundoApellido, setSegundoApellido] = useState('');
  const [nombreUsuario, setNombreUsuario] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState<'CEO_CLIENT' | 'ENGINEER' | 'ADMIN'>('ADMIN');

  // Campos dinámicos de contacto
  const [emailInputs, setEmailInputs] = useState<string[]>(['']);
  const [phoneInputs, setPhoneInputs] = useState<string[]>(['']);

  // Estados de edición y control
  const [editingId, setEditingId] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState('');
  const [searchTerm, setSearchTerm] = useState('');

  const [deleteModal, setDeleteModal] = useState<DeleteModalState>({
    isOpen: false,
    personId: null,
    personName: '',
  });

  const showSuccess = (msg: string) => {
    setSuccessMsg(msg);
    setTimeout(() => setSuccessMsg(''), 4000);
  };

  const resetForm = () => {
    setCedula('');
    setPrimerNombre('');
    setSegundoNombre('');
    setPrimerApellido('');
    setSegundoApellido('');
    setNombreUsuario('');
    setPassword('');
    setRole('ADMIN');
    setEmailInputs(['']);
    setPhoneInputs(['']);
    setEditingId(null);
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
  const removeEmailField = (index: number) => setEmailInputs((prev) => prev.filter((_, i) => i !== index));

  const addPhoneField = () => setPhoneInputs((prev) => [...prev, '']);
  const removePhoneField = (index: number) => setPhoneInputs((prev) => prev.filter((_, i) => i !== index));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!cedula.trim() || !primerNombre.trim() || !primerApellido.trim()) {
      setError('Por favor, rellene todos los campos obligatorios.');
      return;
    }

    const isRepLegal = role === 'CEO_CLIENT';

    if (!editingId && !isRepLegal && (!nombreUsuario.trim() || !password.trim())) {
      setError('El nombre de usuario y la contraseña son obligatorios para crear una identidad.');
      return;
    }

    try {
      if (editingId) {
        // En actualización el backend requiere PersonUpdateRequest
        const updatePayload: PersonUpdateRequest = {
          cedula: cedula.trim(),
          primerNombre: primerNombre.trim(),
          segundoNombre: segundoNombre.trim() || undefined,
          primerApellido: primerApellido.trim(),
          segundoApellido: segundoApellido.trim() || '.',
          tipoPersona: role,
        };
        await updatePerson(editingId, updatePayload);
        showSuccess('¡Persona actualizada con éxito!');
      } else {
        // Creación
        const createPayload: PersonCreateRequest = {
          cedula: cedula.trim(),
          primerNombre: primerNombre.trim(),
          segundoNombre: segundoNombre.trim() || undefined,
          primerApellido: primerApellido.trim(),
          segundoApellido: segundoApellido.trim() || '.',
          nombreUsuario: isRepLegal ? `rep_${cedula.trim()}` : nombreUsuario.trim(),
          password: isRepLegal ? `Rep_${cedula.trim()}_123!` : password,
          tipoPersona: isRepLegal ? 'CEO_CLIENT' : undefined,
          emailPersonList: emailInputs.filter(e => e.trim() !== '').map(correoPersona => ({ correoPersona })),
          phonePersonList: phoneInputs.filter(p => p.trim() !== '').map(telefonoPersona => ({ telefonoPersona })),
        };
        await createPerson(createPayload, role);
        showSuccess('¡Persona registrada con éxito!');
      }
      resetForm();
    } catch (err) {
      // Manejado en el hook
    }
  };

  const handleStartEdit = (person: Person) => {
    setEditingId(person.identificador);
    setCedula(person.cedula);
    setPrimerNombre(person.primerNombre);
    setSegundoNombre(person.segundoNombre || '');
    setPrimerApellido(person.primerApellido);
    setSegundoApellido(person.segundoApellido === '.' ? '' : person.segundoApellido || '');
    setRole((person.tipoPersona as any) || 'ADMIN');
    // Para correos y teléfonos
    setEmailInputs(person.emailPersonList?.map(e => e.correoPersona) || ['']);
    setPhoneInputs(person.phonePersonList?.map(p => p.telefonoPersona) || ['']);
  };

  const triggerDeleteClick = (id: string, name: string) => {
    setDeleteModal({ isOpen: true, personId: id, personName: name });
  };

  const handleConfirmDelete = async () => {
    if (!deleteModal.personId) return;
    const idToDelete = deleteModal.personId;
    setDeleteModal({ isOpen: false, personId: null, personName: '' });
    try {
      await deletePerson(idToDelete);
      showSuccess('Persona dada de baja en el sistema.');
    } catch (err) {
      // Manejado por el hook
    }
  };

  const activePersons = persons.filter((p) => p.estadoActivo !== false);

  const filteredPersons = activePersons.filter((p) => {
    const term = searchTerm.toLowerCase();
    const fullName = `${p.primerNombre} ${p.segundoNombre || ''} ${p.primerApellido} ${p.segundoApellido || ''}`.toLowerCase();
    return (
      p.cedula.toLowerCase().includes(term) ||
      fullName.includes(term) ||
      p.tipoPersona.toLowerCase().includes(term)
    );
  });

  const getRoleBadgeClass = (roleStr: string) => {
    switch (roleStr) {
      case 'SUPER_ADMIN':
      case 'ADMIN':
        return 'bg-purple-100 text-purple-700 border border-purple-200';
      case 'ENGINEER':
        return 'bg-blue-100 text-blue-700 border border-blue-200';
      case 'CEO_CLIENT':
        return 'bg-green-100 text-green-700 border border-green-200';
      default:
        return 'bg-slate-100 text-slate-700 border border-slate-200';
    }
  };

  return (
    <div className="p-4 md:p-6 max-w-7xl mx-auto space-y-8 relative">
      {/* ── Encabezado ──────────────────────────────────────────────── */}
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Gestión de Personas</h1>
        <p className="text-sm text-slate-500 mt-1">
          Módulo para registrar personal de la empresa, ingenieros de servicio, administradores y representantes legales de clientes en SIGMA-BB.
        </p>
      </div>

      {/* ── Alertas de Feedback ──────────────────────────────────────── */}
      {error && (
        <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg flex justify-between items-center animate-fade-in">
          <span>⚠️ {error}</span>
          <button onClick={() => setError(null)} className="text-red-500 hover:text-red-700 font-bold ml-2">✕</button>
        </div>
      )}
      {successMsg && (
        <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg animate-fade-in">
          💡 {successMsg}
        </div>
      )}

      {/* ── Grid Principal ─────────────────────────────────────────── */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">

        {/* ── Formulario de Registro/Edición (1/3) ─────────────────── */}
        <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm h-fit space-y-4">
          <h2 className="text-lg font-semibold text-slate-700">
            {editingId ? '✏️ Modificar Persona' : '👤 Nueva Persona'}
          </h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            
            {/* Cédula */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Número de Cédula *
              </label>
              <input
                required
                type="text"
                value={cedula}
                onChange={(e) => setCedula(e.target.value)}
                placeholder="Ej. 1012345678"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              />
            </div>

            {/* Nombres */}
            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                  Primer Nombre *
                </label>
                <input
                  required
                  type="text"
                  value={primerNombre}
                  onChange={(e) => setPrimerNombre(e.target.value)}
                  placeholder="Ej. Juan"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                  Segundo Nombre
                </label>
                <input
                  type="text"
                  value={segundoNombre}
                  onChange={(e) => setSegundoNombre(e.target.value)}
                  placeholder="Ej. Carlos"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                />
              </div>
            </div>

            {/* Apellidos */}
            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                  Primer Apellido *
                </label>
                <input
                  required
                  type="text"
                  value={primerApellido}
                  onChange={(e) => setPrimerApellido(e.target.value)}
                  placeholder="Ej. Pérez"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                  Segundo Apellido
                </label>
                <input
                  type="text"
                  value={segundoApellido}
                  onChange={(e) => setSegundoApellido(e.target.value)}
                  placeholder="Ej. Gómez"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                />
              </div>
            </div>

            {/* Tipo / Rol de Persona */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Tipo o Rol de la Persona *
              </label>
              <select
                value={role}
                onChange={(e: any) => setRole(e.target.value)}
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              >
                <option value="ADMIN">Administrador (admin)</option>
                <option value="ENGINEER">Ingeniero Técnico (engineer)</option>
                <option value="CEO_CLIENT">Representante Legal</option>
              </select>
            </div>

            {/* Credenciales de Acceso (Solo en creación) */}
            {!editingId && role !== 'CEO_CLIENT' && (
              <div className="bg-slate-50 p-3 rounded-lg border border-slate-200 space-y-3">
                <p className="text-[10px] text-slate-500 font-bold uppercase tracking-wider">Identidad en Keycloak</p>
                <div>
                  <label className="block text-[10px] font-semibold text-slate-600 uppercase mb-0.5">
                    Nombre de Usuario *
                  </label>
                  <input
                    required
                    type="text"
                    value={nombreUsuario}
                    onChange={(e) => setNombreUsuario(e.target.value)}
                    placeholder="Ej. juan.perez"
                    className="w-full px-3 py-1.5 border border-slate-300 rounded-lg text-xs focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all bg-white"
                  />
                </div>
                <div>
                  <label className="block text-[10px] font-semibold text-slate-600 uppercase mb-0.5">
                    Contraseña *
                  </label>
                  <input
                    required
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Contraseña del usuario"
                    className="w-full px-3 py-1.5 border border-slate-300 rounded-lg text-xs focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all bg-white"
                  />
                </div>
              </div>
            )}

            {/* Correos y Teléfonos dinámicos (Solo en creación) */}
            {!editingId && (
              <div className="space-y-4">
                {/* Correos */}
                <div>
                  <div className="flex items-center justify-between mb-1">
                    <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider">
                      Correos Electrónicos
                    </label>
                    <button
                      type="button"
                      onClick={addEmailField}
                      className="text-xs text-blue-600 hover:text-blue-700 font-semibold"
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
                    <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider">
                      Teléfonos de Contacto
                    </label>
                    <button
                      type="button"
                      onClick={addPhoneField}
                      className="text-xs text-blue-600 hover:text-blue-700 font-semibold"
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
                          placeholder="Ej. 3123000000"
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
              </div>
            )}

            {/* Botones de acción */}
            <div className="flex gap-2 pt-2">
              <button
                type="submit"
                disabled={isLoading}
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg transition-colors disabled:bg-blue-400 disabled:cursor-not-allowed shadow-sm"
              >
                {isLoading ? 'Procesando...' : editingId ? 'Actualizar Persona' : 'Crear Persona'}
              </button>
              {editingId && (
                <button
                  type="button"
                  onClick={resetForm}
                  className="bg-slate-200 hover:bg-slate-300 text-slate-700 font-medium text-sm py-2 px-3 rounded-lg transition-colors"
                >
                  Cancelar
                </button>
              )}
            </div>

          </form>
        </div>

        {/* ── Tabla de Personas Registradas (2/3) ──────────────────── */}
        <div className="lg:col-span-2 bg-white p-5 rounded-xl border border-slate-200 shadow-sm space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 border-b border-slate-100 pb-3">
            <h2 className="text-lg font-semibold text-slate-700">Personas Registradas</h2>
            
            {/* Input de Búsqueda */}
            {persons.length > 0 && (
              <div className="relative w-full sm:w-64">
                <span className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-slate-400 text-xs">
                  🔍
                </span>
                <input
                  type="text"
                  placeholder="Buscar por cédula, nombre o rol..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-8 pr-3 py-1.5 border border-slate-300 rounded-lg text-xs focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 placeholder-slate-400 bg-slate-50/50"
                />
                {searchTerm && (
                  <button 
                    onClick={() => setSearchTerm('')} 
                    className="absolute inset-y-0 right-0 flex items-center pr-2.5 text-slate-400 hover:text-slate-600 font-bold text-xs"
                  >
                    ✕
                  </button>
                )}
              </div>
            )}
          </div>

          {/* Listado */}
          {isLoading && persons.length === 0 ? (
            <p className="text-sm text-slate-500 text-center py-8">Cargando personal del sistema...</p>
          ) : persons.length === 0 ? (
            <div className="py-10 text-center space-y-2">
              <span className="text-4xl">👥</span>
              <p className="text-sm text-slate-500">No hay personas registradas en el sistema.</p>
              <p className="text-xs text-slate-400">Use el formulario de la izquierda para registrar la primera.</p>
            </div>
          ) : filteredPersons.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">
              No se encontraron coincidencias para "{searchTerm}".
            </p>
          ) : (
            <div className="overflow-x-auto rounded-lg border border-slate-100">
              <table className="w-full text-left text-sm text-slate-600">
                <thead className="bg-slate-50 text-xs text-slate-700 uppercase tracking-wider border-b border-slate-100">
                  <tr>
                    <th className="px-4 py-3">Cédula</th>
                    <th className="px-4 py-3">Nombre Completo</th>
                    <th className="px-4 py-3">Rol / Tipo</th>
                    <th className="px-4 py-3 text-right">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {filteredPersons.map((p) => {
                    const fullName = `${p.primerNombre} ${p.segundoNombre || ''} ${p.primerApellido} ${p.segundoApellido || ''}`;
                    return (
                      <tr key={p.identificador} className="hover:bg-slate-50/70 transition-colors group">
                        <td className="px-4 py-3">
                          <span className="font-mono text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded font-semibold">
                            {p.cedula}
                          </span>
                        </td>
                        <td className="px-4 py-3">
                          <div className="font-medium text-slate-800 text-sm">{fullName}</div>
                          {(p.emailPersonList?.length > 0 || p.phonePersonList?.length > 0) && (
                            <div className="text-[10px] text-slate-400 flex flex-wrap gap-x-2 mt-0.5">
                              {p.emailPersonList?.[0] && (
                                <span>✉️ {p.emailPersonList[0].correoPersona}</span>
                              )}
                              {p.phonePersonList?.[0] && (
                                <span>📞 {p.phonePersonList[0].telefonoPersona}</span>
                              )}
                            </div>
                          )}
                        </td>
                        <td className="px-4 py-3">
                          <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full uppercase tracking-wider ${getRoleBadgeClass(p.tipoPersona)}`}>
                            {p.tipoPersona === 'CEO_CLIENT' ? 'REPRESENTANTE LEGAL' : p.tipoPersona === 'ENGINEER' ? 'INGENIERO TÉCNICO' : p.tipoPersona}
                          </span>
                        </td>
                        <td className="px-4 py-3 text-right whitespace-nowrap">
                          <div className="flex justify-end gap-1.5">
                            <button
                              onClick={() => handleStartEdit(p)}
                              className="px-2 py-1 text-blue-600 hover:text-blue-700 hover:bg-blue-50 text-xs font-semibold rounded border border-blue-200 transition-colors"
                            >
                              Editar
                            </button>
                            <button
                              onClick={() => triggerDeleteClick(p.identificador, fullName)}
                              className="px-2 py-1 text-red-600 hover:text-red-700 hover:bg-red-50 text-xs font-semibold rounded border border-red-100 transition-colors"
                            >
                              Eliminar
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}

          {activePersons.length > 0 && (
            <p className="text-xs text-slate-400 text-right mt-2">
              {filteredPersons.length} de {activePersons.length} persona{activePersons.length !== 1 ? 's' : ''}
            </p>
          )}

        </div>
      </div>

      {/* ── Modal de Eliminación de Persona ───────────────────────── */}
      {deleteModal.isOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-sm animate-fade-in">
          <div className="bg-white rounded-xl shadow-xl border border-slate-200 max-w-md w-full p-6 space-y-6">
            <div className="flex items-start gap-4">
              <div className="p-3 bg-red-50 text-red-600 rounded-full shrink-0">
                <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                  <path strokeLinecap="round" strokeLinejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </div>
              <div className="space-y-1">
                <h3 className="text-lg font-bold text-slate-800">¿Dar de baja a esta persona?</h3>
                <p className="text-sm text-slate-500">
                  Está a punto de desactivar del sistema a{' '}
                  <span className="font-semibold text-slate-700">"{deleteModal.personName}"</span>.
                  Esta persona ya no estará disponible para asignaciones y se limitará su visibilidad en el sistema.
                </p>
              </div>
            </div>
            <div className="flex justify-end gap-3 pt-2">
              <button
                type="button"
                onClick={() => setDeleteModal({ isOpen: false, personId: null, personName: '' })}
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
