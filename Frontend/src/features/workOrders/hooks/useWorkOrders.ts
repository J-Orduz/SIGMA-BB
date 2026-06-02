import { useState, useEffect, useCallback } from 'react';
import type {
  WorkOrder,
  WorkOrderCreateRequest,
  WorkOrderStatusRequest,
} from '../types/workOrder.types';
import { workOrderService } from '../services/workOrder.service';

export const useWorkOrders = (engineerId?: string) => {
  const [workOrders, setWorkOrders] = useState<WorkOrder[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Fetch all work orders or only those of a specific engineer
  const fetchWorkOrders = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = engineerId
        ? await workOrderService.getByEngineerId(engineerId)
        : await workOrderService.getAll();
      setWorkOrders(data);
    } catch {
      setError('No se pudieron cargar las órdenes de trabajo. Compruebe la conexión con el servidor.');
    } finally {
      setIsLoading(false);
    }
  }, [engineerId]);

  const createWorkOrder = async (data: WorkOrderCreateRequest): Promise<WorkOrder> => {
    setIsLoading(true);
    setError(null);
    try {
      const newWorkOrder = await workOrderService.create(data);
      await fetchWorkOrders();
      return newWorkOrder;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Error al crear la orden de trabajo.';
      setError(message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateWorkOrder = async (id: string, data: WorkOrderCreateRequest): Promise<WorkOrder> => {
    setIsLoading(true);
    setError(null);
    try {
      const updated = await workOrderService.update(id, data);
      await fetchWorkOrders();
      return updated;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Error al actualizar la orden de trabajo.';
      setError(message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateWorkOrderStatus = async (
    id: string,
    data: WorkOrderStatusRequest
  ): Promise<WorkOrder> => {
    setIsLoading(true);
    setError(null);
    try {
      const updated = await workOrderService.updateStatus(id, data);
      await fetchWorkOrders();
      return updated;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Error al actualizar el estado de la orden.';
      setError(message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteWorkOrder = async (id: string): Promise<void> => {
    setIsLoading(true);
    setError(null);
    try {
      await workOrderService.delete(id);
      await fetchWorkOrders();
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Error al eliminar la orden de trabajo.';
      setError(message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const getWorkOrderById = async (id: string): Promise<WorkOrder> => {
    setIsLoading(true);
    setError(null);
    try {
      return await workOrderService.getById(id);
    } catch (err) {
      setError('No se encontró la orden de trabajo solicitada.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchWorkOrders();
  }, [fetchWorkOrders]);

  return {
    workOrders,
    isLoading,
    error,
    setError,
    fetchWorkOrders,
    createWorkOrder,
    updateWorkOrder,
    updateWorkOrderStatus,
    deleteWorkOrder,
    getWorkOrderById,
  };
};
