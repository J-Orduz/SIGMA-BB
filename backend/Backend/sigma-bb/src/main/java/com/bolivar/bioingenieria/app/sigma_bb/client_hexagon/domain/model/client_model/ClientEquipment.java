package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model;

import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * Modelo de dominio que representa un equipo asociado a un cliente.
 * Contiene toda la información relacionada con la compra, identificación
 * y estado del equipo dentro del sistema.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientEquipment {

    /**
     * Identificador único del equipo del cliente.
     * Tipo: {@link UUID}
     */
    private UUID identificadorEquipoCliente;

    /**
     * Número de serie del equipo.
     * Identificador único a nivel de fabricante.
     */
    private String serie;

    /**
     * Fecha de compra del equipo.
     * Tipo: {@link Date}
     */
    private Date fechaCompra;

    /**
     * Valor monetario de compra del equipo.
     * Expresado en unidades monetarias (pesos, dólares, etc.).
     */
    private Long valorCompra;

    /**
     * Número de inventario asignado al equipo en la institución.
     */
    private String numeroInventario;

    /**
     * Indica si el equipo está activo en el sistema.
     * true: equipo en servicio; false: equipo inactivo o retirado.
     */
    private boolean estadoActivo;

    /**
     * Identificador único del modelo del equipo.
     * Referencia a la entidad {@link UUID} que representa el modelo.
     */
    private UUID identificadorModelo;

    /**
     * Identificador único del área de servicio responsable del equipo.
     * Referencia a la entidad {@link ServiceArea} que representa el área de servicio.
     */
    private UUID identificadorAreaServicio;
}
