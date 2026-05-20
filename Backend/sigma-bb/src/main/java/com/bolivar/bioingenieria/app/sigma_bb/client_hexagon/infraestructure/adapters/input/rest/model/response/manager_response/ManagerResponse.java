package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.manager_response;

import lombok.*;

import java.util.UUID;

/**
 * DTO de salida para representar la información de un encargado o responsable dentro del sistema.
 *
 * <p>Contiene los detalles relevantes de un {@link com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Manager},
 * incluyendo su identificación, tipo o rol, y estado activo.</p>
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerResponse {

    /**
     * Identificador único del encargado.
     * Tipo: {@link UUID}
     */
    private UUID identificadorEncargado;

    /**
     * Tipo o rol del encargado en la organización.
     * Ejemplos: sede, area_servicio.
     */
    private String tipoEncargado;

    /**
     * Indica si el encargado está activo en el sistema.
     * true: encargado activo; false: encargado inactivo o removido.
     */
    private boolean estadoActivo;
}
