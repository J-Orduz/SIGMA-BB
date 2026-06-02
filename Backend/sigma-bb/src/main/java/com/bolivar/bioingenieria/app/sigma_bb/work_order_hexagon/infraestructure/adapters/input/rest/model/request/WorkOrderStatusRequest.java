package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para actualizar únicamente el estado de ejecución de una Orden de Trabajo.
 * Permite la transición: CREATED → IN_PROGRESS → EXECUTED.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderStatusRequest {

    /**
     * Nuevo estado de ejecución de la orden.
     * Valores permitidos: CREATED, IN_PROGRESS, EXECUTED
     */
    @NotBlank(message = "El estado de ejecución es obligatorio.")
    private String estadoEjecucion;
}
