package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.output.comunication.respose;

import java.util.UUID;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonComunicationResponse {


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
     * Indica si la persona está activa en el sistema.
     */
    private boolean estadoActivo;

}
