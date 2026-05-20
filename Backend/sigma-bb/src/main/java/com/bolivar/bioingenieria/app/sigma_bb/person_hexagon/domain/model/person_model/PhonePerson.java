package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model;

import lombok.*;

import java.util.UUID;

/**
 * Modelo de dominio que representa un teléfono asociado a una persona.
 * Contiene la información del contacto telefónico y su estado de actividad.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhonePerson {
    /**
     * Identificador único del teléfono de la persona.
     */
    private UUID idTelefonoPersona;

    /**
     * Número telefónico de la persona.
     */
    private String telefonoPersona;

    /**
     * Indica si el teléfono está activo en el sistema.
     */
    private boolean estadoActivo;
}
