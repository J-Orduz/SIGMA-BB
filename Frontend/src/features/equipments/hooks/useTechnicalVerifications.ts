import { useState, useEffect, useCallback } from 'react';
import { technicalVerificationService } from '../services/technical-verification.service';
import type { 
  TechnicalVerification, 
  CreateTechnicalVerificationDTO, 
  UpdateTechnicalVerificationDTO 
} from '../types/technical-verification.types';

export const useTechnicalVerifications = () => {
  const [verifications, setVerifications] = useState<TechnicalVerification[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchVerifications = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await technicalVerificationService.getAll();
      setVerifications(data);
    } catch (err: any) {
      setError(err.message || 'Error al cargar verificaciones');
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchVerifications();
  }, [fetchVerifications]);

  const createVerification = async (data: CreateTechnicalVerificationDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const newVerification = await technicalVerificationService.create(data);
      setVerifications((prev) => [...prev, newVerification]);
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateVerification = async (id: string, data: UpdateTechnicalVerificationDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      const updated = await technicalVerificationService.update(id, data);
      setVerifications((prev) => prev.map((v) => (v.id === id ? updated : v)));
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteVerification = async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await technicalVerificationService.delete(id);
      setVerifications((prev) => prev.filter((v) => v.id !== id));
    } catch (err: any) {
      setError(err.message);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    verifications,
    isLoading,
    error,
    setError,
    createVerification,
    updateVerification,
    deleteVerification,
    refresh: fetchVerifications
  };
};