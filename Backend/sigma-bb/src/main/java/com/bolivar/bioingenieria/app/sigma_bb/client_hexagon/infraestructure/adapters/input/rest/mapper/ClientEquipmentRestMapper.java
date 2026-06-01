package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ClientEquipment;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.clientEqupment_request.ClientEquipmentCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.clientEquipment_response.ClientEquipmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper REST para convertir entre {@link ClientEquipment} y {@link ClientEquipmentResponse}.
 *
 * <p>Este componente se encarga de transformar los DTO de entrada y salida
 * utilizados en la capa REST hacia y desde el modelo de dominio relacionado con los equipos de los clientes.</p>
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientEquipmentRestMapper {

    /**
     * Convierte un {@link ClientEquipmentCreateRequest} en un {@link ClientEquipment}.
     *
     * @param clientEquipmentCreateRequest DTO de entrada con los datos del equipo del cliente
     * @return {@link ClientEquipment} construido a partir del DTO
     */
    ClientEquipment toClientEquipment(ClientEquipmentCreateRequest clientEquipmentCreateRequest);

    /**
     * Convierte un {@link ClientEquipment} en un {@link ClientEquipmentResponse}.
     *
     * @param clientEquipment modelo de dominio a convertir
     * @return {@link ClientEquipmentResponse} con la información del equipo del cliente
     */
    ClientEquipmentResponse toClientEquipmentResponse(ClientEquipment clientEquipment);

    /**
     * Convierte una lista de {@link ClientEquipment} en una lista de {@link ClientEquipmentResponse}.
     *
     * @param clientEquipmentList lista de modelos de dominio a convertir
     * @return lista de {@link ClientEquipmentResponse}
     */
    List<ClientEquipmentResponse> toClientEquipmentResponseList(List<ClientEquipment> clientEquipmentList);
}
