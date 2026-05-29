import { useState, useEffect } from 'react';
import type { Equipment, CreateEquipmentDTO } from '../types/equipment.types';
import { equipmentService } from '../services/equipment.service';

export const useEquipments = () => {
  const [equipments, setEquipments] = useState<Equipment[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchEquipments = async () => {
    setIsLoading(true);
    try {
      const data = await equipmentService.getAll();
      setEquipments(data);
    } catch (err: any) {
      setError(err.message || 'No se pudieron cargar los equipos.');
    } finally {
      setIsLoading(false);
    }
  };

  const createEquipment = async (data: CreateEquipmentDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const newEquip = await equipmentService.create(data);
      await fetchEquipments();
      return newEquip;
    } catch (err: any) {
      setError(err.message || 'Error al guardar el equipo.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateEquipment = async (id: string, data: CreateEquipmentDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      await equipmentService.update(id, data);
      await fetchEquipments();
    } catch (err: any) {
      setError(err.message || 'Error al actualizar el equipo.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteEquipment = async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await equipmentService.delete(id);
      await fetchEquipments();
    } catch (err: any) {
      setError(err.message || 'Error al eliminar el equipo.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchEquipments();
  }, []);

  return {
    equipments,
    isLoading,
    error,
    createEquipment,
    updateEquipment,
    deleteEquipment,
    setError,
    refrescar: fetchEquipments
  };
};