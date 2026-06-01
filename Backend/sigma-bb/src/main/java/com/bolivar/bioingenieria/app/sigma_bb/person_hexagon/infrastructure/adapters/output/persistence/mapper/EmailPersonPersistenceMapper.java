package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.EmailPerson;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.entity.EmailPersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Interfaz mapeadora para convertir entre el modelo de dominio {@link EmailPerson}
 * y su representación de persistencia {@link EmailPersonEntity}.
 *
 * Utiliza MapStruct para generar automáticamente las implementaciones
 * de conversión entre estas capas arquitectónicas.
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailPersonPersistenceMapper {

    /**
     * Convierte un modelo de dominio {@link EmailPerson} a su representación de entidad {@link EmailPersonEntity}.
     *
     * @param emailPerson Objeto de dominio con la información del correo
     * @return Entidad {@link EmailPersonEntity} lista para persistencia
     */
    EmailPersonEntity toEmailPersonEntity(EmailPerson emailPerson);

    /**
     * Convierte una entidad de persistencia {@link EmailPersonEntity} a su modelo de dominio {@link EmailPerson}.
     *
     * @param emailPersonEntity Entidad de persistencia con la información del correo
     * @return Modelo de dominio {@link EmailPerson} con los datos recuperados
     */
    EmailPerson toEmailPerson(EmailPersonEntity emailPersonEntity);

    /**
     * Convierte una lista de entidades de persistencia {@link EmailPersonEntity} a una lista
     * de modelos de dominio {@link EmailPerson}.
     *
     * @param emailPersonEntities Lista de entidades de persistencia con información de correos
     * @return Lista de modelos de dominio EmailPerson
     */
    List<EmailPerson> toEmailPersonList(List<EmailPersonEntity> emailPersonEntities);
}
