package com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.domain.model.person_model.PhonePerson;
import com.bolivar.bioingenieria.app.sigma_bb.person_hexagon.infrastructure.adapters.output.persistence.entity.PhonePersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Interfaz mapeadora para convertir entre el modelo de dominio {@link PhonePerson}
 * y su representación de persistencia {@link PhonePersonEntity}.
 *
 * Utiliza MapStruct para generar automáticamente las implementaciones
 * de conversión entre estas capas arquitectónicas.
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhonePersonPersistenceMapper {

    /**
     * Convierte un modelo de dominio {@link PhonePerson} a su representación de entidad {@link PhonePersonEntity}.
     *
     * @param phonePerson Objeto de dominio con la información del teléfono
     * @return Entidad {@link PhonePersonEntity} lista para persistencia
     */
    PhonePersonEntity toPhonePersonEntity(PhonePerson phonePerson);

    /**
     * Convierte una entidad de persistencia {@link PhonePersonEntity} a su modelo de dominio {@link PhonePerson}.
     *
     * @param phonePersonEntity Entidad de persistencia con la información del teléfono
     * @return Modelo de dominio {@link PhonePerson} con los datos recuperados
     */
    PhonePerson toPhonePerson(PhonePersonEntity phonePersonEntity);

    /**
     * Convierte una lista de entidades de persistencia {@link PhonePersonEntity} a una lista
     * de modelos de dominio {@link PhonePerson}.
     *
     * @param phonePersonEntities Lista de entidades de persistencia con información de teléfonos
     * @return Lista de modelos de dominio {@link PhonePerson}
     */
    List<PhonePerson> toPhonePersonList(List<PhonePersonEntity> phonePersonEntities);
}
