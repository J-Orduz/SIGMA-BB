package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.headquarter_request;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.manager_request.ManagerCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.serviceArea_request.ServiceAreaCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

/**
 * DTO de entrada para crear una nueva {@link com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Headquarter}.
 * Contiene la información necesaria para registrar una sede en el sistema,
 * incluyendo detalles de ubicación y la lista opcional de {@link ServiceAreaCreateRequest}.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HeadquarterCreateRequest",
        description = "DTO de entrada para crear una nueva sede o sucursal de un cliente. " +
                "Contiene la información necesaria para registrar una sede en el sistema, " +
                "incluyendo detalles de ubicación y estado activo.")
@ToString
public class HeadquarterCreateRequest {

    /**
     * Nombre o denominación de la sede.
     */
    @Schema(description = "Nombre o denominación de la sede",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Sede Principal")
    @NotEmpty(message = "El nombre de la sede es obligatorio")
    private String nombreSede;

    /**
     * Nombre de la calle en la dirección de la sede.
     */
    @Schema(description = "Nombre de la calle en la dirección de la sede",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Calle 123")
    @NotEmpty(message = "El numero de la calle de la sede es obligatorio")
    private String direccionCalleSede;

    /**
     * Nombre de la carrera en la dirección de la sede.
     */
    @Schema(description = "Nombre de la carrera en la dirección de la sede",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Carrera 45")
    @NotEmpty(message = "El numero de la carrera de la sede es obligatorio")
    private String direccionCarreraSede;

    /**
     * Número de dirección de la sede.
     */
    @Schema(description = "Número de dirección de la sede",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Número 67")
    @NotEmpty(message = "El numero de la sede es obligatorio")
    private String direccionNumeroSede;

    /**
     * Lista de áreas de servicio asociadas a esta sede.
     * Tipo: {@link List} de {@link ServiceAreaCreateRequest}
     */
    @Schema(description = "Lista de áreas de servicio asociadas a esta sede",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ServiceAreaCreateRequest> serviceAreaList;

    /**
     * Encargado responsable de la sede.
     * Tipo: {@link ManagerCreateRequest}
     */
    @Schema(description = "Encargado responsable de la sede",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ManagerCreateRequest> managerList;

    /**
     * Identificador único de la ciudad donde se ubica la sede.
     * Este campo es obligatorio y no puede estar vacío, representando
     * el código o identificador de la ciudad en el sistema.
     */
    @Schema(description = "Identificador único de la ciudad donde se ubica la sede",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "BOG")
    @NotEmpty(message = "El identificador de la ciudad es obligatorio")
    private String identificadorCiudad;
}
