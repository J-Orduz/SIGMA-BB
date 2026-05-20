package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.response;

import lombok.*;

import java.util.UUID;

/**
 * DTO de respuesta para representar un correo electrónico asociado a una persona.
 * Esta clase se utiliza en la capa REST para exponer la información del correo
 * de manera segura y estructurada hacia el cliente.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailPersonResponse {

    /**
     * Identificador único del correo electrónico de la persona.
     */
    private UUID idCorreoPersona;

    /**
     * Dirección de correo electrónico asociada a la persona.
     */
    private String correoPersona;

}
