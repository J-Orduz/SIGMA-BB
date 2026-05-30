package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.PhoneClient;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.PhoneClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Interfaz mapeadora para convertir entre el modelo de dominio {@link PhoneClient}
 * y su representación de persistencia {@link PhoneClientEntity}.
 *
 * Utiliza MapStruct para generar automáticamente las implementaciones
 * de conversión entre estas capas arquitectónicas.
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhoneClientPersistenceMapper {

    /**
     * Convierte un modelo de dominio {@link PhoneClient} a su representación de entidad {@link PhoneClientEntity}.
     *
     * @param phoneClient Objeto de dominio con la información del teléfono del cliente
     * @return Entidad {@link PhoneClientEntity} lista para persistencia
     */
    PhoneClientEntity toPhoneClientEntity(PhoneClient phoneClient);

    /**
     * Convierte una entidad de persistencia {@link PhoneClientEntity} a su modelo de dominio {@link PhoneClient}.
     *
     * @param phoneClientEntity Entidad de persistencia con la información del teléfono del cliente
     * @return Modelo de dominio {@link PhoneClient} con los datos recuperados
     */
    PhoneClient toPhoneClient(PhoneClientEntity phoneClientEntity);

    /**
     * Convierte una lista de entidades de persistencia {@link PhoneClientEntity} a una lista
     * de modelos de dominio {@link PhoneClient}.
     *
     * @param phoneClientEntities Lista de entidades de persistencia con información de teléfonos del cliente
     * @return Lista de modelos de dominio PhoneClient
     */
    List<PhoneClient> toPhoneClientList(List<PhoneClientEntity> phoneClientEntities);
}
