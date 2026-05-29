package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * DTO de entrada para crear un correo electrónico asociado a una persona.
 * Se utiliza en la capa REST para recibir la información del correo enviada por el cliente.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "EmailPersonCreateRequest",
        description = "DTO de entrada para crear un correo electrónico asociado a una persona")
public class EmailPersonCreateRequest {

    /**
     * Dirección de correo electrónico de la persona.
     */
    @Schema(description = "Dirección de correo electrónico de la persona",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "admin@gmail.com")
    @NotEmpty(message = "El número de teléfono es obligatorio")
    private String correoPersona;
}
