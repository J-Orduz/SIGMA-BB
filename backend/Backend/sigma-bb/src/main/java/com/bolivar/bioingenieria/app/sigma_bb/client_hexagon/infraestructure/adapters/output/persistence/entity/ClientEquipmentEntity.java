package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * Entidad JPA que representa la tabla "equipo_cliente" en la base de datos.
 * Esta entidad se utiliza para mapear los datos relacionados con los equipos
 * asociados a los clientes, incluyendo detalles de compra, identificación y estado.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "equipo_cliente")
public class ClientEquipmentEntity {

    /**
     * Identificador único del equipo del cliente.
     * Tipo: String (UUID en formato de texto).
     */
    @Id
    @Column(name = "k_id_equipo_cliente", nullable = false, unique = true)
    private UUID identificadorEquipoCliente;

    /**
     * Número de serie del equipo.
     * Identificador único a nivel de fabricante.
     */
    @Column(name = "k_serie", nullable = false)
    @NotBlank
    private String serie;

    /**
     * Fecha de compra del equipo.
     * Tipo: {@link Date}
     */
    @Column(name = "f_fecha_compra", nullable = false)
    @NotBlank
    private Date fechaCompra;

    /**
     * Valor monetario de compra del equipo.
     * Expresado en unidades monetarias (pesos, dólares, etc.).
     */
    @Column(name = "v_valor_compra", nullable = false)
    @NotBlank
    private Long valorCompra;

    /**
     * Número de inventario asignado al equipo en la institución.
     */
    @Column(name = "n_no_inventario", nullable = false)
    @NotBlank
    private String numeroInventario;

    /**
     * Indica si el equipo está activo en el sistema.
     * true: equipo en servicio; false: equipo inactivo o retirado.
     */
    @Column(name = "b_estado_activo", nullable = false)
    private boolean estadoActivo = true;

    /**
     * Identificador único del modelo del equipo.
     * Referencia a la entidad {@link UUID} que representa el modelo.
     */
    /*
    @ManyToOne
    @JoinColumn(name = "k_id_modelo", nullable = false)
    private UUID modelo;
    */

    /**
     * Área de servicio a la que está asociado el equipo.
     * Relación ManyToOne con la entidad {@link ServiceAreaEntity}.
     */
    @ManyToOne
    @JoinColumn(name = "k_id_area_servicio", nullable = false)
    private ServiceAreaEntity serviceArea;

}
