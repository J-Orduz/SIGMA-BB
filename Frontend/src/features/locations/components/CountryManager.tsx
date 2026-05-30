import React, { useState } from 'react';
import { useCountries } from '../hooks/useCountries';
import { useCities } from '../hooks/useCities';
import type { Country } from '../types/country.types';
import type { City } from '../types/city.types';

type Tab = 'countries' | 'cities';

export const CountryManager: React.FC = () => {
  const { 
    countries, 
    isLoading: isCountryLoading, 
    error: countryError, 
    createCountry, 
    updateCountry, 
    deleteCountry, 
    setError: setCountryError 
  } = useCountries();

  const {
    cities,
    isLoading: isCityLoading,
    error: cityError,
    createCity,
    updateCity,
    deleteCity,
    setError: setCityError
  } = useCities();

  // Gestión de Pestañas
  const [activeTab, setActiveTab] = useState<Tab>('countries');

  // ===========================================================================
  // ESTADOS DEL FORMULARIO - PAÍSES
  // ===========================================================================
  const [countryId, setCountryId] = useState(''); // Código manual (COL, ARG, etc.)
  const [countryName, setCountryName] = useState('');
  const [editingCountryId, setEditingCountryId] = useState<string | null>(null);
  const [countryFormSubmitted, setCountryFormSubmitted] = useState(false);
  const [countrySuccessMsg, setCountrySuccessMsg] = useState('');
  const [countrySearchTerm, setCountrySearchTerm] = useState('');
  const [isCountryDeleteModalOpen, setIsCountryDeleteModalOpen] = useState(false);
  const [countryToDelete, setCountryToDelete] = useState<Country | null>(null);

  const resetCountryForm = () => {
    setCountryId('');
    setCountryName('');
    setEditingCountryId(null);
    setCountryFormSubmitted(false);
  };

  const showCountrySuccess = (msg: string) => {
    setCountrySuccessMsg(msg);
    setTimeout(() => setCountrySuccessMsg(''), 4000);
  };

  const handleCountrySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setCountryFormSubmitted(true);

    const cleanId = countryId.trim().toUpperCase();
    const cleanName = countryName.trim();

    if (!cleanId || !cleanName) {
      setCountryError('Por favor, rellene todos los campos obligatorios.');
      return;
    }

    if (cleanId.length < 2) {
      setCountryError('El código del país debe tener al menos 2 o 3 caracteres (Ej: COL).');
      return;
    }

    const payload = { id: cleanId, name: cleanName };

    try {
      if (editingCountryId) {
        await updateCountry(editingCountryId, payload);
        showCountrySuccess('¡País actualizado exitosamente!');
      } else {
        await createCountry(payload);
        showCountrySuccess('¡País registrado correctamente!');
      }
      resetCountryForm();
    } catch (err) {}
  };

  const handleStartCountryEdit = (country: Country) => {
    setEditingCountryId(country.id);
    setCountryId(country.id); 
    setCountryName(country.name);
  };

  const triggerCountryDeleteConfirm = (country: Country) => {
    setCountryToDelete(country);
    setIsCountryDeleteModalOpen(true);
  };

  const handleConfirmCountryDelete = async () => {
    if (!countryToDelete) return;
    try {
      await deleteCountry(countryToDelete.id);
      showCountrySuccess('País removido del sistema con éxito.');
    } catch (err) {}
    finally {
      setIsCountryDeleteModalOpen(false);
      setCountryToDelete(null);
    }
  };

  const filteredCountries = countries.filter((country) => {
    const term = countrySearchTerm.toLowerCase();
    return (
      country.name.toLowerCase().includes(term) ||
      country.id.toLowerCase().includes(term)
    );
  });


  // ===========================================================================
  // ESTADOS DEL FORMULARIO - CIUDADES
  // ===========================================================================
  const [cityId, setCityId] = useState(''); // Código manual (Ej: BOG)
  const [cityName, setCityName] = useState('');
  const [cityCountryId, setCityCountryId] = useState('');
  const [editingCityId, setEditingCityId] = useState<string | null>(null);
  const [cityFormSubmitted, setCityFormSubmitted] = useState(false);
  const [citySuccessMsg, setCitySuccessMsg] = useState('');
  const [citySearchTerm, setCitySearchTerm] = useState('');
  const [isCityDeleteModalOpen, setIsCityDeleteModalOpen] = useState(false);
  const [cityToDelete, setCityToDelete] = useState<City | null>(null);

  // Inicializar countryId por defecto cuando cambian los países
  React.useEffect(() => {
    if (countries.length > 0 && !cityCountryId) {
      setCityCountryId(countries[0].id);
    }
  }, [countries, cityCountryId]);

  const resetCityForm = () => {
    setCityId('');
    setCityName('');
    setCityCountryId(countries[0]?.id || '');
    setEditingCityId(null);
    setCityFormSubmitted(false);
  };

  const showCitySuccess = (msg: string) => {
    setCitySuccessMsg(msg);
    setTimeout(() => setCitySuccessMsg(''), 4000);
  };

  const handleCitySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setCityFormSubmitted(true);

    const cleanId = cityId.trim().toUpperCase();
    const cleanName = cityName.trim();
    const cleanCountryId = cityCountryId.trim();

    if (!cleanId || !cleanName || !cleanCountryId) {
      setCityError('Por favor, rellene todos los campos obligatorios.');
      return;
    }

    const payload = { id: cleanId, name: cleanName, countryId: cleanCountryId };

    try {
      if (editingCityId) {
        await updateCity(editingCityId, payload);
        showCitySuccess('¡Ciudad actualizada exitosamente!');
      } else {
        await createCity(payload);
        showCitySuccess('¡Ciudad registrada correctamente!');
      }
      resetCityForm();
    } catch (err) {}
  };

  const handleStartCityEdit = (city: City) => {
    setEditingCityId(city.id);
    setCityId(city.id);
    setCityName(city.name);
    setCityCountryId(city.countryId);
  };

  const triggerCityDeleteConfirm = (city: City) => {
    setCityToDelete(city);
    setIsCityDeleteModalOpen(true);
  };

  const handleConfirmCityDelete = async () => {
    if (!cityToDelete) return;
    try {
      await deleteCity(cityToDelete.id);
      showCitySuccess('Ciudad removida del sistema con éxito.');
    } catch (err) {}
    finally {
      setIsCityDeleteModalOpen(false);
      setCityToDelete(null);
    }
  };

  const filteredCities = cities.filter((city) => {
    const term = citySearchTerm.toLowerCase();
    const matchedCountry = countries.find(c => c.id === city.countryId)?.name || '';
    return (
      city.name.toLowerCase().includes(term) ||
      city.id.toLowerCase().includes(term) ||
      matchedCountry.toLowerCase().includes(term)
    );
  });

  return (
    <div className="p-4 md:p-6 max-w-7xl mx-auto space-y-6">
      {/* Cabecera */}
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Gestión de Ubicaciones</h1>
        <p className="text-sm text-slate-500">Administración de locaciones globales, países y mapeo de ciudades.</p>
      </div>

      {/* Tabs */}
      <div className="flex border-b border-slate-200 bg-white rounded-t-xl overflow-hidden shadow-sm">
        <button
          onClick={() => setActiveTab('countries')}
          className={`flex-1 sm:flex-none px-6 py-3.5 text-sm font-semibold border-b-2 transition-all flex items-center justify-center gap-2 ${
            activeTab === 'countries'
              ? 'border-blue-600 text-blue-600 bg-blue-50/10'
              : 'border-transparent text-slate-500 hover:text-slate-800 hover:bg-slate-50/50'
          }`}
        >
          🌎 Países
          <span className="text-xs bg-slate-200 text-slate-600 px-2 py-0.5 rounded-full font-medium">
            {countries.length}
          </span>
        </button>
        <button
          onClick={() => setActiveTab('cities')}
          className={`flex-1 sm:flex-none px-6 py-3.5 text-sm font-semibold border-b-2 transition-all flex items-center justify-center gap-2 ${
            activeTab === 'cities'
              ? 'border-blue-600 text-blue-600 bg-blue-50/10'
              : 'border-transparent text-slate-500 hover:text-slate-800 hover:bg-slate-50/50'
          }`}
        >
          🏙️ Ciudades
          <span className="text-xs bg-slate-200 text-slate-600 px-2 py-0.5 rounded-full font-medium">
            {cities.length}
          </span>
        </button>
      </div>

      {/* =======================================================================
          TAB: PAÍSES
          ======================================================================= */}
      {activeTab === 'countries' && (
        <div className="space-y-6">
          {countryError && (
            <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg flex justify-between items-center animate-fade-in">
              <span>⚠️ {countryError}</span>
              <button onClick={() => setCountryError(null)} className="text-red-500 font-bold ml-2">✕</button>
            </div>
          )}

          {countrySuccessMsg && (
            <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg animate-fade-in">
              💡 {countrySuccessMsg}
            </div>
          )}

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {/* FORMULARIO */}
            <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm h-fit space-y-4">
              <h2 className="text-lg font-semibold text-slate-700">
                {editingCountryId ? 'Modificar País' : 'Nuevo País'}
              </h2>

              <form onSubmit={handleCountrySubmit} noValidate className="space-y-4">
                {/* CÓDIGO ID DEL PAÍS */}
                <div>
                  <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                    Código del País (ID)
                  </label>
                  <input
                    type="text"
                    placeholder="Ej: COL"
                    value={countryId}
                    disabled={!!editingCountryId} 
                    onChange={(e) => setCountryId(e.target.value)}
                    className={`w-full px-3 py-2 border rounded-lg text-sm uppercase focus:outline-none focus:ring-2 disabled:bg-slate-100 disabled:text-slate-400 ${
                      countryFormSubmitted && !countryId ? 'border-red-400 bg-red-50/30' : 'border-slate-300 focus:ring-blue-500/20 focus:border-blue-500'
                    }`}
                  />
                  {!editingCountryId && (
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
                      countryFormSubmitted && !countryName ? 'border-red-400 bg-red-50/30' : 'border-slate-300 focus:ring-blue-500/20 focus:border-blue-500'
                    }`}
                  />
                </div>

                <div className="flex gap-2 pt-2">
                  <button
                    type="submit"
                    disabled={isCountryLoading}
                    className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg disabled:bg-blue-400 transition-colors"
                  >
                    {editingCountryId ? 'Actualizar País' : 'Guardar País'}
                  </button>
                  {editingCountryId && (
                    <button
                      type="button"
                      onClick={resetCountryForm}
                      className="bg-slate-200 hover:bg-slate-300 text-slate-700 font-medium text-sm py-2 px-3 rounded-lg transition-colors"
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
                
                <div className="relative w-full sm:w-64">
                  <span className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-slate-400 text-xs">
                    🔍
                  </span>
                  <input
                    type="text"
                    placeholder="Buscar por nombre o código..."
                    value={countrySearchTerm}
                    onChange={(e) => setCountrySearchTerm(e.target.value)}
                    className="w-full pl-8 pr-3 py-1.5 border border-slate-300 rounded-lg text-xs focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 placeholder-slate-400 bg-slate-50/50"
                  />
                  {countrySearchTerm && (
                    <button 
                      onClick={() => setCountrySearchTerm('')} 
                      className="absolute inset-y-0 right-0 flex items-center pr-2.5 text-slate-400 hover:text-slate-600 font-bold text-xs"
                    >
                      ✕
                    </button>
                  )}
                </div>
              </div>

              {isCountryLoading && countries.length === 0 ? (
                <p className="text-sm text-slate-500 text-center py-8">Cargando países...</p>
              ) : countries.length === 0 ? (
                <p className="text-sm text-slate-400 text-center py-8">No hay países registrados en la base de datos.</p>
              ) : filteredCountries.length === 0 ? (
                <p className="text-sm text-slate-400 text-center py-8">
                  No se encontraron coincidencias para "{countrySearchTerm}".
                </p>
              ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                  {filteredCountries.map((country) => (
                    <div key={country.id} className="p-4 border border-slate-100 rounded-xl bg-slate-50/30 flex justify-between items-center gap-2 hover:border-slate-200 transition-colors">
                      <div>
                        <span className="text-xs font-mono font-bold bg-slate-200 text-slate-700 px-1.5 py-0.5 rounded mr-2">
                          {country.id}
                        </span>
                        <span className="font-semibold text-slate-800 text-sm">{country.name}</span>
                      </div>
                      
                      <div className="flex gap-1 shrink-0">
                        <button
                          onClick={() => handleStartCountryEdit(country)}
                          className="p-1 px-2 text-xs font-semibold text-blue-600 hover:bg-blue-50 rounded border border-blue-100 transition-colors"
                        >
                          Editar
                        </button>
                        <button
                          onClick={() => triggerCountryDeleteConfirm(country)}
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
        </div>
      )}

      {/* =======================================================================
          TAB: CIUDADES
          ======================================================================= */}
      {activeTab === 'cities' && (
        <div className="space-y-6">
          {cityError && (
            <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg flex justify-between items-center animate-fade-in">
              <span>⚠️ {cityError}</span>
              <button onClick={() => setCityError(null)} className="text-red-500 font-bold ml-2">✕</button>
            </div>
          )}

          {citySuccessMsg && (
            <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg animate-fade-in">
              💡 {citySuccessMsg}
            </div>
          )}

          {countries.length === 0 ? (
            <div className="bg-amber-50 border border-amber-200 text-amber-800 p-6 rounded-xl text-center space-y-2">
              <span className="text-3xl">⚠️</span>
              <h3 className="font-bold text-lg">Se requiere registrar un país primero</h3>
              <p className="text-sm">Para poder crear ciudades, primero debe registrar al menos un país de procedencia en el sistema.</p>
              <button 
                onClick={() => setActiveTab('countries')}
                className="mt-2 bg-amber-600 hover:bg-amber-700 text-white font-medium text-xs py-2 px-4 rounded-lg transition-colors"
              >
                Registrar País
              </button>
            </div>
          ) : (
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
              {/* FORMULARIO DE CIUDADES */}
              <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm h-fit space-y-4">
                <h2 className="text-lg font-semibold text-slate-700">
                  {editingCityId ? 'Modificar Ciudad' : 'Nueva Ciudad'}
                </h2>

                <form onSubmit={handleCitySubmit} noValidate className="space-y-4">
                  {/* CÓDIGO ID DE LA CIUDAD */}
                  <div>
                    <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                      Código de Ciudad (ID)
                    </label>
                    <input
                      type="text"
                      placeholder="Ej: BOG"
                      value={cityId}
                      maxLength={3}
                      disabled={!!editingCityId}
                      onChange={(e) => setCityId(e.target.value)}
                      className={`w-full px-3 py-2 border rounded-lg text-sm uppercase focus:outline-none focus:ring-2 disabled:bg-slate-100 disabled:text-slate-400 ${
                        cityFormSubmitted && !cityId ? 'border-red-400 bg-red-50/30' : 'border-slate-300 focus:ring-blue-500/20 focus:border-blue-500'
                      }`}
                    />
                    {!editingCityId && (
                      <p className="text-[10px] text-slate-400 mt-1">Identificador único (Ej: BOG, MDE, CTG).</p>
                    )}
                  </div>

                  {/* NOMBRE DE LA CIUDAD */}
                  <div>
                    <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                      Nombre Completo
                    </label>
                    <input
                      type="text"
                      placeholder="Ej: Bogotá"
                      value={cityName}
                      onChange={(e) => setCityName(e.target.value)}
                      className={`w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 ${
                        cityFormSubmitted && !cityName ? 'border-red-400 bg-red-50/30' : 'border-slate-300 focus:ring-blue-500/20 focus:border-blue-500'
                      }`}
                    />
                  </div>

                  {/* SELECT DEL PAÍS */}
                  <div>
                    <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                      País al que pertenece
                    </label>
                    <select
                      value={cityCountryId}
                      onChange={(e) => setCityCountryId(e.target.value)}
                      className={`w-full px-3 py-2 border rounded-lg text-sm bg-white focus:outline-none focus:ring-2 ${
                        cityFormSubmitted && !cityCountryId ? 'border-red-400 bg-red-50/30' : 'border-slate-300 focus:ring-blue-500/20 focus:border-blue-500'
                      }`}
                    >
                      {countries.map((c) => (
                        <option key={c.id} value={c.id}>{c.name} ({c.id})</option>
                      ))}
                    </select>
                  </div>

                  <div className="flex gap-2 pt-2">
                    <button
                      type="submit"
                      disabled={isCityLoading}
                      className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg disabled:bg-blue-400 transition-colors"
                    >
                      {editingCityId ? 'Actualizar Ciudad' : 'Guardar Ciudad'}
                    </button>
                    {editingCityId && (
                      <button
                        type="button"
                        onClick={resetCityForm}
                        className="bg-slate-200 hover:bg-slate-300 text-slate-700 font-medium text-sm py-2 px-3 rounded-lg transition-colors"
                      >
                        Cancelar
                      </button>
                    )}
                  </div>
                </form>
              </div>

              {/* LISTADO DE CIUDADES */}
              <div className="lg:col-span-2 bg-white p-5 rounded-xl border border-slate-200 shadow-sm space-y-4">
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 border-b border-slate-100 pb-3">
                  <h2 className="text-lg font-semibold text-slate-700">Ciudades Registradas</h2>
                  
                  <div className="relative w-full sm:w-64">
                    <span className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none text-slate-400 text-xs">
                      🔍
                    </span>
                    <input
                      type="text"
                      placeholder="Buscar por nombre, código o país..."
                      value={citySearchTerm}
                      onChange={(e) => setCitySearchTerm(e.target.value)}
                      className="w-full pl-8 pr-3 py-1.5 border border-slate-300 rounded-lg text-xs focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 placeholder-slate-400 bg-slate-50/50"
                    />
                    {citySearchTerm && (
                      <button 
                        onClick={() => setCitySearchTerm('')} 
                        className="absolute inset-y-0 right-0 flex items-center pr-2.5 text-slate-400 hover:text-slate-600 font-bold text-xs"
                      >
                        ✕
                      </button>
                    )}
                  </div>
                </div>

                {isCityLoading && cities.length === 0 ? (
                  <p className="text-sm text-slate-500 text-center py-8">Cargando ciudades...</p>
                ) : cities.length === 0 ? (
                  <p className="text-sm text-slate-400 text-center py-8">No hay ciudades registradas en la base de datos.</p>
                ) : filteredCities.length === 0 ? (
                  <p className="text-sm text-slate-400 text-center py-8">
                    No se encontraron coincidencias para "{citySearchTerm}".
                  </p>
                ) : (
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                    {filteredCities.map((city) => {
                      const parentCountry = countries.find(c => c.id === city.countryId);
                      return (
                        <div key={city.id} className="p-4 border border-slate-100 rounded-xl bg-slate-50/30 flex justify-between items-center gap-2 hover:border-slate-200 transition-colors">
                          <div className="space-y-1">
                            <div>
                              <span className="text-xs font-mono font-bold bg-slate-200 text-slate-700 px-1.5 py-0.5 rounded mr-2">
                                {city.id}
                              </span>
                              <span className="font-semibold text-slate-800 text-sm">{city.name}</span>
                            </div>
                            <p className="text-[10px] text-slate-500 flex items-center gap-1">
                              <span>🌎 País:</span> 
                              <span className="font-medium text-slate-700">{parentCountry ? parentCountry.name : city.countryId}</span>
                            </p>
                          </div>
                          
                          <div className="flex gap-1 shrink-0">
                            <button
                              onClick={() => handleStartCityEdit(city)}
                              className="p-1 px-2 text-xs font-semibold text-blue-600 hover:bg-blue-50 rounded border border-blue-100 transition-colors"
                            >
                              Editar
                            </button>
                            <button
                              onClick={() => triggerCityDeleteConfirm(city)}
                              className="p-1 px-2 text-xs font-semibold text-red-600 hover:bg-red-50 rounded border border-red-100 transition-colors"
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
          )}
        </div>
      )}

      {/* =======================================================================
          MODAL DE CONFIRMACIÓN DE ELIMINACIÓN - PAÍS
          ======================================================================= */}
      {isCountryDeleteModalOpen && (
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
              Confirme si desea remover este país. Si existen ciudades, equipos o clientes vinculados a este origen, la base de datos podría rechazar la acción por integridad referencial.
            </p>

            <div className="flex gap-2 justify-end pt-2">
              <button
                type="button"
                onClick={() => {
                  setIsCountryDeleteModalOpen(false);
                  setCountryToDelete(null);
                }}
                className="px-4 py-2 border border-slate-200 text-slate-700 font-medium text-sm rounded-xl hover:bg-slate-50 transition-colors"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={handleConfirmCountryDelete}
                className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white font-medium text-sm rounded-xl shadow-sm transition-colors"
              >
                Sí, remover país
              </button>
            </div>
          </div>
        </div>
      )}

      {/* =======================================================================
          MODAL DE CONFIRMACIÓN DE ELIMINACIÓN - CIUDAD
          ======================================================================= */}
      {isCityDeleteModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm animate-fade-in">
          <div className="bg-white rounded-2xl border border-slate-200 shadow-xl max-w-md w-full overflow-hidden p-6 space-y-4 relative animate-scale-up">
            <div className="flex items-center gap-3 border-b border-slate-100 pb-3">
              <div className="w-10 h-10 rounded-full bg-red-50 flex items-center justify-center text-red-600 text-xl shrink-0">
                🗑️
              </div>
              <div>
                <h3 className="text-lg font-bold text-slate-800">¿Eliminar Ciudad?</h3>
                <p className="text-xs text-slate-500">Esta acción desvinculará la ciudad de la base de datos.</p>
              </div>
            </div>

            <div className="bg-slate-50 p-3 rounded-lg border border-slate-100 text-sm text-slate-700">
              <p>
                <span className="font-semibold text-slate-500 text-xs uppercase tracking-wider block">Ciudad a borrar:</span> 
                {cityToDelete?.name} ({cityToDelete?.id})
              </p>
            </div>

            <p className="text-xs text-slate-500 leading-relaxed">
              Confirme si desea remover esta ciudad. Si existen sedes o dependencias vinculadas a esta locación, la base de datos podría rechazar la acción por integridad referencial.
            </p>

            <div className="flex gap-2 justify-end pt-2">
              <button
                type="button"
                onClick={() => {
                  setIsCityDeleteModalOpen(false);
                  setCityToDelete(null);
                }}
                className="px-4 py-2 border border-slate-200 text-slate-700 font-medium text-sm rounded-xl hover:bg-slate-50 transition-colors"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={handleConfirmCityDelete}
                className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white font-medium text-sm rounded-xl shadow-sm transition-colors"
              >
                Sí, remover ciudad
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};