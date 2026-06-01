package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Client;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.client_request.ClientCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.client_responce.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper REST para convertir entre {@link ClientCreateRequest}, {@link Client}
 * y {@link ClientResponse}.
 *
 * <p>Este componente se encarga de transformar los DTO de entrada y salida
 * utilizados en la capa REST hacia y desde el modelo de dominio.</p>
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientRestMapper {

    /**
     * Convierte un {@link ClientCreateRequest} en un {@link Client}.
     *
     * @param clientCreateRequest DTO de entrada con los datos del cliente
     * @return {@link Client} construido a partir del DTO
     */
    Client toClient(ClientCreateRequest clientCreateRequest);

    /**
     * Convierte un {@link Client} en un {@link ClientResponse}.
     *
     * @param client modelo de dominio a convertir
     * @return {@link ClientResponse} con la información del cliente
     */
    @Mapping(target = "correosCliente", source = "emailClientList")
    @Mapping(target = "telefonosCliente", source = "phoneClientList")
    @Mapping(target = "headquarterClientList", source = "headquarterList")
    ClientResponse toClientResponse(Client client);

    /**
     * Convierte una lista de {@link Client} en una lista de {@link ClientResponse}.
     *
     * @param clientList lista de modelos de dominio a convertir
     * @return lista de {@link ClientResponse}
     */
    List<ClientResponse> toClientResponseList(List<Client> clientList);
}
