package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * DTO de entrada para crear un teléfono asociado a una persona.
 * Se utiliza en la capa REST para recibir la información del número telefónico
 * enviada por el cliente.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PhonePersonCreateRequest",
        description = "DTO de entrada para crear un teléfono asociado a una persona")
public class PhonePersonCreateRequest {

    /**
     * Número telefónico de la persona.
     */
    @Schema(description = "Número telefónico de la persona",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "3123120302")
    @NotEmpty(message = "El número de teléfono es obligatorio")
    private String telefonoPersona;
}
