package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.PhoneClient;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.client_request.PhoneClientCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.client_responce.PhoneClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper REST para convertir entre {@link PhoneClient} y {@link PhoneClientResponse}.
 *
 * <p>Este componente se encarga de transformar los DTO de entrada y salida
 * utilizados en la capa REST hacia y desde el modelo de dominio relacionado con los teléfonos de los clientes.</p>
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PhoneClientRestMapper {
    /**
     * Convierte un {@link PhoneClientCreateRequest} en un {@link PhoneClient}.
     *
     * @param clientPhoneCreateRequest DTO de entrada con los datos del teléfono del cliente
     * @return {@link PhoneClient} construido a partir del DTO
     */
    PhoneClient toPhoneClient(PhoneClientCreateRequest phoneClientCreateRequest);

    /**
     * Convierte un {@link PhoneClient} en un {@link PhoneClientResponse}.
     *
     * @param phoneClient modelo de dominio a convertir
     * @return {@link PhoneClientResponse} con la información del teléfono del cliente
     */
    PhoneClientResponse toPhoneClientResponse(PhoneClient phoneClient);

    /**
     * Convierte una lista de {@link PhoneClient} en una lista de {@link PhoneClientResponse}.
     *
     * @param phoneClientList lista de modelos de dominio a convertir
     * @return lista de {@link PhoneClientResponse}
     */
    List<PhoneClientResponse> toPhoneCLientResponseList(List<PhoneClient> phoneClientList);
}
