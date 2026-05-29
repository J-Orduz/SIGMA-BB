import { useState, useEffect } from 'react';
import type { EquipmentType, CreateEquipmentTypeDTO } from '../types/equipment-type.types';
import { equipmentTypeService } from '../services/equipment-type.service';
import { getAuthHeaders } from '../services/equipment-type.service';
import { removeMetrologicalData as removeMetroService } from '../services/equipment-type.service';
import { addMetrologicalData as addMetroService } from '../services/equipment-type.service';

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
      // Almacenar la respuesta del servicio para extraer el ID
      const createdData = await equipmentTypeService.create(typeData);
      await fetchTypes();
      return createdData;
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

  const assignTechnicalVerification = async (equipmentTypeId: string, verificationId: string) => {
    try {
      const authHeaders = getAuthHeaders(null);
      const response = await fetch(`http://localhost:8100/v1/api/equipment-types/${equipmentTypeId}/technical-verification`, {
        method: 'POST',
        headers: { 
          ...authHeaders,
          'Content-Type': 'application/json' 
        },
        body: JSON.stringify(verificationId),
      }); 
      if (!response.ok) throw new Error('Error al asignar verificación');
      await fetchTypes();
    } catch (err) {
      console.error(err);
      throw err;
    }
  };

  const removeMetrologicalData = async (equipmentTypeId: string, value: number, type: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await removeMetroService(equipmentTypeId, value, type);
      await fetchTypes(); // Volvemos a consultar para actualizar la UI con los datos reales del backend
    } catch (err: any) {
      setError(err.message || 'No se pudo eliminar el dato metrológico.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const assignMetrologicalData = async (equipmentTypeId: string, value: number, type: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await addMetroService(equipmentTypeId, value, type);
      await fetchTypes();
    } catch (err: any) {
      setError(err.message || 'No se pudo agregar el dato metrológico.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const removeTechnicalVerification = async (equipmentTypeId: string, verificationId: string) => {
    try {
      const authHeaders = getAuthHeaders(null);
      const response = await fetch(`http://localhost:8100/v1/api/equipment-types/${equipmentTypeId}/technical-verification`, {
        method: 'DELETE',
        headers: { 
          ...authHeaders,
          'Content-Type': 'application/json' 
        },
        body: JSON.stringify(verificationId),
      });
      
      if (!response.ok) throw new Error('Error al remover la verificación técnica');
      await fetchTypes();
    } catch (err) {
      console.error("Error en removeTechnicalVerification:", err);
      setError('No se pudo desvincular la verificación técnica del equipo.');
      throw err;
    }
  };

  useEffect(() => {
    fetchTypes();
  }, []);

  return { 
    equipmentTypes, 
    isLoading, 
    error, 
    createType, 
    updateType, 
    deleteType, 
    assignTechnicalVerification, 
    removeTechnicalVerification, 
    removeMetrologicalData, 
    assignMetrologicalData,
    setError 
  };
};