import React, { useState, useEffect } from 'react';
import { useEquipments } from '../hooks/useEquipments';
import { getAuthHeaders } from '../services/equipment-type.service';
import { useEquipmentTypes } from '../hooks/useEquipmentTypes';

export const EquipmentManager: React.FC = () => {
  const { equipments, isLoading, error, createEquipment, updateEquipment, deleteEquipment, setError } = useEquipments();
  const { equipmentTypes } = useEquipmentTypes();

  // Estado maestro local para las marcas
  const [brands, setBrands] = useState<any[]>([]);
  
  // Estados del Formulario
  const [equipmentTypeId, setEquipmentTypeId] = useState('');
  const [brandId, setBrandId] = useState('');
  const [editingId, setEditingId] = useState<string | null>(null);
  const [formSubmitted, setFormSubmitted] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');

  // Estados para el modal de eliminación
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [equipmentToDelete, setEquipmentToDelete] = useState<any | null>(null);

  // Cargar marcas del sistema
  useEffect(() => {
    fetch('http://localhost:8100/v1/api/brands', { 
      method: 'GET',
      headers: getAuthHeaders(null)
    })
      .then(res => res.json())
      .then(data => setBrands(data || []))
      .catch(err => console.error("Error cargando marcas:", err));
  }, []);

  const resetForm = () => {
    setEquipmentTypeId('');
    setBrandId('');
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

    if (!equipmentTypeId || !brandId) {
      setError('Por favor, seleccione todos los campos obligatorios.');
      return;
    }

    const payload = { equipmentTypeId, brandId };

    try {
      if (editingId) {
        await updateEquipment(editingId, payload);
        showSuccess('¡Equipo actualizado exitosamente!');
      } else {
        await createEquipment(payload);
        showSuccess('¡Equipo registrado correctamente!');
      }
      resetForm();
    } catch (err) {}
  };

  const handleStartEdit = (id: string) => {
    const target = equipments.find((e: any) => e.id === id);
    if (!target) return;
    setEditingId(target.id);
    setEquipmentTypeId(target.equipmentTypeId);
    setBrandId(target.brandId);
  };

  const triggerDeleteConfirm = (equip: any) => {
    setEquipmentToDelete(equip);
    setIsDeleteModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!equipmentToDelete) return;
    try {
      await deleteEquipment(equipmentToDelete.id);
      showSuccess('Equipo removido con éxito.');
    } catch (err) {}
    finally {
      setIsDeleteModalOpen(false);
      setEquipmentToDelete(null);
    }
  };

  return (
    <div className="p-4 md:p-6 max-w-7xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Inventario de Equipos</h1>
        <p className="text-sm text-slate-500">Asociación y control operativo de dispositivos biomédicos.</p>
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
            {editingId ? 'Modificar Equipo' : 'Nuevo Equipo'}
          </h2>

          <form onSubmit={handleSubmit} noValidate className="space-y-4">
            {/* SELECT TIPO DE EQUIPO */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Tipo de Equipo</label>
              <select
                value={equipmentTypeId}
                onChange={(e) => setEquipmentTypeId(e.target.value)}
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !equipmentTypeId ? 'border-red-400 bg-red-50/30' : 'border-slate-300'
                }`}
              >
                <option value="">-- Seleccione --</option>
                {equipmentTypes.map(type => (
                  <option key={type.id} value={type.id}>{type.equipmentTypeName}</option>
                ))}
              </select>
            </div>

            {/* SELECT MARCA */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Marca</label>
              <select
                value={brandId}
                onChange={(e) => setBrandId(e.target.value)}
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !brandId ? 'border-red-400 bg-red-50/30' : 'border-slate-300'
                }`}
              >
                <option value="">-- Seleccione --</option>
                {brands.map(brand => (
                  <option key={brand.id} value={brand.id}>{brand.brandName || brand.name || 'Marca s/n'}</option>
                ))}
              </select>
            </div>

            <div className="flex gap-2 pt-2">
              <button
                type="submit"
                disabled={isLoading}
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg disabled:bg-blue-400"
              >
                {editingId ? 'Actualizar Equipo' : 'Guardar Equipo'}
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

        {/* LISTADO */}
        <div className="lg:col-span-2 bg-white p-5 rounded-xl border border-slate-200 shadow-sm space-y-4">
          <h2 className="text-lg font-semibold text-slate-700">Equipos Registrados</h2>

          {isLoading && equipments.length === 0 ? (
            <p className="text-sm text-slate-500 text-center py-8">Cargando inventario...</p>
          ) : equipments.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">No hay equipos registrados en el sistema.</p>
          ) : (
            <div className="grid grid-cols-1 gap-3">
              {equipments.map((equip: any) => (
                <div key={equip.id} className="p-4 border border-slate-100 rounded-xl bg-slate-50/30 flex flex-col sm:flex-row justify-between sm:items-center gap-4">
                  <div className="space-y-1.5 flex-1">
                    <div className="flex flex-wrap items-baseline gap-2">
                      <h3 className="font-bold text-slate-800 text-base">
                        {equip.equipmentTypeResponse?.equipmentTypeName || 'Tipo no especificado'}
                      </h3>
                      <span className="px-2 py-0.5 bg-blue-50 text-blue-700 text-xs font-semibold rounded-md border border-blue-100">
                        {equip.brandResponse?.name || 'Marca no especificada'}
                      </span>
                    </div>

                    {equip.equipmentTypeResponse?.technicalDefinition && (
                      <p className="text-xs text-slate-600">
                        <span className="font-medium text-slate-500">Definición técnica:</span> {equip.equipmentTypeResponse.technicalDefinition}
                      </p>
                    )}
                    
                    {equip.equipmentTypeResponse?.careRecommendations && (
                      <p className="text-xs text-slate-600">
                        <span className="font-medium text-slate-500">Cuidados:</span> {equip.equipmentTypeResponse.careRecommendations}
                      </p>
                    )}

                    <p className="text-[10px] text-slate-400 font-mono pt-1">ID Equipo: {equip.id}</p>
                  </div>
                  
                  <div className="flex gap-1 self-end sm:self-center">
                    <button
                      onClick={() => handleStartEdit(equip.id)}
                      className="p-1 px-2.5 text-xs font-semibold text-blue-600 hover:bg-blue-50 rounded border border-blue-200 transition-colors"
                    >
                      Editar
                    </button>
                    <button
                      onClick={() => triggerDeleteConfirm(equip)}
                      className="p-1 px-2.5 text-xs font-semibold text-red-600 hover:bg-red-50 rounded border border-red-100 transition-colors"
                    >
                      Eliminar
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
      {/* MODAL DE CONFIRMACIÓN DE ELIMINACIÓN*/}
      {isDeleteModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm animate-fade-in">
          <div className="bg-white rounded-2xl border border-slate-200 shadow-xl max-w-md w-full overflow-hidden p-6 space-y-4 relative animate-scale-up">
            
            {/* Encabezado con Icono de Advertencia */}
            <div className="flex items-center gap-3 border-b border-slate-100 pb-3">
              <div className="w-10 h-10 rounded-full bg-red-50 flex items-center justify-center text-red-600 text-xl shrink-0">
                ⚠️
              </div>
              <div>
                <h3 className="text-lg font-bold text-slate-800">¿Confirmar Eliminación?</h3>
                <p className="text-xs text-slate-500">Esta acción no se puede deshacer de forma directa.</p>
              </div>
            </div>

            {/* Detalles del objeto a borrar */}
            <div className="bg-slate-50 p-3 rounded-lg border border-slate-100 text-sm text-slate-700 space-y-1">
              <p>
                <span className="font-semibold text-slate-500 text-xs uppercase tracking-wider block">Equipo seleccionado:</span> 
                {equipmentToDelete?.equipmentTypeResponse?.equipmentTypeName || 'Tipo de Equipo'}
              </p>
              <p className="text-xs text-slate-600">
                <span className="font-medium">Marca:</span> {equipmentToDelete?.brandResponse?.name || 's/n'}
              </p>
            </div>

            <p className="text-xs text-slate-500 leading-relaxed">
              Al confirmar, el registro se dará de baja en el inventario biomédico y se desvinculará de los flujos de trabajo actuales.
            </p>

            {/* Acciones */}
            <div className="flex gap-2 justify-end pt-2">
              <button
                type="button"
                onClick={() => {
                  setIsDeleteModalOpen(false);
                  setEquipmentToDelete(null);
                }}
                className="px-4 py-2 border border-slate-200 text-slate-700 font-medium text-sm rounded-xl hover:bg-slate-50 transition-colors"
              >
                No, cancelar
              </button>
              <button
                type="button"
                onClick={handleConfirmDelete}
                className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white font-medium text-sm rounded-xl shadow-sm transition-colors"
              >
                Sí, eliminar equipo
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};