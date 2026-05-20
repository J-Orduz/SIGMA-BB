package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Headquarter;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.HeadquarterEntity;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.ManagerEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Interfaz mapeadora para convertir entre el modelo de dominio {@link Headquarter}
 * y su representación de persistencia {@link HeadquarterEntity}.
 *
 * Utiliza MapStruct para generar automáticamente las implementaciones
 * de conversión entre estas capas arquitectónicas.
 */
@Mapper(componentModel = "spring", uses = {
        ServiceAreaPersistenceMapper.class,
        ManagerPersistenceMapper.class})
public interface HeadquarterPersistenceMapper {

    /**
     * Convierte un modelo de dominio {@link Headquarter} a su representación de entidad {@link HeadquarterEntity}.
     *
     * @param headquarter Objeto de dominio con la información de la sede
     * @return Entidad {@link HeadquarterEntity} lista para persistencia
     */
    HeadquarterEntity toHeadquarterEntity(Headquarter headquarter);

    /**
     * Convierte una entidad de persistencia {@link HeadquarterEntity} a su modelo de dominio {@link Headquarter}.
     *
     * @param headquarterEntity Entidad de persistencia con la información de la sede
     * @return Modelo de dominio {@link Headquarter} con los datos recuperados
     */
    Headquarter toHeadquarter(HeadquarterEntity headquarterEntity);

    /**
     * Convierte una lista de entidades de persistencia {@link HeadquarterEntity} a una lista
     * de modelos de dominio {@link Headquarter}.
     *
     * @param headquarterEntities Lista de entidades de persistencia con información de sedes
     * @return Lista de modelos de dominio {@link Headquarter}
     */
    List<Headquarter> toHeadquarterList(List<HeadquarterEntity> headquarterEntities);

    /**
     * Vincula automáticamente las relaciones bidireccionales entre la entidad {@link HeadquarterEntity}
     * y sus entidades relacionadas (ServiceAreaEntity) después del mapeo inicial.
     *
     * Este método se ejecuta después del mapeo para asegurar que las referencias
     * inversas se establezcan correctamente en la base de datos.
     *
     * @param headquarterEntity Entidad {@link HeadquarterEntity} con las relaciones a vincular
     */
    @AfterMapping
    default void linkChlidren(@MappingTarget HeadquarterEntity headquarterEntity) {
        if (headquarterEntity.getServiceAreaList() != null) {
            headquarterEntity.getServiceAreaList()
                    .forEach(serviceAreaEntity ->
                            serviceAreaEntity.setHeadquarter(headquarterEntity));
        }
        if (headquarterEntity.getManagerList() != null) {
            for (ManagerEntity managerEntity : headquarterEntity.getManagerList()) {
                managerEntity.setHeadquarter(headquarterEntity);
            }
        }
    }
}
