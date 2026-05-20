package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa una persona en el sistema.
 *
 * Mapea la tabla "persona" en la base de datos y contiene la
 * información básica de identificación, nombres, apellidos,
 * tipo de persona y su estado, así como sus relaciones con
 * teléfonos y correos electrónicos.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persona")
public class PersonEntity {

    /**
     * Identificador único de la persona.
     *
     * Representa la clave primaria en la base de datos (k_identificador)
     * y se maneja como un UUID.
     */
    @Id
    @Column(name = "k_identificador", nullable = false, unique = true)
    private UUID identificador;

    /**
     * Número de cédula de la persona.
     *
     * Es un campo obligatorio y único que identifica de manera
     * oficial a la persona.
     */
    @NotBlank
    @Column(name = "k_cedula", nullable = false, unique = true)
    private String cedula;

    /**
     * Primer nombre de la persona.
     *
     * Es un campo obligatorio y no puede estar vacío.
     */
    @NotBlank
    @Column(name = "n_primer_nombre", nullable = false)
    private String primerNombre;

    /**
     * Segundo nombre de la persona.
     *
     * Este campo es opcional y puede ser nulo.
     */
    @Column(name = "n_segundo_nombre")
    private String segundoNombre;

    /**
     * Primer apellido de la persona.
     *
     * Es un campo obligatorio y no puede estar vacío.
     */
    @NotBlank
    @Column(name = "n_primer_apellido", nullable = false)
    private String primerApellido;

    /**
     * Segundo apellido de la persona.
     *
     * Es un campo obligatorio y no puede estar vacío.
     */
    @NotBlank
    @Column(name = "n_segundo_apellido", nullable = false)
    private String segundoApellido;

    /**
     * Tipo de persona.
     *
     * Es un campo obligatorio que indica la clasificación de la persona
     * (por ejemplo: NATURAL o JURIDICA).
     */
    @NotBlank
    @Column(name = "t_tipo_persona", nullable = false)
    private String tipoPersona;

    /**
     * Segundo tipo o clasificación adicional de la persona.
     *
     * Este campo es opcional y permite complementar la clasificación principal.
     */
    @Column(name = "t_segundo_tipo_persona")
    private String segundoTipoPersona;

    /**
     * Indica si la persona se encuentra activa.
     *
     * Por defecto su valor es true. Se utiliza para manejar el estado
     * lógico del registro sin eliminarlo físicamente.
     */
    @Column(name = "b_estado_activo", nullable = false)
    private boolean estadoActivo = true;

    /**
     * Lista de teléfonos asociados a la persona.
     *
     * Define una relación uno a muchos (OneToMany), donde una persona
     * puede tener múltiples teléfonos. Las operaciones se propagan
     * (cascade ALL) y los registros huérfanos son eliminados.
     */
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhonePersonEntity> phonePersonList;

    /**
     * Lista de correos electrónicos asociados a la persona.
     *
     * Define una relación uno a muchos (OneToMany), donde una persona
     * puede tener múltiples correos. Las operaciones se propagan
     * (cascade ALL) y los registros huérfanos son eliminados.
     */
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailPersonEntity> emailPersonList;
}
