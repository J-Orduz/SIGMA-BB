package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.serviceArea_response;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ClientEquipment;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.manager_response.ManagerResponse;
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
public class ServiceAreaResponse {

    /**
     * Identificador único del área de servicio.
     * Tipo: {@link UUID}
     */
    private UUID identificadorAreaServicio;

    /**
     * Nombre o denominación del área de servicio.
     */
    private String nombreAreaServicio;

    /**
     * Indica si el área de servicio está activa en el sistema.
     * true: área en operación; false: área inactiva o desactivada.
     */
    private boolean estadoActivo;

    /**
     * Lista de equipos de cliente asociados a esta área de servicio.
     * Tipo: {@link List} de {@link ClientEquipment}
     */
    private List<ClientEquipment> clientEquipmentList;

    /**
     * Indica si el área de servicio está activa en el sistema.
     * true: área en operación; false: área inactiva o desactivada.
     */
    private ManagerResponse encargado;
}
