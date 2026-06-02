package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de respuesta para las Órdenes de Trabajo.
 * Representa los datos devueltos al cliente (frontend) tras operaciones CRUD.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderResponse {

    /** Identificador único de la orden de trabajo (UUID). */
    private UUID identificadorOrdenTrabajo;

    /** Fecha y hora del mantenimiento programado. */
    private LocalDateTime fechaMantenimiento;

    /** Periodicidad del mantenimiento (MONTHLY, QUARTERLY, BIANNUAL, ANUAL). */
    private String periodicidad;

    /** Identificador del ingeniero responsable de la orden. */
    private UUID identificadorIngeniero;

    /** Estado actual de ejecución (CREATED, IN_PROGRESS, EXECUTED). */
    private String estadoEjecucion;

    /** Indica si la orden está activa en el sistema. */
    private boolean estadoActivo;

    /** Lista de UUIDs de los equipos del cliente intervenidos en la orden. */
    private List<UUID> equipoClienteIds;
}
