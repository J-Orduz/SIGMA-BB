package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.EmailClient;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.EmailClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Interfaz mapeadora para convertir entre el modelo de dominio {@link EmailClient}
 * y su representación de persistencia {@link EmailClientEntity}.
 *
 * Utiliza MapStruct para generar automáticamente las implementaciones
 * de conversión entre estas capas arquitectónicas.
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailClientPersistenceMapper {

    /**
     * Convierte un modelo de dominio {@link EmailClient} a su representación de entidad {@link EmailClientEntity}.
     *
     * @param emailClient Objeto de dominio con la información del correo del cliente
     * @return Entidad {@link EmailClientEntity} lista para persistencia
     */
    EmailClientEntity toEmailClientEntity(EmailClient emailClient);

    /**
     * Convierte una entidad de persistencia {@link EmailClientEntity} a su modelo de dominio {@link EmailClient}.
     *
     * @param emailClientEntity Entidad de persistencia con la información del correo del cliente
     * @return Modelo de dominio {@link EmailClient} con los datos recuperados
     */
    EmailClient toEmailClient(EmailClientEntity emailClientEntity);

    /**
     * Convierte una lista de entidades de persistencia {@link EmailClientEntity} a una lista
     * de modelos de dominio {@link EmailClient}.
     *
     * @param emailClientEntities Lista de entidades de persistencia con información de correos del cliente
     * @return Lista de modelos de dominio EmailClient
     */
    List<EmailClient> toEmailClientList(List<EmailClientEntity> emailClientEntities);

}
