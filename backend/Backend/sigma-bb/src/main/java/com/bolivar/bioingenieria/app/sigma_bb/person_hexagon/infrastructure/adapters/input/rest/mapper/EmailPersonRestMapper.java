package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.EmailPerson;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.request.EmailPersonCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.response.EmailPersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapeador REST para conversiones entre el modelo de dominio {@link EmailPerson}
 * y los objetos usados en la capa de entrada REST (requests/responses).
 *
 * Utiliza MapStruct para generar las implementaciones en tiempo de compilación.
 * La política {@code unmappedSourcePolicy = ReportingPolicy.IGNORE} evita errores
 * cuando hay campos en la fuente que no se mapean explícitamente.
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EmailPersonRestMapper {

    /**
     * Convierte una petición de creación {@link EmailPersonCreateRequest} al
     * modelo de dominio {@link EmailPerson}.
     *
     * @param request Request recibido por la API que contiene los datos del correo
     * @return Instancia de {@link EmailPerson} con los datos mapeados
     */
    EmailPerson toEmailPerson(EmailPersonCreateRequest request);

    /**
     * Convierte el modelo de dominio {@link EmailPerson} a la respuesta REST
     * {@link EmailPersonResponse} que será enviada al cliente.
     *
     * @param emailPerson Modelo de dominio con la información del correo
     * @return Objeto {@link EmailPersonResponse} listo para serializar en la respuesta
     */
    EmailPersonResponse toEmailPersonResponse(EmailPerson emailPerson);

    /**
     * Convierte una lista de modelos de dominio {@link EmailPerson} a una lista
     * de objetos de respuesta {@link EmailPersonResponse}.
     *
     * @param emailPersonList Lista de modelos de dominio a convertir
     * @return Lista de respuestas {@link EmailPersonResponse}
     */
    List<EmailPersonResponse> toEmailPersonResponseList(List<EmailPerson> emailPersonList);
}
