import React, { useMemo, useState } from 'react';
import { useServiceReports } from '../hooks/useServiceReports';
import type { CreateServiceReportDTO, ServiceReport } from '../types/service-report.types';

interface DeleteModalState {
  isOpen: boolean;
  report: ServiceReport | null;
}

const initialForm: CreateServiceReportDTO = {
  workOrderId: '',
  clientEquipmentId: '',
  observations: '',
  technicalVerificationResult: '',
};

export const ServiceReportManager: React.FC = () => {
  const {
    serviceReports,
    isLoading,
    error,
    createServiceReport,
    updateServiceReport,
    deleteServiceReport,
    setError,
  } = useServiceReports();

  const [formData, setFormData] = useState<CreateServiceReportDTO>(initialForm);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [formSubmitted, setFormSubmitted] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [deleteModal, setDeleteModal] = useState<DeleteModalState>({
    isOpen: false,
    report: null,
  });

  const filteredReports = useMemo(() => {
    const term = searchTerm.toLowerCase().trim();
    if (!term) return serviceReports;

    return serviceReports.filter((report) =>
      [
        report.id,
        report.workOrderId,
        report.clientEquipmentId,
        report.observations || '',
        report.technicalVerificationResult || '',
      ].some((value) => value.toLowerCase().includes(term))
    );
  }, [searchTerm, serviceReports]);

  const showSuccess = (message: string) => {
    setSuccessMsg(message);
    setTimeout(() => setSuccessMsg(''), 4000);
  };

  const resetForm = () => {
    setFormData(initialForm);
    setEditingId(null);
    setFormSubmitted(false);
  };

  const updateField = (field: keyof CreateServiceReportDTO, value: string) => {
    setFormData((current) => ({
      ...current,
      [field]: value,
    }));
  };

  const buildPayload = (): CreateServiceReportDTO => ({
    workOrderId: formData.workOrderId.trim(),
    clientEquipmentId: formData.clientEquipmentId.trim(),
    observations: formData.observations?.trim() || '',
    technicalVerificationResult: formData.technicalVerificationResult?.trim() || '',
  });

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setFormSubmitted(true);
    setError(null);

    const payload = buildPayload();

    if (!payload.workOrderId || !payload.clientEquipmentId) {
      setError('Complete la orden de trabajo y el equipo cliente para guardar el reporte.');
      return;
    }

    if (payload.workOrderId.length > 10 || payload.clientEquipmentId.length > 10) {
      setError('La orden de trabajo y el equipo cliente no pueden superar 10 caracteres.');
      return;
    }

    try {
      if (editingId) {
        await updateServiceReport(editingId, payload);
        showSuccess('Reporte de servicio actualizado correctamente.');
      } else {
        await createServiceReport(payload);
        showSuccess('Reporte de servicio creado correctamente.');
      }
      resetForm();
    } catch (err) {}
  };

  const handleStartEdit = (report: ServiceReport) => {
    setEditingId(report.id);
    setFormData({
      workOrderId: report.workOrderId || '',
      clientEquipmentId: report.clientEquipmentId || '',
      observations: report.observations || '',
      technicalVerificationResult: report.technicalVerificationResult || '',
    });
    setFormSubmitted(false);
    setError(null);
  };

  const handleConfirmDelete = async () => {
    if (!deleteModal.report) return;

    try {
      await deleteServiceReport(deleteModal.report.id);
      showSuccess('Reporte de servicio eliminado.');
    } catch (err) {}
    finally {
      setDeleteModal({ isOpen: false, report: null });
    }
  };

  const requiredInputClass = (value: string) =>
    `w-full px-3 py-2 border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 ${
      formSubmitted && !value.trim() ? 'border-red-400 bg-red-50/40' : 'border-slate-300'
    }`;

  return (
    <div className="p-4 md:p-6 max-w-7xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Reportes de Servicio</h1>
        <p className="text-sm text-slate-500">Registro operativo de reportes asociados a ordenes de trabajo y equipos cliente.</p>
      </div>

      {error && (
        <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg flex justify-between items-center">
          <span>{error}</span>
          <button onClick={() => setError(null)} className="text-red-500 hover:text-red-700 font-bold ml-2">Cerrar</button>
        </div>
      )}

      {successMsg && (
        <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-sm rounded-lg">
          {successMsg}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm h-fit space-y-4">
          <h2 className="text-lg font-semibold text-slate-700">
            {editingId ? 'Editar Reporte' : 'Nuevo Reporte'}
          </h2>

          <form onSubmit={handleSubmit} noValidate className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Orden de Trabajo
              </label>
              <input
                type="text"
                value={formData.workOrderId}
                maxLength={10}
                onChange={(event) => updateField('workOrderId', event.target.value)}
                placeholder="Ej. OT-001"
                className={requiredInputClass(formData.workOrderId)}
              />
              <p className="mt-1 text-[11px] text-slate-400">Maximo 10 caracteres.</p>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Equipo Cliente
              </label>
              <input
                type="text"
                value={formData.clientEquipmentId}
                maxLength={10}
                onChange={(event) => updateField('clientEquipmentId', event.target.value)}
                placeholder="Ej. EQC-001"
                className={requiredInputClass(formData.clientEquipmentId)}
              />
              <p className="mt-1 text-[11px] text-slate-400">Maximo 10 caracteres.</p>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Resultado Tecnico
              </label>
              <input
                type="text"
                value={formData.technicalVerificationResult}
                maxLength={50}
                onChange={(event) => updateField('technicalVerificationResult', event.target.value)}
                placeholder="Ej. CONFORME"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-600 uppercase tracking-wider mb-1">
                Observaciones
              </label>
              <textarea
                value={formData.observations}
                maxLength={250}
                rows={4}
                onChange={(event) => updateField('observations', event.target.value)}
                placeholder="Detalle del trabajo realizado, hallazgos o recomendaciones."
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
              />
              <p className="mt-1 text-[11px] text-slate-400">{formData.observations?.length || 0}/250 caracteres.</p>
            </div>

            <div className="flex gap-2 pt-2">
              <button
                type="submit"
                disabled={isLoading}
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium text-sm py-2 px-4 rounded-lg disabled:bg-blue-400"
              >
                {editingId ? 'Actualizar Reporte' : 'Crear Reporte'}
              </button>
              {editingId && (
                <button
                  type="button"
                  onClick={resetForm}
                  className="bg-slate-200 hover:bg-slate-300 text-slate-700 font-medium text-sm py-2 px-3 rounded-lg"
                >
                  Cancelar
                </button>
              )}
            </div>
          </form>
        </div>

        <div className="lg:col-span-2 bg-white p-5 rounded-xl border border-slate-200 shadow-sm space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
            <h2 className="text-lg font-semibold text-slate-700">Reportes Registrados</h2>
            <input
              type="text"
              value={searchTerm}
              onChange={(event) => setSearchTerm(event.target.value)}
              placeholder="Buscar por orden, equipo o resultado..."
              className="w-full sm:w-72 px-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 bg-slate-50/50"
            />
          </div>

          {isLoading && serviceReports.length === 0 ? (
            <p className="text-sm text-slate-500 text-center py-8">Cargando reportes de servicio...</p>
          ) : serviceReports.length === 0 ? (
            <p className="text-sm text-slate-400 text-center py-8">No hay reportes de servicio registrados.</p>
          ) : filteredReports.length === 0 ? (
            <p className="text-sm text-slate-400 bg-slate-50 p-4 rounded-lg text-center border border-dashed border-slate-200">
              No se encontraron reportes para "{searchTerm}".
            </p>
          ) : (
            <div className="overflow-x-auto rounded-lg border border-slate-100">
              <table className="w-full text-left text-sm text-slate-600">
                <thead className="bg-slate-50 text-xs text-slate-700 uppercase tracking-wider border-b border-slate-100">
                  <tr>
                    <th className="px-4 py-3">Orden</th>
                    <th className="px-4 py-3">Equipo Cliente</th>
                    <th className="px-4 py-3">Resultado</th>
                    <th className="px-4 py-3">Observaciones</th>
                    <th className="px-4 py-3 text-right">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {filteredReports.map((report) => (
                    <tr key={report.id} className="hover:bg-slate-50/60 transition-colors">
                      <td className="px-4 py-3 font-semibold text-slate-800">{report.workOrderId}</td>
                      <td className="px-4 py-3">{report.clientEquipmentId}</td>
                      <td className="px-4 py-3">
                        <span className="px-2 py-1 bg-blue-50 text-blue-700 rounded-md text-xs font-semibold border border-blue-100">
                          {report.technicalVerificationResult || 'Sin resultado'}
                        </span>
                      </td>
                      <td className="px-4 py-3 max-w-xs">
                        <p className="truncate" title={report.observations || ''}>
                          {report.observations || 'Sin observaciones'}
                        </p>
                      </td>
                      <td className="px-4 py-3 text-right whitespace-nowrap">
                        <div className="flex justify-end gap-2">
                          <button
                            onClick={() => handleStartEdit(report)}
                            className="px-2.5 py-1 text-blue-600 hover:text-blue-700 hover:bg-blue-50 text-xs font-semibold rounded-md border border-blue-200 transition-colors"
                          >
                            Editar
                          </button>
                          <button
                            onClick={() => setDeleteModal({ isOpen: true, report })}
                            className="px-2.5 py-1 text-red-600 hover:text-red-700 hover:bg-red-50 text-xs font-semibold rounded-md border border-red-100 transition-colors"
                          >
                            Eliminar
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {deleteModal.isOpen && deleteModal.report && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-sm">
          <div className="bg-white rounded-xl shadow-xl border border-slate-200 max-w-md w-full p-6 space-y-5">
            <div>
              <h3 className="text-lg font-bold text-slate-800">Eliminar reporte de servicio</h3>
              <p className="text-sm text-slate-500 mt-1">
                Se eliminara el reporte asociado a la orden <span className="font-semibold text-slate-700">{deleteModal.report.workOrderId}</span>.
              </p>
            </div>

            <div className="bg-slate-50 border border-slate-100 rounded-lg p-3 text-sm text-slate-600">
              <p><span className="font-semibold">Equipo cliente:</span> {deleteModal.report.clientEquipmentId}</p>
              <p><span className="font-semibold">Resultado:</span> {deleteModal.report.technicalVerificationResult || 'Sin resultado'}</p>
            </div>

            <div className="flex justify-end gap-3">
              <button
                type="button"
                onClick={() => setDeleteModal({ isOpen: false, report: null })}
                className="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium text-sm rounded-lg transition-colors"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={handleConfirmDelete}
                className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white font-medium text-sm rounded-lg transition-colors"
              >
                Eliminar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
