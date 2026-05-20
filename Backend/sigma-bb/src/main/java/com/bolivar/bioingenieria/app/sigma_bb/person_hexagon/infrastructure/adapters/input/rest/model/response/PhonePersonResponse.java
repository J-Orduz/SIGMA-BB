package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.response;

import lombok.*;

import java.util.UUID;

/**
 * DTO de respuesta para representar un teléfono asociado a una persona.
 * Esta clase se utiliza en la capa REST para exponer la información del teléfono
 * de manera estructurada hacia el cliente.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhonePersonResponse {

    /**
     * Identificador único del teléfono de la persona.
     */
    private UUID idTelefonoPersona;

    /**
     * Número telefónico asociado a la persona.
     */
    private String telefonoPersona;

}
