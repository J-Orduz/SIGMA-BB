package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.manager_request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO de entrada para crear un nuevo encargado dentro de una sede o Area de servicio de cliente.
 * Contiene la información necesaria para registrar un encargado en el sistema,
 * incluyendo detalles del tipo o rol del encargado en la organización.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ManagerCreateRequest",
        description = "DTO de entrada para crear un nuevo encargado dentro de una sede o Area de servicio de cliente. " +
                "Contiene la información necesaria para registrar un encargado en el sistema, " +
                "incluyendo detalles del tipo o rol del encargado en la organización.")
@ToString
public class ManagerCreateRequest {

    /**
     * Tipo o rol del encargado en la organización.
     * Ejemplos: sede, area_servicio.
     */
    @Schema(description = "Tipo o rol del encargado en el cliente",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "sede")
    private String tipoEncargado;
}
