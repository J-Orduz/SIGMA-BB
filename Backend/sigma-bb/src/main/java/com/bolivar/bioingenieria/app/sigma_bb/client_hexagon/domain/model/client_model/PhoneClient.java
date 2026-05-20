package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model;

import lombok.*;

import java.util.UUID;

/**
 * Modelo de dominio que representa un teléfono asociado a un cliente.
 * Contiene el número telefónico, su identificador y el estado lógico de actividad.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhoneClient {

    /**
     * Identificador único del teléfono del cliente.
     */
    private UUID identificadorTelefonoCliente;

    /**
     * Número telefónico asociado al cliente.
     */
    private String telefonoCliente;

    /**
     * Indica si el teléfono está activo en el sistema (soft-delete).
     */
    private boolean estadoActivo;
}
