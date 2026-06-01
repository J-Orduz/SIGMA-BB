package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * DTO de respuesta para representar la información completa de una persona.
 * Esta clase se utiliza en la capa REST para exponer los datos de la persona
 * junto con sus correos electrónicos y teléfonos asociados.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {

    /**
     * Identificador único de la persona.
     */
    private UUID identificador;

    /**
     * Número de cédula de la persona.
     */
    private String cedula;

    /**
     * Primer nombre de la persona.
     */
    private String primerNombre;

    /**
     * Segundo nombre de la persona.
     */
    private String segundoNombre;

    /**
     * Primer apellido de la persona.
     */
    private String primerApellido;

    /**
     * Segundo apellido de la persona.
     */
    private String segundoApellido;

    /**
     * Tipo principal de persona.
     */
    private String tipoPersona;

    /**
     * Tipo secundario de persona, si aplica.
     */
    private String segundoTipoPersona;

    /**
     * Indica si la persona se encuentra activa.
     */
    private boolean estadoActivo;

    /**
     * Lista de correos electrónicos asociados a la persona.
     */
    private List<EmailPersonResponse> emailPersonList;

    /**
     * Lista de teléfonos asociados a la persona.
     */
    private List<PhonePersonResponse> phonePersonList;
}
