package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.client_request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * DTO de entrada para crear un nuevo teléfono asociado a un cliente.
 *
 * <p>Representa la información mínima necesaria para registrar un número
 * telefónico vinculado a un {@link com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client}.</p>
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ClientPhoneCreateRequest",
        description = "DTO de entrada para crear un nuevo teléfono asociado a un cliente.")
@ToString
public class PhoneClientCreateRequest {

    /**
     * Número telefónico asociado al cliente.
     * Se espera un valor en formato nacional o internacional sin espacios ni separadores.
     */
    @Schema(description = "Número telefónico asociado al cliente",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "3123120302")
    @NotEmpty(message = "El número de teléfono del cliente es obligatorio")
    private String telefonoCliente;
}
