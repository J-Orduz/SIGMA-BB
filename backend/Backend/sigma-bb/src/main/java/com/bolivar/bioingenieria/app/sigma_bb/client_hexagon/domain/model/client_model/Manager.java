package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model;

import lombok.*;

import java.util.UUID;

/**
 * Modelo de dominio que representa a una persona encargada de administrar
 * o supervisar un cliente en el sistema.
 * Contiene la información de identificación y el estado de actividad del encargado.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Manager {
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
