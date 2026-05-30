import { useState, useEffect, useCallback } from 'react';
import type { City, CreateCityDTO, UpdateCityDTO } from '../types/city.types';
import { cityService } from '../services/city.service';

export const useCities = () => {
  const [cities, setCities] = useState<City[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchCities = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await cityService.getAll();
      setCities(data || []);
    } catch (err: any) {
      setError(err.message || 'Error al cargar las ciudades.');
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchCities();
  }, [fetchCities]);

  const createCity = async (payload: CreateCityDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const newCity = await cityService.create(payload);
      setCities((prev) => [...prev, newCity]);
      return newCity;
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateCity = async (id: string, payload: UpdateCityDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const updated = await cityService.update(id, payload);
      setCities((prev) => prev.map((c) => (c.id === id ? updated : c)));
      return updated;
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteCity = async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await cityService.delete(id);
      setCities((prev) => prev.filter((c) => c.id !== id));
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    cities,
    isLoading,
    error,
    setError,
    createCity,
    updateCity,
    deleteCity,
    refetch: fetchCities,
  };
};
