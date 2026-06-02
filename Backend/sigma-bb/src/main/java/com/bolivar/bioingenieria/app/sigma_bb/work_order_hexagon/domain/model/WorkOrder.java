package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Modelo de dominio para la Orden de Trabajo.
 * Representa la lógica central del negocio para la gestión
 * de órdenes de mantenimiento de equipos clínicos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrder {

    /** Identificador único de la orden de trabajo (UUID). */
    private UUID identificadorOrdenTrabajo;

    /** Fecha programada para el mantenimiento. */
    private LocalDateTime fechaMantenimiento;

    /**
     * Periodicidad del mantenimiento.
     * Valores permitidos: MONTHLY, QUARTERLY, BIANNUAL, ANUAL
     */
    private String periodicidad;

    /**
     * Identificador del ingeniero responsable de la orden.
     * FK hacia la tabla persona (tipo ENGINEER).
     */
    private UUID identificadorIngeniero;

    /**
     * Estado de ejecución de la orden.
     * Valores permitidos: CREATED, IN_PROGRESS, EXECUTED
     */
    private String estadoEjecucion;

    /** Indica si la orden está activa en el sistema. */
    private boolean estadoActivo;

    /**
     * Lista de identificadores de equipos del cliente
     * que serán intervenidos en esta orden de trabajo.
     */
    private List<UUID> equipoClienteIds;
}
