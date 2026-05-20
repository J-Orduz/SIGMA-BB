package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Entidad que representa los teléfonos asociados a una persona.
 *
 * Mapea la tabla "telefono_persona" en la base de datos y contiene la
 * información relacionada con los números de teléfono vinculados a una persona,
 * incluyendo su identificador, número y estado.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "telefono_persona")
public class PhonePersonEntity {

    /**
     * Identificador único del teléfono asociado a una persona.
     *
     * Representa la clave primaria en la base de datos (k_id_telefono_persona)
     * y se maneja como un UUID.
     */
    @Id
    @Column(name = "k_id_telefono_persona", nullable = false, unique = true)
    private UUID idTelefonoPersona;

    /**
     * Referencia a la {@link PersonEntity} a la que pertenece este teléfono.
     *
     * Define una relación muchos a uno (ManyToOne), donde múltiples teléfonos
     * pueden estar asociados a una misma persona.
     */
    @ManyToOne
    @JoinColumn(name = "k_identificador")
    private PersonEntity person;

    /**
     * Número de teléfono asociado a la persona.
     *
     * Es un campo obligatorio y no puede estar vacío.
     */
    @NotBlank
    @Column(name = "n_telefono_persona", nullable = false)
    private String telefonoPersona;

    /**
     * Indica si el teléfono se encuentra activo.
     *
     * Por defecto su valor es true. Se utiliza para manejar el estado
     * lógico del registro sin eliminarlo físicamente.
     */
    @Column(name = "b_estado_activo", nullable = false)
    private boolean estadoActivo = true;
}
