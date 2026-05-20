package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.client_request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * DTO de entrada para crear un nuevo correo electrónico asociado a un cliente.
 *
 * <p>Representa la información mínima requerida para registrar una dirección
 * de correo en el sistema vinculada a un {@link com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client}.</p>
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ClientEmailCreateRequest",
        description = "DTO de entrada para crear un nuevo correo electrónico asociado a un cliente.")
@ToString
public class EmailClientCreateRequest {

    /**
     * Dirección de correo electrónico del cliente.
     * Debe cumplir el formato estándar de e-mail (local@dominio).
     */
    @Schema(description = "Dirección de correo electrónico del cliente",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "usuario@ejemplo.com")
    @NotEmpty(message = "El correo electrónico del cliente es obligatorio")
    private String correoCliente;
}
