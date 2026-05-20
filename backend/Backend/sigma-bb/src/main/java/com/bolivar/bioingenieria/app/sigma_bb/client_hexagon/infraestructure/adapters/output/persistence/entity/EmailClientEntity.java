package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Entidad JPA que representa la tabla "correo_cliente" en la base de datos.
 * Esta entidad se utiliza para persistir la información de los correos electrónicos asociados a los clientes.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "correo_cliente")
public class EmailClientEntity {

    /**
     * Identificador único del correo del cliente, generado como UUID.
     */
    @Id
    @Column(name = "k_id_correo_cliente", nullable = false, unique = true)
    private UUID identificadorCorreoCliente;

    /**
     * Dirección de correo electrónico del cliente.
     */
    @Column(name = "n_correo_cliente", nullable = false)
    @NotBlank
    private String correoCliente;

    /**
    * Indica si el correo está activo en el sistema (soft delete).
    */
    @Column(name = "b_estado_activo", nullable = false)
    private boolean estadoActivo;

    /**
     * Relación ManyToOne con la entidad {@link ClientEntity}, representando el cliente al que pertenece este correo.
     * La columna "k_id_cliente" es una clave foránea que referencia a la tabla de clientes.
     */
    @ManyToOne
    @JoinColumn(name = "k_id_cliente", nullable = false)
    private ClientEntity client;
}
