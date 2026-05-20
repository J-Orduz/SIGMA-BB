package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.client_responce;

import lombok.*;

/**
 * DTO de entrada para crear un nuevo teléfono asociado a un cliente.
 *
 * <p>Representa la información mínima necesaria para registrar un número
 * telefónico vinculado a un {@link com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client}.</p>
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneClientResponse {

    /**
     * Identificador único del teléfono del cliente.
     */
    private String identificadorTelefonoCliente;

    /**
     * Número telefónico asociado al cliente.
     * Se espera un valor en formato nacional o internacional sin espacios ni separadores.
     */
    private String telefonoCliente;

    /**
     * Indica si el teléfono está activo en el sistema (soft-delete).
     */
    private boolean estadoActivo;
}
