import React, { useState, useEffect } from 'react';
import { useEquipmentTypes } from '../hooks/useEquipmentTypes';
import type { MetrologicalData, CreateEquipmentTypeDTO } from '../types/equipment-type.types';
import { getAuthHeaders } from '../services/equipment-type.service';

const VERIFICATION_TYPES_MAP: Record<string, string> = {
  'VER_TEC_INIC': 'Verificación Inicial Técnica',
  'PROC_MANT': 'Protocolo de Mantenimiento'
};

interface DeleteModalState {
  isOpen: boolean;
  typeId: string | null;
  typeName: string;
}

export const EquipmentTypeManager: React.FC = () => {
  const { 
    equipmentTypes, 
    isLoading, 
    error, 
    createType, 
    updateType,
    deleteType, 
    assignTechnicalVerification, 
    removeTechnicalVerification, 
    setError 
  } = useEquipmentTypes();

  // Traer el listado maestro completo de verificaciones técnicas del sistema para listarlas abajo
  const [allTechnicalVerification, setAllTechnicalVerification] = useState<any[]>([]);

  useEffect(() => {
  const headers = getAuthHeaders(null); 

  fetch('http://localhost:8100/v1/api/technical-verifications', {
    method: 'GET',
    headers: headers
  }) 
    .then(res => {
      if (!res.ok) {
        throw new Error(`Error en el servidor: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      setAllTechnicalVerification(data || []);
    })
    .catch((err) => {
      console.error("Error detallado cargando verificaciones:", err);
    });
}, []);

  // Estados del Formulario Principal
  const [name, setName] = useState('');
  const [definition, setDefinition] = useState('');
  const [recommendations, setRecommendations] = useState('');
  const [voltage, setVoltage] = useState<number | ''>('');
  const [amperage, setAmperage] = useState<number | ''>('');
  const [technology, setTechnology] = useState('');
  const [verifiable, setVerifiable] = useState(false);
  const [maintenanceValue, setMaintenanceValue] = useState<number | ''>('');
  
  // Estado local para agregar datos metrológicos dinámicamente
  const [metroList, setMetroList] = useState<MetrologicalData[]>([]);
  const [metroValue, setMetroValue] = useState<number | ''>('');
  const [metroType, setMetroType] = useState('');

  // Estados de control de UI
  const [searchTerm, setSearchTerm] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [editingId, setEditingId] = useState<string | null>(null);
  const [formSubmitted, setFormSubmitted] = useState(false);

  // Estados de control del modal de validación metrológica
  const [isMetroAlertOpen, setIsMetroAlertOpen] = useState(false);

  // Estados para la sección de Verificación Técnica
  const [requiresTechVerification, setRequiresTechVerification] = useState(false);
  const [selectedTechVerifications, setSelectedTechVerifications] = useState<string[]>([]); // Array de IDs elegidos
  const [initialTechVerifications, setInitialTechVerifications] = useState<string[]>([]);   // Copia original para comparar al editar

  // Estado del Modal de Eliminación
  const [deleteModal, setDeleteModal] = useState<DeleteModalState>({
    isOpen: false,
    typeId: null,
    typeName: ''
  });

  const handleAddMetroData = () => {
    if (!metroType.trim() || metroValue === '') return;
    if (Number(metroValue) < 0) {
      setError('El valor metrológico base no puede ser un número negativo.');
      return; 
    }

    setMetroList([...metroList, { value: Number(metroValue), type: metroType.trim() }]);
    setMetroValue('');
    setMetroType('');
  };

  const handleRemoveMetroData = (index: number) => {
    setMetroList(metroList.filter((_, i) => i !== index));
  };

  const handleToggleTechVerification = (id: string) => {
    if (selectedTechVerifications.includes(id)) {
      setSelectedTechVerifications(selectedTechVerifications.filter(item => item !== id));
    } else {
      setSelectedTechVerifications([...selectedTechVerifications, id]);
    }
  };

  const resetForm = () => {
    setName('');
    setDefinition('');
    setRecommendations('');
    setVoltage('');
    setAmperage('');
    setTechnology('');
    setVerifiable(false);
    setMaintenanceValue('');
    setMetroList([]);
    setEditingId(null);
    setFormSubmitted(false);
    setRequiresTechVerification(false);
    setSelectedTechVerifications([]);
    setInitialTechVerifications([]);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFormSubmitted(true);

    // Validación manual antes de enviar
    if (!name.trim() || voltage === '' || amperage === '' || !technology.trim() || maintenanceValue === '' || !definition.trim() || !recommendations.trim()) {
      setError('Por favor, complete todos los campos obligatorios marcados en rojo.');
      return;
    }

    // Validación de voltaje positivo
    const voltValue = Number(voltage);
    if (voltValue <= 0) {
      setError('El voltaje debe ser un número estrictamente mayor a 0 V.');
      return;
    }

    // Validación de rangos de amperaje
    const ampValue = Number(amperage);
    if (ampValue <= 0 || ampValue >= 100) {
      setError('El amperaje debe ser estrictamente mayor a 0 y menor a 100 A.');
      return; 
    }

    // Validación de valor de mantenimiento estrictamente mayor a 0
    const maintValue = Number(maintenanceValue);
    if (maintValue <= 0) {
      setError('El valor de mantenimiento debe ser un monto estrictamente mayor a $0.');
      return;
    }

    // Validación metrologica
    if (verifiable && metroList.length === 0) {
      setIsMetroAlertOpen(true);
      return;
    }

    const payload: CreateEquipmentTypeDTO = {
      equipmentTypeName: name.trim(),
      technicalDefinition: definition.trim() || 'Sin definición técnica registrada',
      careRecommendations: recommendations.trim() || 'Seguir manual del fabricante',
      voltage: Math.round(Number(voltage)), 
      amperage: Number(Number(amperage).toFixed(2)), 
      predominantTechnology: technology.trim() || 'Digital',
      verifiable: verifiable,
      unitMaintenanceValue: Number(maintenanceValue || 0),
      metrologicalData: verifiable ? metroList : []
    };

    try {
      if (editingId) {
        if (requiresTechVerification && selectedTechVerifications.length === 0) {
          setError('Si activa la casilla de Verificación Técnica, debe seleccionar al menos una de la lista.');
          return;
        }
        await updateType(editingId, payload);
        showSuccess('¡Tipo de equipo actualizado exitosamente!');
        
        const toDelete = initialTechVerifications.filter(id => !selectedTechVerifications.includes(id));
        for (const id of toDelete) {
          await removeTechnicalVerification(editingId, id);
        }
        const toAdd = selectedTechVerifications.filter(id => !initialTechVerifications.includes(id));
        for (const id of toAdd) {
          await assignTechnicalVerification(editingId, id);
        }
      } else {
        await createType(payload);
        showSuccess('¡Tipo de equipo registrado correctamente!');
      }
      resetForm();
    } catch (err) {}
  };

  const handleStartEdit = (id: string) => {
    const target = equipmentTypes.find((t) => t.id === id);
    if (!target) return;
    setEditingId(target.id);
    setName(target.equipmentTypeName);
    setDefinition(target.technicalDefinition);
    setRecommendations(target.careRecommendations);
    setVoltage(target.voltage ?? '');
    setAmperage(target.amperage ?? '');
    setTechnology(target.predominantTechnology);
    setVerifiable(target.verifiable);
    setMaintenanceValue(target.unitMaintenanceValue);
    setMetroList(target.metrologicalData || []);
    const mappedIds = (target as any).technicalVerification || [];
    setRequiresTechVerification(mappedIds.length > 0);
    setSelectedTechVerifications(mappedIds);
    setInitialTechVerifications(mappedIds);
  };

  const handleDeleteTrigger = (id: string, name: string) => {
    setDeleteModal({ isOpen: true, typeId: id, typeName: name });
  };

  const handleConfirmDelete = async () => {
    if (!deleteModal.typeId) return;
    const targetId = deleteModal.typeId;
    setDeleteModal({ isOpen: false, typeId: null, typeName: '' });
    try {
      await deleteType(targetId);
      showSuccess('Registro removido con éxito.');
    } catch (err) {}
  };

  const showSuccess = (msg: string) => {
    setSuccessMsg(msg);
    setTimeout(() => setSuccessMsg(''), 4000);
  };

  const filteredTypes = Array.isArray(equipmentTypes) 
    ? equipmentTypes.filter((t) => t.equipmentTypeName.toLowerCase().includes(searchTerm.toLowerCase()))
    : [];

  return (
    <div className="p-4 md:p-6 max-w-7xl mx-auto space-y-6 relative">
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Tipos de Equipos</h1>
        <p className="text-sm text-slate-500">Configuración de fichas técnicas maestras de dispositivos biomédicos.</p>
      </div>

      {error && (
        <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg flex justify-between items-center animate-fade-in">
          <span>⚠️ {error}</span>
          <button onClick={() => setError(null)} className="text-red-500 font-bold ml-2 hover:text-red-800">✕</button>
        </div>
      )}

      {successMsg && (
        <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg animate-fade-in">
          💡 {successMsg}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* FORMULARIO DE CREACIÓN / EDICIÓN */}
        <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm h-fit space-y-4">
          <h2 className="text-lg font-semibold text-slate-700">
            {editingId ? '📝 Modificar Tipo' : 'Nuevo Tipo de Equipo'}
          </h2>

          <form onSubmit={handleSubmit} noValidate className="space-y-4">             
            {/* INPUT NOMBRE */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Nombre</label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Ej. Electrocardiógrafo"
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !name.trim() 
                    ? 'border-red-400 focus:ring-red-200 bg-red-50/30' 
                    : 'border-slate-300 focus:ring-blue-500'
                }`}
              />
              {formSubmitted && !name.trim() && (
                <p className="text-[11px] text-red-500 font-medium mt-1 animate-fade-in">⚠ El nombre es obligatorio.</p>
              )}
            </div>

            <div className="grid grid-cols-2 gap-3">
              {/* INPUT VOLTAJE */}
              <div>
                <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Voltaje (V)</label>
                <input
                  type="number"
                  min="1"
                  value={voltage}
                  onChange={(e) => setVoltage(e.target.value === '' ? '' : Number(e.target.value))}
                  placeholder="110"
                  className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                    formSubmitted && (voltage === '' || Number(voltage) <= 0) 
                      ? 'border-red-400 focus:ring-red-200 bg-red-50/30' 
                      : 'border-slate-300 focus:ring-blue-500'
                  }`}
                />
                {formSubmitted && voltage === '' && (
                  <p className="text-[11px] text-red-500 font-medium mt-1 animate-fade-in">⚠ Campo requerido.</p>
                )}
                {formSubmitted && voltage !== '' && Number(voltage) <= 0 && (
                  <p className="text-[11px] text-red-500 font-medium mt-1 animate-fade-in">⚠ Debe ser mayor a 0.</p>
                )}
              </div>

              {/* INPUT AMPERAJE */}
              <div>
                <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Amperaje (A)</label>
                <input
                  type="number"
                  step="0.01"
                  value={amperage}
                  onChange={(e) => setAmperage(e.target.value === '' ? '' : Number(e.target.value))}
                  placeholder="2.5"
                  className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                    formSubmitted && (amperage === '' || Number(amperage) <= 0 || Number(amperage) >= 100) 
                      ? 'border-red-400 focus:ring-red-200 bg-red-50/30' 
                      : 'border-slate-300 focus:ring-blue-500'
                  }`}
                />
                {formSubmitted && amperage === '' && (
                  <p className="text-[11px] text-red-500 font-medium mt-1 animate-fade-in">⚠ Campo requerido.</p>
                )}
                {formSubmitted && amperage !== '' && (Number(amperage) <= 0 || Number(amperage) >= 100) && (
                  <p className="text-[11px] text-red-500 font-medium mt-1 animate-fade-in">⚠ Rango: 0.01 a 99.99</p>
                )}
              </div>
            </div>

            <div className="grid grid-cols-2 gap-3">
              {/* INPUT TECNOLOGÍA */}
              <div>
                <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Tecnología</label>
                <input
                  type="text"
                  value={technology}
                  onChange={(e) => setTechnology(e.target.value)}
                  placeholder="Ej. Digital, Neumática"
                  className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                    formSubmitted && !technology.trim() 
                      ? 'border-red-400 focus:ring-red-200 bg-red-50/30' 
                      : 'border-slate-300 focus:ring-blue-500'
                  }`}
                />
                {formSubmitted && !technology.trim() && (
                  <p className="text-[11px] text-red-500 font-medium mt-1 animate-fade-in">⚠ Campo requerido.</p>
                )}
              </div>

              {/* INPUT VALOR MANTENIMIENTO */}
              <div>
                <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Valor Mantenimiento</label>
                <input
                  type="number"
                  min="1"
                  value={maintenanceValue}
                  onChange={(e) => setMaintenanceValue(e.target.value === '' ? '' : Number(e.target.value))}
                  placeholder="$"
                  className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                    formSubmitted && (maintenanceValue === '' || Number(maintenanceValue) <= 0) 
                      ? 'border-red-400 focus:ring-red-200 bg-red-50/30' 
                      : 'border-slate-300 focus:ring-blue-500'
                  }`}
                />
                {formSubmitted && maintenanceValue === '' && (
                  <p className="text-[11px] text-red-500 font-medium mt-1 animate-fade-in">⚠ Campo requerido.</p>
                )}
                {formSubmitted && maintenanceValue !== '' && Number(maintenanceValue) <= 0 && (
                  <p className="text-[11px] text-red-500 font-medium mt-1 animate-fade-in">⚠ Debe ser mayor a 0.</p>
                )}
              </div>
            </div>

            {/* TEXTAREA DEFINICIÓN TÉCNICA */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Definición Técnica</label>
              <textarea
                value={definition}
                onChange={(e) => setDefinition(e.target.value)}
                rows={2}
                placeholder="Describa el funcionamiento técnico..."
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !definition.trim() 
                    ? 'border-red-400 focus:ring-red-200 bg-red-50/30' 
                    : 'border-slate-300 focus:ring-blue-500'
                }`}
              />
              {formSubmitted && !definition.trim() && (
                <p className="text-[11px] text-red-500 font-medium mt-1 animate-fade-in">⚠ Defina la descripción técnica.</p>
              )}
            </div>

            {/* TEXTAREA RECOMENDACIONES */}
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">Recomendaciones de Cuidado</label>
              <textarea
                value={recommendations}
                onChange={(e) => setRecommendations(e.target.value)}
                rows={2}
                placeholder="Instrucciones de limpieza o seguridad..."
                className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                  formSubmitted && !recommendations.trim() 
                    ? 'border-red-400 focus:ring-red-200 bg-red-50/30' 
                    : 'border-slate-300 focus:ring-blue-500'
                }`}
              />
              {formSubmitted && !recommendations.trim() && (
                <p className="text-[11px] text-red-500 font-medium mt-1 animate-fade-in">⚠ Escriba los cuidados básicos.</p>
              )}
            </div>

            {/* SECCIÓN DE VERIFICACIÓN METROLÓGICA */}
            <div className="pt-2 border-t border-slate-100">
              <label className="flex items-center space-x-2 cursor-pointer select-none">
                <input
                  type="checkbox"
                  checked={verifiable}
                  onChange={(e) => setVerifiable(e.target.checked)}
                  className="w-4 h-4 text-blue-600 rounded border-slate-300 focus:ring-blue-500"
                />
                <span className="text-sm font-medium text-slate-700">¿Requiere Verificación Metrológica?</span>
              </label>
            </div>

            {verifiable && (
              <div className="bg-slate-50 p-3 rounded-lg border border-slate-200 space-y-2">
                <p className="text-xs font-bold text-slate-500 uppercase tracking-wider">Datos Metrológicos Base</p>
                <div className="flex gap-2">
                  <input
                    type="text"
                    value={metroType}
                    onChange={(e) => setMetroType(e.target.value)}
                    placeholder="Tipo (Ej. Presión)"
                    className="w-1/2 px-2 py-1 text-xs border border-slate-300 rounded focus:outline-none"
                  />
                  <input
                    type="number"
                    step="0.1"
                    min="0"
                    value={metroValue}
                    onChange={(e) => setMetroValue(e.target.value === '' ? '' : Number(e.target.value))}
                    placeholder="Valor"
                    className="w-1/3 px-2 py-1 text-xs border border-slate-300 rounded focus:outline-none focus:ring-1 focus:ring-blue-500"
                  />
                  <button
                    type="button"
                    onClick={handleAddMetroData}
                    className="bg-slate-800 text-white px-2 rounded text-xs hover:bg-slate-700 transition-colors"
                  >
                    +
                  </button>
                </div>
                
                {metroList.length > 0 ? (
                  <div className="text-xs space-y-1 pt-1 max-h-24 overflow-y-auto">
                    {metroList.map((m, i) => (
                      <div key={i} className="flex justify-between items-center bg-white px-2 py-1 rounded border border-slate-100">
                        <span>{m.type}: <strong className="text-slate-800">{m.value}</strong></span>
                        <button type="button" onClick={() => handleRemoveMetroData(i)} className="text-red-500 hover:text-red-700">✕</button>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="text-[11px] text-orange-600 italic pt-1">
                    * Ingrese el Tipo, Valor y presione "+" para agregarlo a la ficha técnica obligatoriamente.
                  </p>
                )}
              </div>
            )}
            
            {editingId && (
              <div className="pt-2 border-t border-slate-100 space-y-3">
                <label className="flex items-center space-x-2 cursor-pointer select-none">
                  <input
                    type="checkbox"
                    checked={requiresTechVerification}
                    onChange={(e) => {
                      setRequiresTechVerification(e.target.checked);
                      if (!e.target.checked) setSelectedTechVerifications([]);
                    }}
                    className="w-4 h-4 text-blue-600 rounded border-slate-300 focus:ring-blue-500"
                  />
                  <span className="text-sm font-medium text-slate-700">¿Requiere Verificación Técnica?</span>
                </label>

                {requiresTechVerification && (
                  <div className="bg-slate-50 p-3 rounded-lg border border-slate-200 space-y-2">
                    <p className="text-xs font-bold text-slate-500 uppercase tracking-wider">Verificaciones Disponibles</p>
                    
                    {allTechnicalVerification.length > 0 ? (
                      <div className="space-y-1.5 max-h-40 overflow-y-auto pr-1">
                        {allTechnicalVerification.map((tv) => (
                          <label 
                            key={tv.id} 
                            className="flex items-center justify-between p-2 bg-white rounded border border-slate-200 text-xs hover:bg-slate-50 transition-colors cursor-pointer"
                          >
                            <div className="flex flex-col pr-2 truncate">
                              <span className="font-semibold text-slate-700 truncate">{tv.description}</span>
                              <span className="text-[10px] text-slate-400 font-medium">
                                Tipo: {VERIFICATION_TYPES_MAP[tv.verificationType] || tv.verificationType}
                              </span>
                            </div>
                            <input
                              type="checkbox"
                              checked={selectedTechVerifications.includes(tv.id)}
                              onChange={() => handleToggleTechVerification(tv.id)}
                              className="w-4 h-4 text-blue-600 rounded border-slate-300 focus:ring-blue-500 flex-shrink-0"
                            />
                          </label>
                        ))}
                      </div>
                    ) : (
                      <p className="text-[11px] text-slate-400 italic">No hay criterios técnicos parametrizados en el sistema.</p>
                    )}
                    
                    {formSubmitted && selectedTechVerifications.length === 0 && (
                      <p className="text-[11px] text-red-500 font-medium mt-1">⚠ Debe seleccionar al menos una verificación técnica.</p>
                    )}
                  </div>
                )}
              </div>
            )}

            <div className="flex gap-2 pt-2">
              <button
                type="submit"
                disabled={isLoading}
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg transition-colors disabled:bg-blue-400"
              >
                {editingId ? 'Actualizar Ficha' : 'Guardar Tipo'}
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

        {/* LISTADO PRINCIPAL */}
        <div className="lg:col-span-2 bg-white p-5 rounded-xl border border-slate-200 shadow-sm space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
            <h2 className="text-lg font-semibold text-slate-700">Catálogo de Fichas Configuradas</h2>
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Buscar por nombre..."
              className="px-3 py-1.5 border border-slate-300 rounded-lg text-sm bg-slate-50/50 w-full sm:w-64 focus:outline-none focus:ring-2 focus:ring-blue-500 transition-all"
            />
          </div>

          {isLoading && filteredTypes.length === 0 ? (
            <p className="text-sm text-slate-500 text-center py-8">Sincronizando catálogo técnico con SIGMA-BB...</p>
          ) : filteredTypes.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">No se encontraron tipos de equipos registrados que coincidan.</p>
          ) : (
            <div className="grid grid-cols-1 gap-4">
              {filteredTypes.map((type) => (
                <div key={type.id} className="p-4 border border-slate-100 rounded-xl bg-slate-50/30 hover:border-slate-200 transition-all space-y-3">
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="font-bold text-slate-800 text-base">{type.equipmentTypeName}</h3>
                      <span className="text-xs text-slate-400 font-mono">Tecnología: {type.predominantTechnology}</span>
                    </div>
                    <div className="flex gap-1">
                      <button
                        onClick={() => handleStartEdit(type.id)}
                        className="p-1 px-2.5 text-xs font-semibold text-blue-600 hover:bg-blue-50 rounded border border-blue-200 transition-colors"
                      >
                        Editar
                      </button>
                      <button
                        onClick={() => handleDeleteTrigger(type.id, type.equipmentTypeName)}
                        className="p-1 px-2.5 text-xs font-semibold text-red-600 hover:bg-red-50 rounded border border-red-100 transition-colors"
                      >
                        Eliminar
                      </button>
                    </div>
                  </div>

                  <div className="grid grid-cols-2 sm:grid-cols-4 gap-2 text-xs text-slate-600 bg-white p-2.5 rounded-lg border border-slate-100">
                    <div>⚡ Voltaje: <strong className="text-slate-800">{type.voltage ?? 'N/A'} V</strong></div>
                    <div>🔋 Amperaje: <strong className="text-slate-800">{type.amperage ?? 'N/A'} A</strong></div>
                    <div>💰 Costo Mant.: <strong className="text-slate-800">${type.unitMaintenanceValue ? type.unitMaintenanceValue.toLocaleString() : '0'}</strong></div>
                    <div>
                      Metrología:{' '}
                      <span className={`px-1.5 py-0.5 rounded text-[10px] font-bold ${type.verifiable ? 'bg-orange-100 text-orange-800' : 'bg-slate-100 text-slate-500'}`}>
                        {type.verifiable ? 'APLICA' : 'NO APLICA'}
                      </span>
                    </div>
                    <div>
                      Verif. Técnica:{' '}
                      <span className={`px-1.5 py-0.5 rounded text-[10px] font-bold ${type.technicalVerification && type.technicalVerification.length > 0 ? 'bg-blue-100 text-blue-800' : 'bg-slate-100 text-slate-500'}`}>
                        {type.technicalVerification && type.technicalVerification.length > 0 ? `${type.technicalVerification.length} ASIGNADAS` : 'NO APLICA'}
                      </span>
                    </div>
                  </div>
                  
                    {type.technicalVerification && type.technicalVerification.length > 0 && typeof type.technicalVerification !== 'string' && (
                    <div className="p-2 bg-white/60 rounded border border-slate-100 text-[11px] space-y-1">
                      <p className="font-bold text-slate-400 uppercase tracking-wide text-[9px]">Verificaciones Vinculadas:</p>
                      <div className="flex flex-wrap gap-1">
                        {type.technicalVerification.map((tvId: any) => {
                          const originalVerification = allTechnicalVerification.find(
                            (masterTv) => masterTv.id === tvId
                          );
                          const typeCode = originalVerification?.verificationType;
                          const typeLabel = typeCode 
                            ? (VERIFICATION_TYPES_MAP[typeCode] || typeCode) 
                            : 'Tipo desconocido';

                          return (
                            <span 
                              key={tvId} 
                              className="px-2 py-0.5 bg-blue-50 text-blue-700 rounded text-[10px] font-medium border border-blue-100"
                            >
                              {typeLabel}
                            </span>
                          );
                        })}
                      </div>
                    </div>
                  )}

                  <div className="text-xs space-y-1 bg-white/50 p-2 rounded border border-slate-100">
                    <p><span className="font-semibold text-slate-700">Definición:</span> {type.technicalDefinition}</p>
                    <p><span className="font-semibold text-slate-700">Cuidados:</span> {type.careRecommendations}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
      
      {/* MODAL ERROR METROLOGÍA */}
      {isMetroAlertOpen && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm flex justify-center items-center z-50 p-4 animate-fade-in">
          <div className="bg-white max-w-md w-full rounded-xl shadow-xl border border-slate-200 p-6 space-y-4 transform transition-all scale-100">
            <div className="flex items-center space-x-3 text-orange-600">
              <h3 className="text-lg font-bold text-slate-800">Se requiere configuración metrológica</h3>
            </div>
            <p className="text-sm text-slate-600 leading-relaxed">
              Ha seleccionado que este tipo de equipo <span className="font-semibold text-slate-800">¿Requiere Verificación Metrológica?</span>. 
              Por lo tanto, es estrictamente necesario ingresar al menos un <span className="font-medium text-slate-900">Tipo y Valor</span> de referencia metrológica base.
            </p>
            <div className="bg-amber-50 border border-amber-200 rounded-lg p-3 text-xs text-amber-800 space-y-1">
              <p className="font-semibold">💡 ¿Cómo solucionarlo?</p>
              <ol className="list-decimal pl-4 space-y-0.5">
                <li>Escriba el tipo (ej: <i>Presión</i>, <i>Flujo</i>).</li>
                <li>Escriba el valor numérico base.</li>
                <li>Haga clic en el botón <strong className="bg-slate-200 px-1 rounded font-bold text-slate-700">+</strong> para guardarlo en el listado antes de enviar la ficha técnica.</li>
              </ol>
            </div>
            <div className="flex pt-2 justify-end">
              <button
                type="button"
                onClick={() => setIsMetroAlertOpen(false)}
                className="w-full sm:w-auto px-5 py-2 bg-slate-900 text-white text-sm font-medium rounded-lg hover:bg-slate-800 transition-colors"
              >
                Entendido
              </button>
            </div>
          </div>
        </div>
      )}

      {/* VENTANA MODAL DE CONFIRMACIÓN DE ELIMINACIÓN */}
      {deleteModal.isOpen && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm flex justify-center items-center z-50 p-4 animate-fade-in">
          <div className="bg-white max-w-md w-full rounded-xl shadow-xl border border-slate-200 p-6 space-y-4 transform transition-all scale-100">
            <div className="flex items-center space-x-3 text-red-600">
              <div className="p-2 bg-red-50 rounded-lg"><span className="text-xl">⚠️</span></div>
              <h3 className="text-lg font-bold text-slate-800">¿Confirmar Eliminación?</h3>
            </div>
            <p className="text-sm text-slate-600 leading-relaxed">
              ¿Está seguro de que desea eliminar permanentemente la ficha técnica de: 
              <strong className="text-slate-900 block mt-1 font-semibold text-base">"{deleteModal.typeName}"</strong>?
            </p>
            <div className="flex space-x-3 pt-2 justify-end">
              <button
                type="button"
                onClick={() => setDeleteModal({ isOpen: false, typeId: null, typeName: '' })}
                className="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 text-sm font-semibold rounded-lg transition-colors focus:outline-none"
              >
                No, Cancelar
              </button>
              <button
                type="button"
                onClick={handleConfirmDelete}
                className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white text-sm font-semibold rounded-lg transition-colors shadow-sm shadow-red-200 focus:outline-none"
              >
                Sí, Eliminar Registro
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};