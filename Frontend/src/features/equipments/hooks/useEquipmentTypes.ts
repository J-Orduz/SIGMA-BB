import { useState, useEffect } from 'react';
import type { EquipmentType, CreateEquipmentTypeDTO } from '../types/equipment-type.types';
import { equipmentTypeService } from '../services/equipment-type.service';

export const useEquipmentTypes = () => {
  const [equipmentTypes, setEquipmentTypes] = useState<EquipmentType[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchTypes = async () => {
    setIsLoading(true);
    try {
      const data = await equipmentTypeService.getAll();
      setEquipmentTypes(data);
    } catch (err) {
      setError('No se pudo establecer conexión para cargar los tipos de equipo.');
    } finally {
      setIsLoading(false);
    }
  };

  const createType = async (typeData: CreateEquipmentTypeDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      await equipmentTypeService.create(typeData);
      await fetchTypes();
    } catch (err: any) {
      setError(err.message || 'Ocurrió un error al guardar el tipo de equipo. Verifique los campos requeridos.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateType = async (id: string, typeData: CreateEquipmentTypeDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      await equipmentTypeService.update(id, typeData);
      await fetchTypes();
    } catch (err: any) {
      setError(err.message || 'No se pudo actualizar la información del tipo de equipo.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteType = async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await equipmentTypeService.delete(id);
      await fetchTypes();
    } catch (err: any) {
      setError(err.message || 'Error al intentar dar de baja el registro.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchTypes();
  }, []);

  return { equipmentTypes, isLoading, error, createType, updateType, deleteType, setError };
};