package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.clientEquipment_response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * DTO de salida para representar la información de un equipo de cliente.
 *
 * <p>Contiene los detalles relevantes de un {@link com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ClientEquipment},
 * incluyendo su identificación, características y estado.</p>
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientEquipmentResponse {

    /**
     * Identificador único del equipo de cliente.
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
}
