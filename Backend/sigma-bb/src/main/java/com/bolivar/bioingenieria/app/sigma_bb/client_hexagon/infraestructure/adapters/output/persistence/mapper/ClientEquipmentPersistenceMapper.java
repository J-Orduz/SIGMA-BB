package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ClientEquipment;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.ClientEquipmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Interfaz mapeadora para convertir entre el modelo de dominio {@link ClientEquipment}
 * y su representación de persistencia {@link ClientEquipmentEntity}.
 *
 * Utiliza MapStruct para generar automáticamente las implementaciones
 * de conversión entre estas capas arquitectónicas.
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientEquipmentPersistenceMapper {

    /**
     * Convierte un modelo de dominio {@link ClientEquipment} a su representación de entidad {@link ClientEquipmentEntity}.
     *
     * @param clientEquipment Objeto de dominio con la información del equipo del cliente
     * @return Entidad {@link ClientEquipmentEntity} lista para persistencia
     */
    ClientEquipmentEntity toClientEquipmentEntity(ClientEquipment clientEquipment);

    /**
     * Convierte una entidad de persistencia {@link ClientEquipmentEntity} a su modelo de dominio {@link ClientEquipment}.
     *
     * @param clientEquipmentEntity Entidad de persistencia con la información del equipo del cliente
     * @return Modelo de dominio {@link ClientEquipment} con los datos recuperados
     */
    ClientEquipment toClientEquipment(ClientEquipmentEntity clientEquipmentEntity);

    /**
     * Convierte una lista de entidades de persistencia {@link ClientEquipmentEntity} a una lista
     * de modelos de dominio {@link ClientEquipment}.
     *
     * @param clientEquipmentEntities Lista de entidades de persistencia con información de equipos del cliente
     * @return Lista de modelos de dominio {@link ClientEquipment}
     */
    List<ClientEquipment> toClientEquipmentList(List<ClientEquipmentEntity> clientEquipmentEntities);
}
