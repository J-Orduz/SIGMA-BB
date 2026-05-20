package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model;

import lombok.*;

import java.util.UUID;

/**
 * Modelo de dominio que representa un correo electrónico asociado a una persona.
 * Contiene la información del contacto por correo y su estado de actividad.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailPerson {
    /**
     * Identificador único del correo electrónico de la persona.
     */
    private UUID idCorreoPersona;

    /**
     * Dirección de correo electrónico de la persona.
     */
    private String correoPersona;

    /**
     * Indica si el correo electrónico está activo en el sistema.
     */
    private boolean estadoActivo;
}
