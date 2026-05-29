package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.request;

import lombok.*;

import java.util.List;

/**
 * DTO de solicitud para la creación de una persona.
 * Contiene toda la información necesaria para crear una persona en el sistema, incluyendo datos personales, tipos de persona, identidad y contactos asociados.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonCreateRequestUseCase {

    /**
     * Cédula de la persona.
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
     * Tipo principal de la persona.
     */
    private String tipoPersona;

    /**
     * Tipo secundario de la persona, si aplica.
     */
    private String segundoTipoPersona;

    /**
     * Nombre de usuario para la identidad de la persona.
     */
    private String nombreUsuario;

    /**
     * Correo electrónico asociado a la identidad de la persona.
     */
    private String password;

    /**
     * Lista de teléfonos asociados a la persona.
     */
    private List<PhonePersonCreateRequestUseCase> phonePersonList;

    /**
     * Lista de correos electrónicos asociados a la persona.
     */
    private List<EmailPersonCreateRequestUseCase> emailPersonList;
}
