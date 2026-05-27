package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.application.ports.input.request.PersonCreateRequestUseCase;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.Person;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.request.PersonCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.request.PersonUpdateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.input.rest.model.response.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapeador REST para transformar información relacionada con personas.
 * Convierte entre los DTOs de entrada y salida de la capa REST y el modelo de dominio {@link Person}.
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PersonRestMapper {

    /**
     * Convierte un DTO de {@link PersonCreateRequest} a su modelo de dominio correspondiente.
     *
     * @param request Datos de la persona recibidos desde la capa REST
     * @return Objeto de dominio {@link Person} listo para ser utilizado en la lógica de negocio
     */
    Person toPerson(PersonCreateRequest request);

    /**
     * Convierte un DTO de {@link PersonCreateRequest} a su modelo de caso de uso correspondiente.
     *
     * @param personCreateRequest Datos de la persona recibidos desde la capa REST
     * @return Objeto de caso de uso {@link PersonCreateRequestUseCase} listo para ser utilizado en la lógica de negocio
     */
    PersonCreateRequestUseCase toPersonCreateRequestUseCase(PersonCreateRequest personCreateRequest);

    /**
     * Convierte un DTO de {@link PersonUpdateRequest} a su modelo de dominio correspondiente.
     *
     * @param request Datos actualizados de la persona recibidos desde la capa REST
     * @return Objeto de dominio {@link Person} listo para ser utilizado en la lógica de negocio
     */
    Person tuUpdatePerson(PersonUpdateRequest request);

    /**
     * Convierte un modelo de dominio {@link Person} a su representación de respuesta REST.
     *
     * @param person Objeto de dominio con la información de la persona
     * @return DTO de respuesta {@link PersonResponse} para exponer al cliente
     */
    PersonResponse toPersonResponse(Person person);

    /**
     * Convierte una lista de modelos de dominio {@link Person} a una lista de respuestas REST.
     *
     * @param personList Lista de personas en el modelo de dominio
     * @return Lista de DTOs de respuesta {@link PersonResponse}
     */
    List<PersonResponse> toPersonResponseList(List<Person> personList);
}
