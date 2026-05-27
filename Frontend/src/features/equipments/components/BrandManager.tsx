import React, { useState } from 'react';
import { useBrands } from '../hooks/useBrands';

// Definición de interfaz local para controlar el estado del modal de borrado
interface DeleteModalState {
  isOpen: boolean;
  brandId: string | null;
  brandName: string;
}

export const BrandManager: React.FC = () => {
  const { brands, isLoading, error, createBrand, updateBrand, deleteBrand, setError } = useBrands();
  const [name, setName] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  // Estado local para el término de búsqueda
  const [searchTerm, setSearchTerm] = useState('');

  // Estados locales para el modo edición en línea
  const [editingId, setEditingId] = useState<string | null>(null);
  const [editingName, setEditingName] = useState('');

  // Estado que controla la ventana de confirmación de borrado
  const [deleteModal, setDeleteModal] = useState<DeleteModalState>({
    isOpen: false,
    brandId: null,
    brandName: ''
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;

    try {
      await createBrand({ name });
      setName('');
      showSuccess('¡Marca creada con éxito!');
    } catch (err) {}
  };

  const handleStartEdit = (id: string, currentName: string) => {
    setEditingId(id);
    setEditingName(currentName);
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setEditingName('');
  };

  const handleSaveEdit = async (id: string) => {
    if (!editingName.trim()) return;
    try {
      await updateBrand(id, editingName);
      setEditingId(null);
      showSuccess('¡Marca actualizada correctamente!');
    } catch (err) {}
  };

  // Guarda los datos y abre el modal visual
  const handleDeleteClick = (id: string, brandName: string) => {
    setDeleteModal({
      isOpen: true,
      brandId: id,
      brandName: brandName
    });
  };

  // Se ejecuta cuando el usuario confirma la acción dentro de la ventana modal
  const handleConfirmDelete = async () => {
    if (!deleteModal.brandId) return;

    const idToDelete = deleteModal.brandId;
    
    // Cerrar el modal inmediatamente de forma visual
    setDeleteModal({ isOpen: false, brandId: null, brandName: '' });

    try {
      await deleteBrand(idToDelete);
      showSuccess('Marca eliminada del sistema.');
    } catch (err) {}
  };

  const showSuccess = (msg: string) => {
    setSuccessMsg(msg);
    setTimeout(() => setSuccessMsg(''), 4000);
  };

  const filteredBrands = brands.filter((brand) =>
    brand.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="p-4 md:p-6 max-w-5xl mx-auto space-y-8 relative">
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Gestión de Marcas</h1>
        <p className="text-sm text-slate-500">Módulo administrativo para registrar, modificar y dar de baja fabricantes biomédicos.</p>
      </div>

      {/* Alertas de Feedback globales */}
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

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Formulario de Registro */}
        <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm h-fit">
          <h2 className="text-lg font-semibold text-slate-700 mb-4">Nueva Marca</h2>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-xs font-medium text-slate-600 uppercase tracking-wider mb-1">
                Nombre de la Marca
              </label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Ej. Siemens, Fluke, Philips"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                required
              />
            </div>
            <button
              type="submit"
              disabled={isLoading}
              className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg transition-colors disabled:bg-blue-400"
            >
              {isLoading && editingId === null ? 'Guardando...' : 'Crear Marca'}
            </button>
          </form>
        </div>

        {/* Tabla / Grid de Marcas */}
        <div className="md:col-span-2 bg-white p-5 rounded-xl border border-slate-200 shadow-sm space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
            <h2 className="text-lg font-semibold text-slate-700">Marcas Registradas</h2>
            
            {brands.length > 0 && (
              <div className="relative w-full sm:w-64">
                <span className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-slate-400 text-sm">
                  🔍
                </span>
                <input
                  type="text"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  placeholder="Buscar por nombre..."
                  className="w-full pl-9 pr-3 py-1.5 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all placeholder:text-slate-400 bg-slate-50/50"
                />
              </div>
            )}
          </div>

          {isLoading && brands.length === 0 ? (
            <p className="text-sm text-slate-500">Sincronizando con el servidor de SIGMA-BB...</p>
          ) : brands.length === 0 ? (
            <p className="text-sm text-slate-500">No hay marcas configuradas.</p>
          ) : filteredBrands.length === 0 ? (
            <p className="text-sm text-slate-400 bg-slate-50 p-4 rounded-lg text-center border border-dashed border-slate-200">
              No se encontraron coincidencias para "{searchTerm}"
            </p>
          ) : (
            <div className="overflow-x-auto rounded-lg border border-slate-100">
              <table className="w-full text-left text-sm text-slate-600">
                <thead className="bg-slate-50 text-xs text-slate-700 uppercase tracking-wider border-b border-slate-100">
                  <tr>
                    <th className="px-4 py-3">Nombre de la Marca</th>
                    <th className="px-4 py-3 text-right">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {filteredBrands.map((brand) => (
                    <tr key={brand.id} className="hover:bg-slate-50/50 transition-colors">
                      <td className="px-4 py-3">
                        {editingId === brand.id ? (
                          <input
                            type="text"
                            value={editingName}
                            onChange={(e) => setEditingName(e.target.value)}
                            className="px-2 py-1 border border-blue-400 bg-blue-50/30 rounded text-sm focus:outline-none focus:ring-1 focus:ring-blue-500 w-full max-w-xs font-medium text-slate-800"
                            autoFocus
                          />
                        ) : (
                          <span className="font-medium text-slate-800">{brand.name}</span>
                        )}
                      </td>

                      <td className="px-4 py-3 text-right whitespace-nowrap">
                        {editingId === brand.id ? (
                          <div className="flex justify-end gap-2">
                            <button
                              onClick={() => handleSaveEdit(brand.id)}
                              disabled={isLoading}
                              className="px-2.5 py-1 bg-green-600 hover:bg-green-700 text-white text-xs font-semibold rounded-md transition-colors"
                            >
                              Guardar
                            </button>
                            <button
                              onClick={handleCancelEdit}
                              className="px-2.5 py-1 bg-slate-200 hover:bg-slate-300 text-slate-700 text-xs font-semibold rounded-md transition-colors"
                            >
                              Cancelar
                            </button>
                          </div>
                        ) : (
                          <div className="flex justify-end gap-2">
                            <button
                              onClick={() => handleStartEdit(brand.id, brand.name)}
                              className="px-2.5 py-1 text-blue-600 hover:text-blue-700 hover:bg-blue-50 text-xs font-semibold rounded-md border border-blue-200 transition-colors"
                            >
                              Editar
                            </button>
                            <button
                              onClick={() => handleDeleteClick(brand.id, brand.name)}
                              className="px-2.5 py-1 text-red-600 hover:text-red-700 hover:bg-red-50 text-xs font-semibold rounded-md border border-red-100 transition-colors"
                            >
                              Eliminar
                            </button>
                          </div>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* VENTANA MODAL BONITA DE CONFIRMACIÓN */}
      {deleteModal.isOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-sm animate-fade-in">
          {/* Tarjeta contenedora del diálogo */}
          <div className="bg-white rounded-xl shadow-xl border border-slate-200 max-w-md w-full p-6 space-y-6 transform scale-100 transition-all">
            
            {/* Encabezado e Icono de Advertencia */}
            <div className="flex items-start gap-4">
              <div className="p-3 bg-red-50 text-red-600 rounded-full shrink-0">
                <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                  <path strokeLinecap="round" strokeLinejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </div>
              <div className="space-y-1">
                <h3 className="text-lg font-bold text-slate-800">¿Eliminar fabricante biomédico?</h3>
                <p className="text-sm text-slate-500">
                  Está a punto de dar de baja la marca <span className="font-semibold text-slate-700">"{deleteModal.brandName}"</span>. Esta acción no se puede deshacer si el elemento no tiene históricos vinculados.
                </p>
              </div>
            </div>

            {/* Botones de acción inferiores */}
            <div className="flex justify-end gap-3 pt-2">
              <button
                type="button"
                onClick={() => setDeleteModal({ isOpen: false, brandId: null, brandName: '' })}
                className="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium text-sm rounded-lg transition-colors focus:outline-none"
              >
                No, cancelar
              </button>
              <button
                type="button"
                onClick={handleConfirmDelete}
                className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white font-medium text-sm rounded-lg transition-colors shadow-sm focus:outline-none"
              >
                Sí, eliminar marca
              </button>
            </div>

          </div>
        </div>
      )}

    </div>
  );
};