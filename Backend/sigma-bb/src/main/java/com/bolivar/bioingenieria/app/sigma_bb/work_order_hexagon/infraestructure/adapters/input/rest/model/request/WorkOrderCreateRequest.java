package com.bolivar.bioingenieria.app.sigma_bb.work_order_hexagon.infraestructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de entrada para la creación y actualización de Órdenes de Trabajo.
 * Mapea los datos enviados desde el cliente (frontend) al sistema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderCreateRequest {

    /**
     * Fecha y hora programada para el mantenimiento.
     */
    @NotNull(message = "La fecha de mantenimiento es obligatoria.")
    private LocalDateTime fechaMantenimiento;

    /**
     * Periodicidad del mantenimiento.
     * Valores permitidos: MONTHLY, QUARTERLY, BIANNUAL, ANUAL
     */
    @NotBlank(message = "La periodicidad es obligatoria.")
    private String periodicidad;

    /**
     * Identificador UUID del ingeniero responsable de la orden.
     */
    @NotNull(message = "El ingeniero asignado es obligatorio.")
    private UUID identificadorIngeniero;

    /**
     * Lista de identificadores UUID de los equipos del cliente
     * que serán intervenidos en esta orden de trabajo.
     */
    @NotEmpty(message = "Debe seleccionar al menos un equipo.")
    private List<UUID> equipoClienteIds;
}
