import React, { useState } from 'react';
import { useModels } from '../hooks/useModels';
import { useManufacturers } from '../../manufacturers/hooks/useManufacturers';
import { useEquipments } from '../hooks/useEquipments'; 
import type { EquipmentModel } from '../types/model.types';

export const ModelManager: React.FC = () => {
  const { models, isLoading: isModelsLoading, error, createModel, updateModel, deleteModel, setError } = useModels();
  const { manufacturers } = useManufacturers();
  const { equipments, isLoading: isEquipmentsLoading } = useEquipments();

  // Estados del Formulario
  const [invima, setInvima] = useState('');
  const [manufacturerId, setManufacturerId] = useState('');
  const [equipmentId, setEquipmentId] = useState(''); 
  const [editingId, setEditingId] = useState<string | null>(null);
  const [formSubmitted, setFormSubmitted] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');

  // Estado de Búsqueda
  const [searchTerm, setSearchTerm] = useState('');

  // Estado del Modal de Eliminación
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [modelToDelete, setModelToDelete] = useState<EquipmentModel | null>(null);

  const resetForm = () => {
    setInvima('');
    setManufacturerId('');
    setEquipmentId('');
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

    if (!invima.trim() || !manufacturerId || !equipmentId) {
      setError('Por favor, complete todos los campos obligatorios del modelo.');
      return;
    }

    const payload = {
      invima: invima.trim(),
      manufacturerId,
      equipmentId, 
    };

    try {
      if (editingId) {
        await updateModel(editingId, payload);
        showSuccess('¡Modelo actualizado exitosamente!');
      } else {
        await createModel(payload);
        showSuccess('¡Modelo registrado correctamente en el inventario!');
      }
      resetForm();
    } catch (err) {}
  };

  const handleStartEdit = (model: EquipmentModel) => {
    setEditingId(model.id);
    setInvima(model.invima);
    setManufacturerId(model.manufacturerId);
    setEquipmentId(model.equipmentId);
  };

  const triggerDeleteConfirm = (model: EquipmentModel) => {
    setModelToDelete(model);
    setIsDeleteModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!modelToDelete) return;
    try {
      await deleteModel(modelToDelete.id);
      showSuccess('Modelo biomédico removido con éxito.');
    } catch (err) {}
    finally {
      setIsDeleteModalOpen(false);
      setModelToDelete(null);
    }
  };

  // Filtrado reactivo multivariable
  const filteredModels = models.filter((model) => {
    const term = searchTerm.toLowerCase();
    
    const matchingMfg = manufacturers.find(m => m.id === model.manufacturerId);
    const mfgLabel = model.manufacturerResponse?.name || matchingMfg?.name || '';

    const matchingEq = equipments.find(e => e.id === model.equipmentId);
    
    const eqBrand = (matchingEq as any)?.brandResponse?.name || '';
    const eqTypeName = (matchingEq as any)?.equipmentTypeResponse?.equipmentTypeName || '';
    const eqLabel = model.equipmentResponse?.name || (eqBrand && eqTypeName ? `${eqBrand}, ${eqTypeName}` : '') || '';
    const eqSerial = (matchingEq as any)?.serial || ''; 

    return (
      model.invima.toLowerCase().includes(term) ||
      mfgLabel.toLowerCase().includes(term) ||
      eqLabel.toLowerCase().includes(term) ||
      eqSerial.toLowerCase().includes(term) ||
      model.id.toLowerCase().includes(term)
    );
  });

  return (
    <div className="p-4 md:p-6 max-w-7xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Gestión de Modelos</h1>
        <p className="text-sm text-slate-500">Mapeo de referencias de fábrica y homologaciones de registros INVIMA vigentes.</p>
      </div>

      {error && (
        <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg flex justify-between items-center">
          <span>⚠️ {error}</span>
          <button onClick={() => setError(null)} className="text-red-500 font-bold ml-2">✕</button>
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
            {editingId ? 'Modificar Modelo' : 'Nuevo Modelo'}
          </h2>

          <form onSubmit={handleSubmit} noValidate className="space-y-4">
            {/* REGISTRO INVIMA */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Registro / Código INVIMA
              </label>
              <input
                type="text"
                placeholder="Ej: INVIMA 2018DM-001234"
                value={invima}
                onChange={(e) => setInvima(e.target.value)}
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !invima.trim() ? 'border-red-400 bg-red-50/30' : 'border-slate-300'
                }`}
              />
            </div>

            {/* SELECT FABRICANTE */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Fabricante Responsable
              </label>
              <select
                value={manufacturerId}
                onChange={(e) => setManufacturerId(e.target.value)}
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !manufacturerId ? 'border-red-400 bg-red-50/30' : 'border-slate-300'
                }`}
              >
                <option value="">-- Seleccione Fabricante --</option>
                {manufacturers.map((m) => (
                  <option key={m.id} value={m.id}>
                    {m.name}
                  </option>
                ))}
              </select>
            </div>

            {/* SELECT EQUIPO REAL */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Equipo Real Asociado
              </label>
              <select
                value={equipmentId}
                onChange={(e) => setEquipmentId(e.target.value)}
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !equipmentId ? 'border-red-400 bg-red-50/30' : 'border-slate-300'
                }`}
              >
                <option value="">-- Seleccione Equipo --</option>
                {equipments.map((e) => {
                  const brandName = (e as any).brandResponse?.name;
                  const typeName = (e as any).equipmentTypeResponse?.equipmentTypeName;
                  const displayLabel = brandName && typeName 
                    ? `${brandName}, ${typeName}` 
                    : `Equipo sin nombre completo (ID: ${e.id.substring(0,8)}...)`;

                  return (
                    <option key={e.id} value={e.id}>
                      {displayLabel}
                    </option>
                  );
                })}
              </select>
              {isEquipmentsLoading && <p className="text-[10px] text-blue-500 mt-1">Cargando inventario de equipos...</p>}
            </div>

            <div className="flex gap-2 pt-2">
              <button
                type="submit"
                disabled={isModelsLoading}
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg disabled:bg-blue-400"
              >
                {editingId ? 'Actualizar Modelo' : 'Guardar Modelo'}
              </button>
              {editingId && (
                <button
                  type="button"
                  onClick={resetForm}
                  className="bg-slate-200 hover:bg-slate-300 text-slate-700 font-medium text-sm py-2 px-3 rounded-lg"
                >
                  Cancelar
                </button>
              )}
            </div>
          </form>
        </div>

        {/* LISTADO CON BARRA DE BÚSQUEDA */}
        <div className="lg:col-span-2 bg-white p-5 rounded-xl border border-slate-200 shadow-sm space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 border-b border-slate-100 pb-3">
            <h2 className="text-lg font-semibold text-slate-700">Modelos Registrados</h2>
            
            <div className="relative w-full sm:w-64">
              <span className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-slate-400 text-xs">
                🔍
              </span>
              <input
                type="text"
                placeholder="Buscar por INVIMA, marca o equipo..."
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
          </div>

          {isModelsLoading && models.length === 0 ? (
            <p className="text-sm text-slate-500 text-center py-8">Cargando catálogo de modelos...</p>
          ) : models.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">No hay modelos registrados en el sistema biomédico.</p>
          ) : filteredModels.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">
              No se hallaron coincidencias para "{searchTerm}".
            </p>
          ) : (
            <div className="grid grid-cols-1 gap-3">
              {filteredModels.map((model) => {
                const matchingMfg = manufacturers.find(m => m.id === model.manufacturerId);
                const mfgName = model.manufacturerResponse?.name || matchingMfg?.name || `ID: ${model.manufacturerId}`;

                const matchingEq = equipments.find(e => e.id === model.equipmentId);
                
                const brandName = (matchingEq as any)?.brandResponse?.name;
                const typeName = (matchingEq as any)?.equipmentTypeResponse?.equipmentTypeName;
                const eqName = model.equipmentResponse?.name || (brandName && typeName ? `${brandName}, ${typeName}` : '') || `ID: ${model.equipmentId}`;

                return (
                  <div key={model.id} className="p-4 border border-slate-100 rounded-xl bg-slate-50/30 flex justify-between items-center gap-4">
                    <div className="space-y-1">
                      <div className="flex items-center gap-2">
                        <span className="text-xs bg-blue-50 text-blue-700 font-semibold px-2 py-0.5 rounded border border-blue-100">
                          {model.invima}
                        </span>
                      </div>
                      <p className="text-sm text-slate-700 font-medium">
                        Equipo Vincular: <span className="text-slate-900 font-semibold">{eqName}</span>
                      </p>
                      <p className="text-xs text-slate-500">
                        Fabricante: <span className="text-slate-700 bg-slate-100 px-1.5 py-0.5 rounded text-[11px] font-medium">{mfgName}</span>
                      </p>
                      <p className="text-[10px] text-slate-400 font-mono">UUID: {model.id}</p>
                    </div>
                    
                    <div className="flex gap-1 shrink-0">
                      <button
                        onClick={() => handleStartEdit(model)}
                        className="p-1 px-2.5 text-xs font-semibold text-blue-600 hover:bg-blue-50 rounded border border-blue-100 transition-colors"
                      >
                        Editar
                      </button>
                      <button
                        onClick={() => triggerDeleteConfirm(model)}
                        className="p-1 px-2.5 text-xs font-semibold text-red-600 hover:bg-red-50 rounded border border-red-100 transition-colors"
                      >
                        Eliminar
                      </button>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>

      {/* MODAL DE CONFIRMACIÓN */}
      {isDeleteModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm animate-fade-in">
          <div className="bg-white rounded-2xl border border-slate-200 shadow-xl max-w-md w-full overflow-hidden p-6 space-y-4 relative animate-scale-up">
            <div className="flex items-center gap-3 border-b border-slate-100 pb-3">
              <div className="w-10 h-10 rounded-full bg-red-50 flex items-center justify-center text-red-600 text-xl shrink-0">
                ⚠️
              </div>
              <div>
                <h3 className="text-lg font-bold text-slate-800">¿Remover Ficha de Modelo?</h3>
                <p className="text-xs text-slate-500">Esta acción desvinculará la homologación INVIMA.</p>
              </div>
            </div>

            <div className="bg-slate-50 p-3 rounded-lg border border-slate-100 text-sm text-slate-700 space-y-1">
              <p><span className="font-semibold text-slate-500 text-xs uppercase block">Registro INVIMA:</span> {modelToDelete?.invima}</p>
            </div>

            <p className="text-xs text-slate-500 leading-relaxed">
              El borrado fallará automáticamente si existen órdenes de trabajo activas o equipos físicos serializados vinculados a este modelo específico.
            </p>

            <div className="flex gap-2 justify-end pt-2">
              <button
                type="button"
                onClick={() => {
                  setIsDeleteModalOpen(false);
                  setModelToDelete(null);
                }}
                className="px-4 py-2 border border-slate-200 text-slate-700 font-medium text-sm rounded-xl hover:bg-slate-50 transition-colors"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={handleConfirmDelete}
                className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white font-medium text-sm rounded-xl shadow-sm transition-colors"
              >
                Sí, eliminar modelo
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};