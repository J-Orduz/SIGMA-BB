import { useState, useEffect, useCallback } from 'react';
import type { Country, CreateCountryDTO, UpdateCountryDTO } from '../types/country.types';
import { countryService } from '../services/country.service';

export const useCountries = () => {
  const [countries, setCountries] = useState<Country[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchCountries = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await countryService.getAll();
      setCountries(data || []);
    } catch (err: any) {
      setError(err.message || 'Error desconocido al cargar países.');
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchCountries();
  }, [fetchCountries]);

  const createCountry = async (payload: CreateCountryDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const newCountry = await countryService.create(payload);
      setCountries((prev) => [...prev, newCountry]);
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateCountry = async (id: string, payload: UpdateCountryDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const updated = await countryService.update(id, payload);
      setCountries((prev) => prev.map((c) => (c.id === id ? updated : c)));
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteCountry = async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await countryService.delete(id);
      setCountries((prev) => prev.filter((c) => c.id !== id));
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    countries,
    isLoading,
    error,
    setError,
    createCountry,
    updateCountry,
    deleteCountry,
    refetch: fetchCountries,
  };
};