package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


/**
 * DTO de entrada para actualizar la información de una persona.
 * Se utiliza en la capa REST para recibir los datos modificados enviados por el cliente.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PersonUpdateRequest",
        description = "DTO de entrada para actualizar la información de una persona, incluyendo sus teléfonos y correos electrónicos asociados.")
public class PersonUpdateRequest {

    /**
     * Cédula de la persona.
     */
    @Schema(description = "Número de cédula de la persona",
            example = "123456789",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "La cédula es obligatoria")
    private String cedula;

    /**
     * Primer nombre de la persona.
     */
    @Schema(description = "Primer nombre de la persona",
            example = "Juan",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "El primer nombre es obligatorio")
    private String primerNombre;

    /**
     * Segundo nombre de la persona.
     */
    @Schema(description = "Segundo nombre de la persona",
            example = "Carlos",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String segundoNombre;

    /**
     * Primer apellido de la persona.
     */
    @Schema(description = "Primer apellido de la persona",
            example = "Pérez",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "El primer apellido es obligatorio")
    private String primerApellido;

    /**
     * Segundo apellido de la persona.
     */
    @Schema(description = "Segundo apellido de la persona",
            example = "Gómez",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String segundoApellido;

    /**
     * Tipo principal de la persona.
     */
    @Schema(description = "Tipo principal de la persona",
            example = "representante_legal",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "El tipo de persona es obligatorio")
    private String tipoPersona;

    /**
     * Tipo secundario de la persona, si aplica.
     */
    @Schema(description = "Tipo secundario de la persona, si aplica",
            example = "encargado",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String segundoTipoPersona;

    /**
     * Lista de teléfonos asociados a la persona.
     */
    @Schema(description = "Lista de teléfonos asociados a la persona",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private java.util.List<PhonePersonCreateRequest> phonePersonList;

    /**
     * Lista de correos electrónicos asociados a la persona.
     */
    @Schema(description = "Lista de correos electrónicos asociados a la persona",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private java.util.List<EmailPersonCreateRequest> emailPersonList;

}
