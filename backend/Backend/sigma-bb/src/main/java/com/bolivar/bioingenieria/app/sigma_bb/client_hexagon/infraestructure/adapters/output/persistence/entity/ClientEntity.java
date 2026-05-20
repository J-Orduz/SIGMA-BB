package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entidad que representa un cliente en el sistema.
 *
 * Mapea la tabla "cliente" en la base de datos y contiene la
 * información básica de identificación, razón social, tipo de
 * identificación, estado activo, así como sus relaciones con
 * correos electrónicos, teléfonos y sedes.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cliente")
public class ClientEntity {

    /**
     * Identificador único del cliente.
     *
     * Representa la clave primaria en la base de datos (k_id_cliente)
     * y se maneja como un String, que puede ser un número de identificación
     * o cualquier otro formato definido por el negocio.
     */
    @Id
    @Column(name = "k_id_cliente", nullable = false, unique = true)
    private String identificadorCliente;

    /**
     * Tipo de identificación asociado al cliente (por ejemplo: NIT, Cédula, Pasaporte).
     *
     * Es un campo obligatorio que describe la categoría o tipo de documento
     * que se utiliza para identificar al cliente.
     */
    @Column(name = "n_tipo_identificacion", nullable = false)
    @NotBlank
    private String tipoIdentifiacion;

    /**
    * Razón social o nombre comercial del cliente.
    *
    * Es un campo obligatorio que representa el nombre oficial o comercial
    * del cliente, utilizado para su identificación y gestión dentro del sistema.
    */
    @Column(name = "n_razon_social", nullable = false)
    @NotBlank
    private String razonSocial;

    /**
     * Indica si el cliente está activo en el sistema.
     *
     * Por defecto su valor es true. Se utiliza para manejar el estado
     * del cliente sin eliminarlo físicamente de la base de datos (soft-delete).
     */
    @Column(name = "b_estado_activo", nullable = false)
    private boolean estadoActivo = true;

    /**
     * Lista de {@link EmailClientEntity} asociados al cliente.
     *
     * Define una relación uno a muchos (OneToMany) con la entidad
     * EmailClientEntity, donde un cliente puede tener múltiples correos
     * electrónicos asociados. Se utiliza cascade ALL para propagar las
     * operaciones y orphanRemoval para eliminar los correos huérfanos.
     */
    @OneToMany(mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<EmailClientEntity> emailClientList;

    /**
     * Lista de {@link PhoneClientEntity} asociados al cliente.
     *
     * Define una relación uno a muchos (OneToMany) con la entidad
     * PhoneClientEntity, donde un cliente puede tener múltiples teléfonos
     * asociados. Se utiliza cascade ALL para propagar las operaciones y
     * orphanRemoval para eliminar los teléfonos huérfanos.
     */
    @OneToMany(mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PhoneClientEntity> phoneClientList;

    /**
     * Lista de {@link HeadquarterEntity} asociados al cliente.
     *
     * Define una relación uno a muchos (OneToMany) con la entidad
     * HeadquarterEntity, donde un cliente puede tener múltiples sedes
     * o sucursales asociadas. Se utiliza cascade ALL para propagar las
     * operaciones y orphanRemoval para eliminar las sedes huérfanas.
     */
    @OneToMany(mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<HeadquarterEntity> headquarterList;

    /**
     * País de residencia o ubicación del cliente.
     *
     * Define una relación muchos a uno (ManyToOne) con la entidad
     * CountryEntity, donde múltiples clientes pueden estar asociados a
     * un mismo país. El campo k_id_pais en la tabla cliente actúa como
     * clave foránea que referencia al país.
     */
    @Column(name = "k_id_pais", nullable = false)
    @NotBlank
    private String identificadorPais;

}
