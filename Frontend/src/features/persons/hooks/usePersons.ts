import { useState, useEffect, useCallback } from 'react';
import type { Person, PersonCreateRequest, PersonUpdateRequest } from '../types/person.types';
import { personService } from '../services/person.service';

export const usePersons = () => {
  const [persons, setPersons] = useState<Person[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchPersons = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await personService.getAll();
      setPersons(data || []);
    } catch (err: any) {
      setError(err.message || 'Error al obtener la lista de personas.');
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchPersons();
  }, [fetchPersons]);

  const createPerson = async (data: PersonCreateRequest, role: 'CEO_CLIENT' | 'ENGINEER' | 'ADMIN'): Promise<Person> => {
    setIsLoading(true);
    setError(null);
    try {
      const newPerson = await personService.create(data, role);
      await fetchPersons();
      return newPerson;
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updatePerson = async (id: string, data: PersonUpdateRequest): Promise<Person> => {
    setIsLoading(true);
    setError(null);
    try {
      const updated = await personService.update(id, data);
      await fetchPersons();
      return updated;
    } catch (err: any) {
      setError(err.message || 'Error al actualizar la persona.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deletePerson = async (id: string): Promise<void> => {
    setIsLoading(true);
    setError(null);
    try {
      await personService.delete(id);
      await fetchPersons();
    } catch (err: any) {
      setError(err.message || 'No se pudo eliminar la persona.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    persons,
    isLoading,
    error,
    setError,
    fetchPersons,
    createPerson,
    updatePerson,
    deletePerson,
  };
};
