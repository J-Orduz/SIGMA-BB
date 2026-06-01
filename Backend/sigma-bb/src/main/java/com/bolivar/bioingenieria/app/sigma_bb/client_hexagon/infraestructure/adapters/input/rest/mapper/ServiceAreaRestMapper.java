package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ServiceArea;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.serviceArea_request.ServiceAreaCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.serviceArea_response.ServiceAreaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper REST para convertir entre {@link ServiceArea} y {@link ServiceAreaResponse}.
 *
 * <p>Este componente se encarga de transformar los DTO de entrada y salida
 * utilizados en la capa REST hacia y desde el modelo de dominio relacionado con las áreas de servicio de los clientes.</p>
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceAreaRestMapper {

    /**
     * Convierte un {@link ServiceAreaCreateRequest} en un {@link ServiceArea}.
     *
     * @param serviceAreaCreateRequest DTO de entrada con los datos del área de servicio
     * @return {@link ServiceArea} construido a partir del DTO
     */
    ServiceArea toServiceArea(ServiceAreaCreateRequest serviceAreaCreateRequest);

    /**
     * Convierte un {@link ServiceArea} en un {@link ServiceAreaResponse}.
     *
     * @param serviceArea modelo de dominio a convertir
     * @return {@link ServiceAreaResponse} con la información del área de servicio
     */
    ServiceAreaResponse toServiceAreaResponse(ServiceArea serviceArea);

    /**
     * Convierte una lista de {@link ServiceArea} en una lista de {@link ServiceAreaResponse}.
     *
     * @param serviceAreaList lista de modelos de dominio a convertir
     * @return lista de {@link ServiceAreaResponse}
     */
    List<ServiceAreaResponse> toServiceAreaResponseList(List<ServiceArea> serviceAreaList);
}
