package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.EmailClient;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.client_request.EmailClientCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.client_responce.EmailClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper REST para convertir entre {@link EmailClient} y {@link EmailClientResponse}.
 *
 * <p>Este componente se encarga de transformar los DTO de entrada y salida
 * utilizados en la capa REST hacia y desde el modelo de dominio relacionado con los correos electrónicos de los clientes.</p>
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailClientRestMapper {

    /**
     * Convierte un {@link EmailClientCreateRequest} en un {@link EmailClient}.
     *
     * @param emailPersonCreateRequest DTO de entrada con los datos del correo electrónico del cliente
     * @return {@link EmailClient} construido a partir del DTO
     */
    EmailClient toEmailClient(EmailClientCreateRequest emailPersonCreateRequest);

    /**
     * Convierte un {@link EmailClient} en un {@link EmailClientResponse}.
     *
     * @param emailClient modelo de dominio a convertir
     * @return {@link EmailClientResponse} con la información del correo electrónico del cliente
     */
    EmailClientResponse toEmailClientResponse(EmailClient emailClient);

    /**
     * Convierte una lista de {@link EmailClient} en una lista de {@link EmailClientResponse}.
     *
     * @param emailClientList lista de modelos de dominio a convertir
     * @return lista de {@link EmailClientResponse}
     */
    List<EmailClientResponse> toEmailClientResponseList(List<EmailClient> emailClientList);
}
