package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.serviceArea_request;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ClientEquipment;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Manager;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.clientEqupment_request.ClientEquipmentCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.manager_request.ManagerCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * DTO de entrada para crear un nuevo área de servicio dentro de una sede de cliente.
 * Contiene la información necesaria para registrar un área de servicio en el sistema,
 * incluyendo detalles del área, estado activo y los equipos asociados.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ServiceAreaCreateRequest",
        description = "DTO de entrada para crear un nuevo área de servicio dentro de una sede de cliente. " +
                "Contiene la información necesaria para registrar un área de servicio en el sistema, " +
                "incluyendo detalles del área, estado activo y los equipos asociados.")
@ToString
public class ServiceAreaCreateRequest {

    /**
     * Nombre o denominación del área de servicio.
     */
    @Schema(description = "Nombre o denominación del área de servicio",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Consultorio Medico")
    @NotEmpty(message = "El nombre del área de servicio es obligatorio")
    private String nombreAreaServicio;

    /**
     * Lista de equipos de cliente asociados a esta área de servicio.
     * Tipo: {@link List} de {@link ClientEquipment}
     */
    @Schema(description = "Lista de equipos de cliente asociados a esta área de servicio",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ClientEquipmentCreateRequest> clientEquipmentList;

    /**
     * Encargado responsable del área de servicio.
     * Tipo: {@link Manager}
     */
    @Schema(description = "Encargado responsable del área de servicio",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ManagerCreateRequest> managerList;

}
