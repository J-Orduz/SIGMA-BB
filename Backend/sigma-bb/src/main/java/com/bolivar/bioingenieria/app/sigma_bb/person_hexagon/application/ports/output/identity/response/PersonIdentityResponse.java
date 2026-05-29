package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.identity.response;

import lombok.*;

/**
 * DTO de respuesta para la gestión de identidad del usuario.
 * Contiene la información necesaria para crear o gestionar la identidad de una persona en el sistema.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonIdentityResponse {

    /**
     * Nombre de usuario para la identidad de la persona.
     */
    private String userName;

    /**
     * Correo electrónico asociado a la identidad de la persona.
     */
    private String email;

    /**
     * Primer nombre de la persona.
     */
    private String firstName;

    /**
     * Segundo nombre de la persona.
     */
    private String lastName;

    /**
     * Contraseña para la identidad de la persona.
     */
    private String password;

}
