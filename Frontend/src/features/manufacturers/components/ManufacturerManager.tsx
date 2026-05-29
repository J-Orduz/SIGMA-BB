import React, { useState } from 'react';
import { useManufacturers } from '../hooks/useManufacturers';
import { useCountries } from '../../locations/hooks/useCountries';
import type { Manufacturer } from '../types/manufacturer.types';

export const ManufacturerManager: React.FC = () => {
  const { manufacturers, isLoading: isManuLoading, error, createManufacturer, updateManufacturer, deleteManufacturer, setError } = useManufacturers();
  const { countries, isLoading: isCountriesLoading } = useCountries();

  // Estados del Formulario
  const [name, setName] = useState('');
  const [countryId, setCountryId] = useState('');
  const [editingId, setEditingId] = useState<string | null>(null);
  const [formSubmitted, setFormSubmitted] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');

  // Estado para la barra de búsqueda 🔍
  const [searchTerm, setSearchTerm] = useState('');

  // Estados para el Modal de Confirmación
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [manufacturerToDelete, setManufacturerToDelete] = useState<Manufacturer | null>(null);

  const resetForm = () => {
    setName('');
    setCountryId('');
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

    if (!name.trim() || !countryId) {
      setError('Por favor, complete todos los campos obligatorios.');
      return;
    }

    const payload = { name: name.trim(), countryId };

    try {
      if (editingId) {
        await updateManufacturer(editingId, payload);
        showSuccess('¡Fabricante actualizado exitosamente!');
      } else {
        await createManufacturer(payload);
        showSuccess('¡Fabricante registrado correctamente!');
      }
      resetForm();
    } catch (err) {}
  };

  const handleStartEdit = (manu: Manufacturer) => {
    setEditingId(manu.id);
    setName(manu.name);
    setCountryId(manu.countryId);
  };

  const triggerDeleteConfirm = (manu: Manufacturer) => {
    setManufacturerToDelete(manu);
    setIsDeleteModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!manufacturerToDelete) return;
    try {
      await deleteManufacturer(manufacturerToDelete.id);
      showSuccess('Fabricante removido con éxito.');
    } catch (err) {}
    finally {
      setIsDeleteModalOpen(false);
      setManufacturerToDelete(null);
    }
  };

  // Filtrado reactivo multivariable (Por nombre de fábrica o procedencia territorial)
  const filteredManufacturers = manufacturers.filter((manu) => {
    const term = searchTerm.toLowerCase();
    
    // Conseguir etiqueta del país para contrastar la búsqueda también por origen
    const matchingCountry = countries.find(c => c.id === manu.countryId);
    const countryLabel = manu.countryResponse?.name || matchingCountry?.name || manu.countryId;

    return (
      manu.name.toLowerCase().includes(term) ||
      countryLabel.toLowerCase().includes(term) ||
      manu.countryId.toLowerCase().includes(term)
    );
  });

  return (
    <div className="p-4 md:p-6 max-w-7xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Gestión de Fabricantes</h1>
        <p className="text-sm text-slate-500">Administración de fábricas matrices proveedoras de equipamiento biomédico.</p>
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
            {editingId ? 'Modificar Fabricante' : 'Nuevo Fabricante'}
          </h2>

          <form onSubmit={handleSubmit} noValidate className="space-y-4">
            {/* NOMBRE DEL FABRICANTE */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Nombre del Fabricante
              </label>
              <input
                type="text"
                placeholder="Ej: GE Healthcare"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !name.trim() ? 'border-red-400 bg-red-50/30' : 'border-slate-300'
                }`}
              />
            </div>

            {/* SELECT PAÍS DE ORIGEN */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                País de Origen
              </label>
              <select
                value={countryId}
                onChange={(e) => setCountryId(e.target.value)}
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !countryId ? 'border-red-400 bg-red-50/30' : 'border-slate-300'
                }`}
              >
                <option value="">-- Seleccione un País --</option>
                {countries.map((c) => (
                  <option key={c.id} value={c.id}>
                    {c.name} ({c.id})
                  </option>
                ))}
              </select>
              {isCountriesLoading && <p className="text-[10px] text-blue-500 mt-1">Cargando países territoriales...</p>}
            </div>

            <div className="flex gap-2 pt-2">
              <button
                type="submit"
                disabled={isManuLoading}
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg disabled:bg-blue-400"
              >
                {editingId ? 'Actualizar Fabricante' : 'Guardar Fabricante'}
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

        {/* LISTADO CON BUSCADOR INTEGRADO */}
        <div className="lg:col-span-2 bg-white p-5 rounded-xl border border-slate-200 shadow-sm space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 border-b border-slate-100 pb-3">
            <h2 className="text-lg font-semibold text-slate-700">Fabricantes Autorizados</h2>
            
            {/* Input de Búsqueda Estilizado */}
            <div className="relative w-full sm:w-64">
              <span className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-slate-400 text-xs">
                🔍
              </span>
              <input
                type="text"
                placeholder="Buscar por nombre o procedencia..."
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

          {isManuLoading && manufacturers.length === 0 ? (
            <p className="text-sm text-slate-500 text-center py-8">Cargando inventario de fabricantes...</p>
          ) : manufacturers.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">No hay fabricantes guardados.</p>
          ) : filteredManufacturers.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">
              No se encontraron coincidencias para "{searchTerm}".
            </p>
          ) : (
            <div className="grid grid-cols-1 gap-3">
              {filteredManufacturers.map((manu) => {
                const matchingCountry = countries.find(c => c.id === manu.countryId);
                const countryLabel = manu.countryResponse?.name || matchingCountry?.name || manu.countryId;

                return (
                  <div key={manu.id} className="p-4 border border-slate-100 rounded-xl bg-slate-50/30 flex justify-between items-center gap-4">
                    <div>
                      <h3 className="font-bold text-slate-800 text-base">{manu.name}</h3>
                      <p className="text-xs text-slate-500 font-medium mt-0.5">
                        Procedencia: <span className="text-slate-700 bg-slate-100 px-1.5 py-0.5 rounded font-mono text-[11px]">{countryLabel}</span>
                      </p>
                      <p className="text-[10px] text-slate-400 font-mono mt-1">ID Fabricante: {manu.id}</p>
                    </div>
                    
                    <div className="flex gap-1 shrink-0">
                      <button
                        onClick={() => handleStartEdit(manu)}
                        className="p-1 px-2.5 text-xs font-semibold text-blue-600 hover:bg-blue-50 rounded border border-blue-100 transition-colors"
                      >
                        Editar
                      </button>
                      <button
                        onClick={() => triggerDeleteConfirm(manu)}
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

      {/* MODAL DE ELIMINACIÓN DE FABRICANTE */}
      {isDeleteModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm animate-fade-in">
          <div className="bg-white rounded-2xl border border-slate-200 shadow-xl max-w-md w-full overflow-hidden p-6 space-y-4 relative animate-scale-up">
            <div className="flex items-center gap-3 border-b border-slate-100 pb-3">
              <div className="w-10 h-10 rounded-full bg-red-50 flex items-center justify-center text-red-600 text-xl shrink-0">
                ⚙️
              </div>
              <div>
                <h3 className="text-lg font-bold text-slate-800">¿Remover Fabricante?</h3>
                <p className="text-xs text-slate-500">Se eliminará la casa de manufactura seleccionada.</p>
              </div>
            </div>

            <div className="bg-slate-50 p-3 rounded-lg border border-slate-100 text-sm text-slate-700">
              <p>
                <span className="font-semibold text-slate-500 text-xs uppercase tracking-wider block">Fabricante:</span> 
                {manufacturerToDelete?.name}
              </p>
            </div>

            <p className="text-xs text-slate-500 leading-relaxed">
              Considere que si existen modelos de equipos o marcas ligadas operativamente a este fabricante, el servidor denegará el borrado para preservar el registro del historial biomédico.
            </p>

            <div className="flex gap-2 justify-end pt-2">
              <button
                type="button"
                onClick={() => {
                  setIsDeleteModalOpen(false);
                  setManufacturerToDelete(null);
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
                Sí, eliminar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};