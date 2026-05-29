package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.headquarter_response;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.manager_response.ManagerResponse;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.serviceArea_response.ServiceAreaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * DTO de entrada para crear una nueva {@link com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Headquarter}.
 * Contiene la información necesaria para registrar una sede en el sistema,
 * incluyendo detalles de ubicación y la lista opcional de {@link ServiceAreaResponse}.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquarterResponse {

    /**
     * Identificador único de la sede.
     */
    private UUID identificadorSede;

    /**
     * Nombre o denominación de la sede.
     */
    private String nombreSede;

    /**
     * Nombre de la calle en la dirección de la sede.
     */
    private String direccionCalleSede;

    /**
     * Nombre de la carrera en la dirección de la sede.
     */
    private String direccionCarreraSede;

    /**
     * Número de dirección de la sede.
     */
    private String direccionNumeroSede;

    /**
     * Indica si la sede está activa en el sistema.
     * true: sede en operación; false: sede inactiva o cerrada.
     */
    private boolean estadoActivo;

    /**
     * Lista de áreas de servicio asociadas a esta sede.
     * Tipo: {@link List} de {@link ServiceAreaResponse}
     */
    private List<ServiceAreaResponse> serviceAreaList;

    /**
    * Identificador único de la ciudad donde se ubica la sede.
    * Tipo: {@link UUID}
    */
    private ManagerResponse encargado;

    /**
     * Identificador único de la ciudad donde se ubica la sede.
     */
    private String identificadorCiudad;

    /**
     * Identificador único del cliente al que pertenece la sede.
     */
    private String identificadorCliente;
}
