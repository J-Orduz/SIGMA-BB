package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Entidad que representa los correos electrónicos asociados a una persona.
 *
 * Mapea la tabla "correo_persona" en la base de datos y contiene la
 * información relacionada con los emails vinculados a una persona,
 * incluyendo su identificador, dirección de correo y estado.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "correo_persona")
public class EmailPersonEntity {

    /**
     * Identificador único del correo asociado a una persona.
     *
     * Este campo representa la clave primaria de la entidad en la base de datos
     * (k_id_correo_persona) y se genera como un UUID.
     */
    @Id
    @Column(name = "k_id_correo_persona", nullable = false, unique = true)
    private UUID idCorreoPersona;

    /**
     * Referencia a la persona a la que pertenece este correo electrónico.
     *
     * Define una relación muchos a uno (ManyToOne), donde múltiples correos
     * pueden estar asociados a una misma persona.
     */
    @ManyToOne
    @JoinColumn(name = "k_identificador")
    private PersonEntity person;

    /**
     * Correo electrónico de la persona.
     *
     * Este campo es obligatorio y no puede estar vacío, representando
     * la dirección de email asociada a la persona.
     */
    @NotBlank
    @Column(name = "n_correo_persona", nullable = false)
    private String correoPersona;

    /**
     * Indica si el correo electrónico se encuentra activo.
     *
     * Por defecto su valor es true. Se utiliza para manejar el estado
     * lógico del registro sin eliminarlo físicamente.
     */
    @Column(name = "b_estado_activo", nullable = false)
    private boolean estadoActivo = true;
}
