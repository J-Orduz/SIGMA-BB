import React, { useState } from 'react';
import { useCountries } from '../hooks/useCountries';
import type { Country } from '../types/country.types';

export const CountryManager: React.FC = () => {
  const { countries, isLoading, error, createCountry, updateCountry, deleteCountry, setError } = useCountries();

  // Estados del Formulario
  const [countryId, setCountryId] = useState(''); // Código manual (COL, ARG, etc.)
  const [countryName, setCountryName] = useState('');
  const [editingId, setEditingId] = useState<string | null>(null);
  const [formSubmitted, setFormSubmitted] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');

  // Estado para la barra de búsqueda
  const [searchTerm, setSearchTerm] = useState('');

  // Estados para el Modal de Confirmación
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [countryToDelete, setCountryToDelete] = useState<Country | null>(null);

  const resetForm = () => {
    setCountryId('');
    setCountryName('');
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

    const cleanId = countryId.trim().toUpperCase();
    const cleanName = countryName.trim();

    if (!cleanId || !cleanName) {
      setError('Por favor, rellene todos los campos obligatorios.');
      return;
    }

    if (cleanId.length < 2) {
      setError('El código del país debe tener al menos 2 o 3 caracteres (Ej: COL).');
      return;
    }

    const payload = { id: cleanId, name: cleanName };

    try {
      if (editingId) {
        await updateCountry(editingId, payload);
        showSuccess('¡País actualizado exitosamente!');
      } else {
        await createCountry(payload);
        showSuccess('¡País registrado correctamente!');
      }
      resetForm();
    } catch (err) {}
  };

  const handleStartEdit = (country: Country) => {
    setEditingId(country.id);
    setCountryId(country.id); 
    setCountryName(country.name);
  };

  const triggerDeleteConfirm = (country: Country) => {
    setCountryToDelete(country);
    setIsDeleteModalOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!countryToDelete) return;
    try {
      await deleteCountry(countryToDelete.id);
      showSuccess('País removido del sistema con éxito.');
    } catch (err) {}
    finally {
      setIsDeleteModalOpen(false);
      setCountryToDelete(null);
    }
  };

  // Filtrado de países en tiempo real (por nombre o por ID/Código)
  const filteredCountries = countries.filter((country) => {
    const term = searchTerm.toLowerCase();
    return (
      country.name.toLowerCase().includes(term) ||
      country.id.toLowerCase().includes(term)
    );
  });

  return (
    <div className="p-4 md:p-6 max-w-7xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Gestión de Países</h1>
        <p className="text-sm text-slate-500">Administración de locaciones globales y mapeo territorial.</p>
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
            {editingId ? 'Modificar País' : 'Nuevo País'}
          </h2>

          <form onSubmit={handleSubmit} noValidate className="space-y-4">
            {/* CÓDIGO ID DEL PAÍS */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Código del País (ID)
              </label>
              <input
                type="text"
                placeholder="Ej: COL"
                value={countryId}
                disabled={!!editingId} 
                onChange={(e) => setCountryId(e.target.value)}
                className={`w-full px-3 py-2 border rounded-lg text-sm uppercase focus:outline-none focus:ring-2 disabled:bg-slate-100 disabled:text-slate-400 ${
                  formSubmitted && !countryId ? 'border-red-400 bg-red-50/30' : 'border-slate-300'
                }`}
              />
              {!editingId && (
                <p className="text-[10px] text-slate-400 mt-1">Código único alfanumérico identificador.</p>
              )}
            </div>

            {/* NOMBRE DEL PAÍS */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Nombre Completo
              </label>
              <input
                type="text"
                placeholder="Ej: Colombia"
                value={countryName}
                onChange={(e) => setCountryName(e.target.value)}
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !countryName ? 'border-red-400 bg-red-50/30' : 'border-slate-300'
                }`}
              />
            </div>

            <div className="flex gap-2 pt-2">
              <button
                type="submit"
                disabled={isLoading}
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg disabled:bg-blue-400"
              >
                {editingId ? 'Actualizar País' : 'Guardar País'}
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
            <h2 className="text-lg font-semibold text-slate-700">Países Registrados</h2>
            
            {/* Input de Búsqueda Estilizado */}
            <div className="relative w-full sm:w-64">
              <span className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-slate-400 text-xs">
                🔍
              </span>
              <input
                type="text"
                placeholder="Buscar por nombre o código..."
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

          {isLoading && countries.length === 0 ? (
            <p className="text-sm text-slate-500 text-center py-8">Cargando países...</p>
          ) : countries.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">No hay países registrados en la base de datos.</p>
          ) : filteredCountries.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">
              No se encontraron coincidencias para "{searchTerm}".
            </p>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              {filteredCountries.map((country) => (
                <div key={country.id} className="p-4 border border-slate-100 rounded-xl bg-slate-50/30 flex justify-between items-center gap-2">
                  <div>
                    <span className="text-xs font-mono font-bold bg-slate-200 text-slate-700 px-1.5 py-0.5 rounded mr-2">
                      {country.id}
                    </span>
                    <span className="font-semibold text-slate-800 text-sm">{country.name}</span>
                  </div>
                  
                  <div className="flex gap-1 shrink-0">
                    <button
                      onClick={() => handleStartEdit(country)}
                      className="p-1 px-2 text-xs font-semibold text-blue-600 hover:bg-blue-50 rounded border border-blue-100 transition-colors"
                    >
                      Editar
                    </button>
                    <button
                      onClick={() => triggerDeleteConfirm(country)}
                      className="p-1 px-2 text-xs font-semibold text-red-600 hover:bg-red-50 rounded border border-red-100 transition-colors"
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

      {/* MODAL DE CONFIRMACIÓN DE ELIMINACIÓN */}
      {isDeleteModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm animate-fade-in">
          <div className="bg-white rounded-2xl border border-slate-200 shadow-xl max-w-md w-full overflow-hidden p-6 space-y-4 relative animate-scale-up">
            <div className="flex items-center gap-3 border-b border-slate-100 pb-3">
              <div className="w-10 h-10 rounded-full bg-red-50 flex items-center justify-center text-red-600 text-xl shrink-0">
                🗑️
              </div>
              <div>
                <h3 className="text-lg font-bold text-slate-800">¿Eliminar Territorio?</h3>
                <p className="text-xs text-slate-500">Esta acción desvinculará la procedencia de la base de datos.</p>
              </div>
            </div>

            <div className="bg-slate-50 p-3 rounded-lg border border-slate-100 text-sm text-slate-700">
              <p>
                <span className="font-semibold text-slate-500 text-xs uppercase tracking-wider block">País a borrar:</span> 
                {countryToDelete?.name} ({countryToDelete?.id})
              </p>
            </div>

            <p className="text-xs text-slate-500 leading-relaxed">
              Confirme si desea remover este país. Si existen equipos o clientes vinculados a este origen, la base de datos podría rechazar la acción por integridad referencial.
            </p>

            <div className="flex gap-2 justify-end pt-2">
              <button
                type="button"
                onClick={() => {
                  setIsDeleteModalOpen(false);
                  setCountryToDelete(null);
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
                Sí, remover país
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};