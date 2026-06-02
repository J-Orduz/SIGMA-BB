package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.domain.model.WorkOrder;

import java.util.List;
import java.util.UUID;

/**
 * Puerto de entrada (inbound) para el servicio de Órdenes de Trabajo.
 * Define los casos de uso disponibles para la capa de aplicación.
 * Sigue el principio de arquitectura hexagonal (puertos y adaptadores).
 */
public interface WorkOrderServicePort {

    /**
     * Obtiene todas las órdenes de trabajo activas en el sistema.
     *
     * @return Lista de todas las órdenes de trabajo.
     */
    List<WorkOrder> findAll();

    /**
     * Obtiene una orden de trabajo por su identificador único.
     *
     * @param id Identificador UUID de la orden de trabajo.
     * @return La orden de trabajo encontrada.
     */
    WorkOrder findById(UUID id);

    /**
     * Obtiene todas las órdenes de trabajo asignadas a un ingeniero específico.
     *
     * @param engineerId Identificador UUID del ingeniero.
     * @return Lista de órdenes de trabajo del ingeniero.
     */
    List<WorkOrder> findByEngineerId(UUID engineerId);

    /**
     * Crea y persiste una nueva orden de trabajo.
     *
     * @param workOrder Modelo de dominio con los datos de la nueva orden.
     * @return La orden de trabajo creada con su ID generado.
     */
    WorkOrder save(WorkOrder workOrder);

    /**
     * Actualiza los datos completos de una orden de trabajo existente.
     *
     * @param id        Identificador UUID de la orden a actualizar.
     * @param workOrder Modelo de dominio con los nuevos datos.
     * @return La orden de trabajo actualizada.
     */
    WorkOrder update(UUID id, WorkOrder workOrder);

    /**
     * Actualiza únicamente el estado de ejecución de una orden de trabajo.
     * Permite la transición: CREATED → IN_PROGRESS → EXECUTED.
     *
     * @param id              Identificador UUID de la orden.
     * @param estadoEjecucion Nuevo estado de ejecución.
     * @return La orden de trabajo con el estado actualizado.
     */
    WorkOrder updateStatus(UUID id, String estadoEjecucion);

    /**
     * Realiza el soft-delete de una orden de trabajo (b_estado_activo = false).
     *
     * @param id Identificador UUID de la orden a eliminar.
     */
    void delete(UUID id);
}
