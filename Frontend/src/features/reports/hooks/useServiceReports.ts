import { useEffect, useState } from 'react';
import { serviceReportService } from '../services/service-report.service';
import type { CreateServiceReportDTO, ServiceReport, UpdateServiceReportDTO } from '../types/service-report.types';

export const useServiceReports = () => {
  const [serviceReports, setServiceReports] = useState<ServiceReport[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchServiceReports = async () => {
    setIsLoading(true);
    setError(null);

    try {
      const data = await serviceReportService.getAll();
      setServiceReports(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudieron cargar los reportes de servicio.');
    } finally {
      setIsLoading(false);
    }
  };

  const createServiceReport = async (data: CreateServiceReportDTO) => {
    setIsLoading(true);
    setError(null);

    try {
      await serviceReportService.create(data);
      await fetchServiceReports();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo crear el reporte de servicio.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateServiceReport = async (id: string, data: UpdateServiceReportDTO) => {
    setIsLoading(true);
    setError(null);

    try {
      await serviceReportService.update(id, data);
      await fetchServiceReports();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo actualizar el reporte de servicio.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteServiceReport = async (id: string) => {
    setIsLoading(true);
    setError(null);

    try {
      await serviceReportService.delete(id);
      await fetchServiceReports();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo eliminar el reporte de servicio.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchServiceReports();
  }, []);

  return {
    serviceReports,
    isLoading,
    error,
    createServiceReport,
    updateServiceReport,
    deleteServiceReport,
    fetchServiceReports,
    setError,
  };
};
