import React, { useState } from 'react';
import { useTechnicalVerifications } from '../hooks/useTechnicalVerifications';

// Diccionario para mostrar nombres legibles en la lista
const VERIFICATION_TYPES_MAP: Record<string, string> = {
  'VER_TEC_INIC': 'Verificación Inicial Técnica',
  'PROC_MANT': 'Protocolo de Mantenimiento'
};

interface DeleteModalState {
  isOpen: boolean;
  id: string | null;
  description: string;
}

export const TechnicalVerificationManager: React.FC = () => {
  const { 
    verifications, 
    isLoading, 
    error, 
    setError, 
    createVerification, 
    updateVerification, 
    deleteVerification 
  } = useTechnicalVerifications();

  // Estados del Formulario
  const [description, setDescription] = useState('');
  const [verificationType, setVerificationType] = useState('');
  const [editingId, setEditingId] = useState<string | null>(null);
  const [formSubmitted, setFormSubmitted] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');
  const [searchTerm, setSearchTerm] = useState('');

  // Estado del Modal de Eliminación
  const [deleteModal, setDeleteModal] = useState<DeleteModalState>({
    isOpen: false,
    id: null,
    description: ''
  });

  const resetForm = () => {
    setDescription('');
    setVerificationType('');
    setEditingId(null);
    setFormSubmitted(false);
  };

  const showSuccess = (msg: string) => {
    setSuccessMsg(msg);
    setTimeout(() => setSuccessMsg(''), 4000);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFormSubmitted(true);

    if (!description.trim() || !verificationType.trim()) {
      setError('Por favor, complete todos los campos obligatorios marcados en rojo.');
      return;
    }

    const payload = {
      description: description.trim(),
      verificationType: verificationType.trim()
    };

    try {
      if (editingId) {
        await updateVerification(editingId, payload);
        showSuccess('¡Verificación técnica actualizada con éxito!');
      } else {
        await createVerification(payload);
        showSuccess('¡Verificación técnica registrada correctamente!');
      }
      resetForm();
    } catch (err) {}
  };

  const handleStartEdit = (id: string) => {
    const target = verifications.find((v) => v.id === id);
    if (!target) return;
    setEditingId(target.id);
    setDescription(target.description);
    setVerificationType(target.verificationType);
  };

  const handleDeleteTrigger = (id: string, desc: string) => {
    setDeleteModal({ isOpen: true, id, description: desc });
  };

  const handleConfirmDelete = async () => {
    if (!deleteModal.id) return;
    const targetId = deleteModal.id;
    setDeleteModal({ isOpen: false, id: null, description: '' });
    try {
      await deleteVerification(targetId);
      showSuccess('Registro eliminado con éxito.');
    } catch (err) {}
  };

  const filteredVerifications = Array.isArray(verifications)
    ? verifications.filter((v) => 
        v.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
        v.verificationType.toLowerCase().includes(searchTerm.toLowerCase())
      )
    : [];

  return (
    <div className="p-4 md:p-6 max-w-7xl mx-auto space-y-6 relative">
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Verificaciones Técnicas</h1>
        <p className="text-sm text-slate-500">Configuración de criterios de calibración y pruebas para tipos de equipos.</p>
      </div>

      {error && (
        <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg flex justify-between items-center">
          <span>⚠️ {error}</span>
          <button onClick={() => setError(null)} className="text-red-500 font-bold ml-2 hover:text-red-800">✕</button>
        </div>
      )}

      {successMsg && (
        <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg">
          💡 {successMsg}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* FORMULARIO */}
        <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm h-fit space-y-4">
          <h2 className="text-lg font-semibold text-slate-700">
            {editingId ? '📝 Modificar Criterio' : 'Nuevo Criterio Técnico'}
          </h2>

          <form onSubmit={handleSubmit} noValidate className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Tipo de Verificación</label>
              <select
                  value={verificationType}
                  onChange={(e) => setVerificationType(e.target.value)}
                  className={`w-full px-3 py-2 border rounded-lg text-sm bg-white focus:outline-none focus:ring-2 ${
                  formSubmitted && !verificationType.trim()
                      ? 'border-red-400 focus:ring-red-200 bg-red-50/30'
                      : 'border-slate-300 focus:ring-blue-500'
                  }`}
              >
                  <option value="">-- Seleccione una opción --</option>
                  <option value="VER_TEC_INIC">Verificación Inicial Técnica</option>
                  <option value="PROC_MANT">Protocolo de Mantenimiento</option>
              </select>
              {formSubmitted && !verificationType.trim() && (
                  <p className="text-[11px] text-red-500 font-medium mt-1">⚠ El tipo es requerido.</p>
              )}
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Descripción del Criterio</label>
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                rows={3}
                placeholder="Ej. Verificación de calibración de electrodos y rango de ondas..."
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !description.trim()
                    ? 'border-red-400 focus:ring-red-200 bg-red-50/30'
                    : 'border-slate-300 focus:ring-blue-500'
                }`}
              />
              {formSubmitted && !description.trim() && (
                <p className="text-[11px] text-red-500 font-medium mt-1">⚠ La descripción es obligatoria.</p>
              )}
            </div>

            <div className="flex gap-2 pt-2">
              <button
                type="submit"
                disabled={isLoading}
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg transition-colors disabled:bg-blue-400"
              >
                {editingId ? 'Actualizar Criterio' : 'Guardar Criterio'}
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

        {/* LISTADO */}
        <div className="lg:col-span-2 bg-white p-5 rounded-xl border border-slate-200 shadow-sm space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
            <h2 className="text-lg font-semibold text-slate-700">Criterios Registrados</h2>
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Buscar criterio o tipo..."
              className="px-3 py-1.5 border border-slate-300 rounded-lg text-sm bg-slate-50/50 w-full sm:w-64 focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          {isLoading && filteredVerifications.length === 0 ? (
            <p className="text-sm text-slate-500 text-center py-8">Sincronizando criterios técnicos...</p>
          ) : filteredVerifications.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">No se encontraron verificaciones técnicas registradas.</p>
          ) : (
            <div className="grid grid-cols-1 gap-3">
              {filteredVerifications.map((v) => (
                <div key={v.id} className="p-4 border border-slate-100 rounded-xl bg-slate-50/30 hover:border-slate-200 transition-all space-y-2">
                  <div className="flex justify-between items-start">
                    <div>
                      <span className="px-2 py-0.5 bg-blue-100 text-blue-800 font-semibold text-[11px] rounded-full uppercase tracking-wide">
                        {VERIFICATION_TYPES_MAP[v.verificationType] || v.verificationType}
                      </span>
                    </div>
                    <div className="flex gap-2">
                      <button
                        onClick={() => handleStartEdit(v.id)}
                        className="text-xs font-medium text-blue-600 hover:underline"
                      >
                        Editar
                      </button>
                      <button
                        onClick={() => handleDeleteTrigger(v.id, v.description)}
                        className="text-xs font-medium text-red-600 hover:underline"
                      >
                        Eliminar
                      </button>
                    </div>
                  </div>
                  <p className="text-sm text-slate-700 leading-relaxed">{v.description}</p>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* MODAL ELIMINAR */}
      {deleteModal.isOpen && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm flex justify-center items-center z-50 p-4">
          <div className="bg-white max-w-md w-full rounded-xl shadow-xl border border-slate-200 p-6 space-y-4">
            <div className="flex items-center space-x-3 text-red-600">
              <h3 className="text-lg font-bold text-slate-800">¿Confirmar Eliminación?</h3>
            </div>
            <p className="text-sm text-slate-600 leading-relaxed">
              ¿Está seguro de eliminar permanentemente el criterio: 
              <span className="text-slate-900 block font-medium italic mt-1">"{deleteModal.description}"</span>?
            </p>
            <div className="flex space-x-3 justify-end pt-2">
              <button
                type="button"
                onClick={() => setDeleteModal({ isOpen: false, id: null, description: '' })}
                className="px-4 py-2 bg-slate-100 text-slate-700 text-sm font-semibold rounded-lg hover:bg-slate-200"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={handleConfirmDelete}
                className="px-4 py-2 bg-red-600 text-white text-sm font-semibold rounded-lg hover:bg-red-700"
              >
                Eliminar Registro
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};