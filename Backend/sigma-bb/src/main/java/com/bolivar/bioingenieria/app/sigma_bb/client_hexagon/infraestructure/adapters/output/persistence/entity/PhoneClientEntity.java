package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Entidad que representa un teléfono asociado a un cliente en la base de datos.
 *
 * Mapea la tabla "telefono_cliente" y contiene la información del número
 * telefónico, su identificador único, estado activo, así como la relación
 * con el cliente al que pertenece.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "telefono_cliente")
public class PhoneClientEntity {

    /**
     * Identificador único del teléfono del cliente.
     *
     * Representa la clave primaria en la base de datos (k_id_telefono_cliente)
     * y se maneja como un UUID para garantizar su unicidad y facilitar su gestión.
     */
    @Id
    @Column(name = "k_id_telefono_cliente", nullable = false, unique = true)
    private UUID identificadorTelefonoCliente;

    /**
     * Número telefónico asociado al cliente.
     *
     * Es un campo obligatorio que representa el número de teléfono del cliente,
     * el cual debe cumplir con un formato válido según las reglas de negocio
     * (por ejemplo, formato nacional o internacional sin espacios ni separadores).
     */
    @Column(name = "n_telefono_cliente", nullable = false)
    @NotBlank
    private String telefonoCliente;

    /**
     * Indica si el teléfono está activo en el sistema (soft-delete).
     *
     * Es un campo booleano que permite marcar un teléfono como inactivo sin eliminarlo físicamente
     * de la base de datos, facilitando así la gestión de registros históricos y la recuperación
     * de información en caso de ser necesario.
     */
    @Column(name = "b_estado_activo", nullable = false)
    private boolean estadoActivo;

    /**
     * Relación de muchos a uno con la entidad {@link ClientEntity}.
     *
     * Representa la asociación entre un teléfono y el cliente al que pertenece,
     * utilizando la clave foránea "k_id_cliente" para establecer la relación
     * en la base de datos. Esta relación permite acceder a la información del
     * cliente desde el teléfono y viceversa, facilitando la gestión de datos
     * relacionados en el sistema.
     */
    @ManyToOne
    @JoinColumn(name = "k_id_cliente")
    private ClientEntity client;

}
