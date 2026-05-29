import { useState, useEffect, useCallback } from 'react';
import type { EquipmentModel, CreateModelDTO, UpdateModelDTO } from '../types/model.types';
import { modelService } from '../services/model.service';

export const useModels = () => {
  const [models, setModels] = useState<EquipmentModel[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchModels = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await modelService.getAll();
      setModels(data || []);
    } catch (err: any) {
      setError(err.message || 'Error al cargar los modelos.');
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchModels();
  }, [fetchModels]);

  const createModel = async (payload: CreateModelDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const newModel = await modelService.create(payload);
      setModels((prev) => [...prev, newModel]);
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateModel = async (id: string, payload: UpdateModelDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const updated = await modelService.update(id, payload);
      setModels((prev) => prev.map((m) => (m.id === id ? updated : m)));
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteModel = async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await modelService.delete(id);
      setModels((prev) => prev.filter((m) => m.id !== id));
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    models,
    isLoading,
    error,
    setError,
    createModel,
    updateModel,
    deleteModel,
    refetch: fetchModels,
  };
};