package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa una sede o sucursal de un cliente en el sistema.
 *
 * Mapea la tabla "sede" en la base de datos y contiene la información
 * relacionada con la ubicación, estado activo, así como sus relaciones
 * con áreas de servicio y el cliente al que pertenece.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sede")
public class HeadquarterEntity {

    /**
     * Identificador único de la sede.
     *
     * Este campo representa la clave primaria de la entidad en la base de datos
     * (k_id_sede) y se genera como un UUID.
     */
    @Id
    @Column(name = "k_id_sede", nullable = false, unique = true)
    private UUID identificadorSede;

    /**
     * Nombre o denominación de la sede.
     *
     * Este campo es obligatorio y no puede estar vacío, representando
     * el nombre oficial o comercial de la sede.
     */
    @Column(name = "n_nombre_sede", nullable = false)
    @NotBlank
    private String nombreSede;

    /**
     * Nombre de la calle en la dirección de la sede.
     *
     * Este campo es obligatorio y no puede estar vacío, representando
     * el nombre de la calle donde se ubica la sede.
     */
    @Column(name = "dir_calle", nullable = false)
    @NotBlank
    private String direccionCalleSede;

    /**
     * Nombre de la carrera en la dirección de la sede.
     *
     * Este campo es obligatorio y no puede estar vacío, representando
     * el nombre de la carrera donde se ubica la sede.
     */
    @Column(name = "dir_carrera", nullable = false)
    @NotBlank
    private String direccionCarreraSede;

    /**
     * Número de dirección de la sede.
     *
     * Este campo es obligatorio y no puede estar vacío, representando
     * el número o referencia adicional para la ubicación de la sede.
     */
    @Column(name = "dir_num", nullable = false)
    @NotBlank
    private String direccionNumeroSede;

    /**
     * Indica si la sede está activa en el sistema.
     *
     * Por defecto su valor es true. Se utiliza para manejar el estado
     * lógico del registro sin eliminarlo físicamente.
     */
    @Column(name = "b_estado_activo", nullable = false)
    private boolean estadoActivo;

    /**
     * Lista de {@link ServiceAreaEntity} asociadas a esta sede.
     *
     * Define una relación uno a muchos (OneToMany) con la entidad {@link ServiceAreaEntity},
     * donde una sede puede tener múltiples áreas de servicio asociadas.
     */
    @OneToMany(mappedBy = "headquarter",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ServiceAreaEntity> serviceAreaList;

    /**
     * Encargado o responsable de la sede.
     *
     * Define una relación uno a uno (OneToOne) con la entidad {@link ManagerEntity},
     * donde cada sede tiene asignado un único encargado. La eliminación en cascada
     * asegura que al eliminar una sede, también se elimine el encargado relacionado.
     */
    @OneToMany(mappedBy = "headquarter",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ManagerEntity> managerList;

    /**
     * Referencia al {@link ClientEntity} al que pertenece esta sede.
     *
     * Define una relación muchos a uno (ManyToOne), donde múltiples sedes
     * pueden estar asociadas a un mismo cliente.
     */
    @ManyToOne
    @JoinColumn(name = "k_id_cliente", nullable = false)
    private ClientEntity client;

    /**
     * Identificador único de la ciudad donde se ubica la sede.
     *
     * Define una relación muchos a uno (ManyToOne) con la entidad {CityEntity},
     * donde múltiples sedes pueden estar asociadas a una misma ciudad. El campo
     * k_id_ciudad en la tabla sede actúa como clave foránea que referencia a la ciudad.
     */
    @Column(name = "k_id_ciudad", nullable = false)
    @NotBlank
    private String identificadorCiudad;
}
