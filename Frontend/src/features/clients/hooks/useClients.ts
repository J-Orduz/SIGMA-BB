import { useState, useEffect, useCallback } from 'react';
import type { Client, ClientCreateRequest } from '../types/client.types';
import { clientService } from '../services/client.service';

export const useClients = () => {
  const [clients, setClients] = useState<Client[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchClients = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await clientService.getAll();
      setClients(data);
    } catch {
      setError('No se pudieron cargar los clientes. Compruebe la conexión con el servidor.');
    } finally {
      setIsLoading(false);
    }
  }, []);

  const createClient = async (data: ClientCreateRequest): Promise<Client> => {
    setIsLoading(true);
    setError(null);
    try {
      const newClient = await clientService.create(data);
      await fetchClients();
      return newClient;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Error al registrar el cliente.';
      setError(message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateClient = async (id: string, data: ClientCreateRequest): Promise<Client> => {
    setIsLoading(true);
    setError(null);
    try {
      const updated = await clientService.update(id, data);
      await fetchClients();
      return updated;
    } catch (err) {
      setError('No se pudo actualizar el cliente. Intente nuevamente.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteClient = async (id: string): Promise<void> => {
    setIsLoading(true);
    setError(null);
    try {
      await clientService.delete(id);
      await fetchClients();
    } catch (err) {
      setError('No es posible eliminar este cliente. Puede tener registros asociados en el sistema.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const getClientById = async (id: string): Promise<Client> => {
    setIsLoading(true);
    setError(null);
    try {
      return await clientService.getById(id);
    } catch (err) {
      setError('No se encontró el cliente solicitado.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchClients();
  }, [fetchClients]);

  return {
    clients,
    isLoading,
    error,
    setError,
    fetchClients,
    createClient,
    updateClient,
    deleteClient,
    getClientById,
  };
};
