package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Manager;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.ManagerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Interfaz mapeadora para convertir entre el modelo de dominio {@link Manager}
 * y su representación de persistencia {@link ManagerEntity}.
 *
 * Utiliza MapStruct para generar automáticamente las implementaciones
 * de conversión entre estas capas arquitectónicas.
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ManagerPersistenceMapper {

    /**
     * Convierte un modelo de dominio {@link Manager} a su representación de entidad {@link ManagerEntity}.
     *
     * @param manager Objeto de dominio con la información del gerente
     * @return Entidad {@link ManagerEntity} lista para persistencia
     */
    ManagerEntity toManagerEntity(Manager manager);

    /**
     * Convierte una entidad de persistencia {@link ManagerEntity} a su modelo de dominio {@link Manager}.
     *
     * @param managerEntity Entidad de persistencia con la información del gerente
     * @return Modelo de dominio {@link Manager} con los datos recuperados
     */
    Manager toManager(ManagerEntity managerEntity);

    /**
     * Convierte una lista de entidades de persistencia {@link ManagerEntity} a una lista
     * de modelos de dominio {@link Manager}.
     *
     * @param managerEntities Lista de entidades de persistencia con información de gerentes
     * @return Lista de modelos de dominio {@link Manager}
     */
    List<Manager> toManagerList(List<ManagerEntity> managerEntities);
}
