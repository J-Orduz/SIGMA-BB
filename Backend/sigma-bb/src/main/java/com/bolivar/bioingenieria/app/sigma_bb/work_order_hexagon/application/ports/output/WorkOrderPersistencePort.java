package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.domain.model.WorkOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida (outbound) para la persistencia de Órdenes de Trabajo.
 * Define el contrato que debe cumplir el adaptador de persistencia.
 * Sigue el principio de arquitectura hexagonal (puertos y adaptadores).
 */
public interface WorkOrderPersistencePort {

    /**
     * Obtiene todas las órdenes de trabajo del repositorio.
     *
     * @return Lista de todas las órdenes de trabajo.
     */
    List<WorkOrder> findAll();

    /**
     * Busca una orden de trabajo por su identificador único.
     *
     * @param id Identificador UUID de la orden.
     * @return Optional con la orden encontrada o vacío si no existe.
     */
    Optional<WorkOrder> findById(UUID id);

    /**
     * Busca todas las órdenes asignadas a un ingeniero.
     *
     * @param engineerId Identificador UUID del ingeniero.
     * @return Lista de órdenes asignadas al ingeniero.
     */
    List<WorkOrder> findByEngineerId(UUID engineerId);

    /**
     * Persiste una nueva orden de trabajo en el repositorio.
     *
     * @param workOrder Modelo de dominio a persistir.
     * @return La orden persistida.
     */
    WorkOrder save(WorkOrder workOrder);

    /**
     * Actualiza una orden de trabajo existente.
     *
     * @param id        Identificador UUID de la orden a actualizar.
     * @param workOrder Modelo con los nuevos datos.
     * @return La orden actualizada.
     */
    WorkOrder update(UUID id, WorkOrder workOrder);

    /**
     * Elimina lógicamente una orden de trabajo (soft delete).
     *
     * @param id Identificador UUID de la orden a eliminar.
     */
    void delete(UUID id);
}
