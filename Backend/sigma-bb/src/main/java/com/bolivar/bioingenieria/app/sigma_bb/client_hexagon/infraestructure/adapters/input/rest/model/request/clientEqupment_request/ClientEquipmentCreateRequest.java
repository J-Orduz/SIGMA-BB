package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.clientEqupment_request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ClientEquipmentCreateRequest",
        description = "DTO de entrada para crear un nuevo equipo de cliente. " +
                "Contiene la información necesaria para registrar un equipo en el sistema, " +
                "incluyendo detalles del equipo, estado activo y los servicios asociados.")
@ToString
public class ClientEquipmentCreateRequest {

    /**
     * Número de serie del equipo.
     * Identificador único a nivel de fabricante.
     */
    @Schema(description = "Número de serie del equipo. Identificador único a nivel de fabricante.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "SN123456789")
    @NotEmpty(message = "El número de serie del equipo es obligatorio")
    private String serie;

    /**
     * Fecha de compra del equipo.
     * Tipo: {@link Date}
     */
    @Schema(description = "Fecha de compra del equipo. Tipo: Date",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "2023-01-15")
    private Date fechaCompra;

    /**
     * Valor monetario de compra del equipo.
     * Expresado en unidades monetarias (pesos, dólares, etc.).
     */
    @Schema(description = "Valor monetario de compra del equipo. Expresado en unidades monetarias (pesos, dólares, etc.)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "1500000")
    private Long valorCompra;

    /**
     * Número de inventario asignado al equipo en la institución.
     */
    @Schema(description = "Número de inventario asignado al equipo en la institución",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "INV-2023-001")
    private String numeroInventario;

    /**
     * Identificador único del modelo del equipo.
     */
    @Schema(description = "Identificador único del modelo del equipo.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private java.util.UUID identificadorModelo;
}
