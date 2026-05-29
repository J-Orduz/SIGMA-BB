import { useState, useEffect, useCallback } from 'react';
import type { Manufacturer, CreateManufacturerDTO, UpdateManufacturerDTO } from '../types/manufacturer.types';
import { manufacturerService } from '../services/manufacturer.service';

export const useManufacturers = () => {
  const [manufacturers, setManufacturers] = useState<Manufacturer[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchManufacturers = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await manufacturerService.getAll();
      setManufacturers(data || []);
    } catch (err: any) {
      setError(err.message || 'Error al cargar fabricantes.');
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchManufacturers();
  }, [fetchManufacturers]);

  const createManufacturer = async (payload: CreateManufacturerDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const newManu = await manufacturerService.create(payload);
      setManufacturers((prev) => [...prev, newManu]);
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateManufacturer = async (id: string, payload: UpdateManufacturerDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const updated = await manufacturerService.update(id, payload);
      setManufacturers((prev) => prev.map((m) => (m.id === id ? updated : m)));
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteManufacturer = async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await manufacturerService.delete(id);
      setManufacturers((prev) => prev.filter((m) => m.id !== id));
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    manufacturers,
    isLoading,
    error,
    setError,
    createManufacturer,
    updateManufacturer,
    deleteManufacturer,
    refetch: fetchManufacturers,
  };
};