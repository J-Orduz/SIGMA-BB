package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.client_request;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.headquarter_request.HeadquarterCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

/**
 * DTO de entrada para crear un nuevo {@link com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client}.
 * Contiene los datos básicos del cliente y listas opcionales de teléfonos y correos.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ClienteCreateRequest",
        description = "DTO de entrada para crear un nuevo cliente, incluyendo sus teléfonos y correos electrónicos asociados.")
@ToString
public class ClientCreateRequest {

    /**
     * Identificador único del cliente.
     * Normalmente es el NIT o el código fiscal/tributario que identifica al cliente en el negocio.
     * @see #tipoIdentifiacion
     */
    @Schema(description = "Identificador único del cliente, como NIT o código definido por el negocio",
            example = "900123456-7",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "El identificador del cliente es obligatorio")
    private String identificadorCliente;

    /**
     * Tipo de identificación del cliente.
     * Valores esperados: por ejemplo "NIT", "Cédula", "Pasaporte".
     */
    @Schema(description = "Tipo de identificación del cliente (por ejemplo: 'NIT_juridico', 'NIT_natural', 'CC', 'CE')",
            example = "NIT_juridico",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "El tipo de identificación es obligatorio")
    private String tipoIdentifiacion;

    /**
     * Razón social o nombre comercial del cliente.
     */
    @Schema(description = "Razón social o nombre comercial del cliente",
            example = "Empresa XYZ S.A.S.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "La razón social es obligatoria")
    private String razonSocial;

    /**
     * Lista de teléfonos asociados al cliente.
     * Cada elemento es un {@link PhoneClientCreateRequest} con los datos del teléfono a almacenar.
     */
    @Schema(description = "Lista de teléfonos asociados al cliente",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<PhoneClientCreateRequest> phoneClientList;

    /**
     * Lista de correos electrónicos asociados al cliente.
     * Cada elemento es un {@link EmailClientCreateRequest} con los datos del correo a almacenar.
     */
    @Schema(description = "Lista de correos electrónicos asociados al cliente",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<EmailClientCreateRequest> emailClientList;

    /**
     * Lista de sedes o sucursales asociadas al cliente.
     * Cada elemento es un {@link HeadquarterCreateRequest} con los datos de la sede a almacenar.
     */
    @Schema(description = "Lista de sedes o sucursales asociadas al cliente",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<HeadquarterCreateRequest> headquarterList;

    /**
     * Identificador del país asociado al cliente.
     * Este campo es importante para determinar la moneda, formato de dirección y otros aspectos regionales.
     * Se espera un código de país estándar (por ejemplo: "COL" para Colombia, "USA" para Estados Unidos).
     */
    @Schema(description = "Identificador del país asociado al cliente, utilizado para determinar la moneda y otros aspectos regionales",
            example = "COL",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "El identificador del país es obligatorio")
    private String identificadorPais;
}
