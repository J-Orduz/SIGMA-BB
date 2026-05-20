package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.client_responce;

import lombok.*;

import java.util.UUID;

/**
 * DTO de entrada para crear un nuevo correo electrónico asociado a un cliente.
 *
 * <p>Representa la información mínima requerida para registrar una dirección
 * de correo en el sistema vinculada a un {@link com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client}.</p>
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailClientResponse {

    /**
     * Identificador del correo del cliente.
     */
    private UUID identificadorCorreoCliente;

    /**
     * Dirección de correo electrónico del cliente.
     * Debe cumplir el formato estándar de e-mail (local@dominio).
     */
    private String correoCliente;

    /**
     * Indica si el correo está activo en el sistema (soft delete).
     */
    private boolean estadoActivo;
}
