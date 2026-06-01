package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ServiceArea;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.ManagerEntity;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.entity.ServiceAreaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Interfaz mapeadora para convertir entre el modelo de dominio {@link ServiceArea}
 * y su representación de persistencia {@link ServiceAreaEntity}.
 *
 * Utiliza MapStruct para generar automáticamente las implementaciones
 * de conversión entre estas capas arquitectónicas. Integra además los
 * mapeadores de {@link ManagerPersistenceMapper} y {@link ClientEquipmentPersistenceMapper}
 * para manejar las relaciones de uno a muchos con manager y client equipment respectivamente.
 */
@Mapper(componentModel = "spring", uses = {
        ManagerPersistenceMapper.class,
        ClientEquipmentPersistenceMapper.class})
public interface ServiceAreaPersistenceMapper {

    /**
     * Convierte un modelo de dominio {@link ServiceArea} a su representación de entidad {@link ServiceAreaEntity}.
     *
     * @param serviceArea Objeto de dominio con la información del área de servicio
     * @return Entidad {@link ServiceAreaEntity} lista para persistencia
     */
    @Mapping(target = "headquarter.identificadorSede", source = "identificadorSede")
    ServiceAreaEntity toServiceAreaEntity(ServiceArea serviceArea);

    /**
     * Convierte una entidad de persistencia {@link ServiceAreaEntity} a su modelo de dominio {@link ServiceArea}.
     *
     * @param serviceAreaEntity Entidad de persistencia con la información del área de servicio
     * @return Modelo de dominio {@link ServiceArea} con los datos recuperados
     */
    @Mapping(target = "identificadorSede", source = "headquarter.identificadorSede")
    ServiceArea toServiceArea(ServiceAreaEntity serviceAreaEntity);

    /**
     * Convierte una lista de entidades de persistencia {@link ServiceAreaEntity} a una lista
     * de modelos de dominio {@link ServiceArea}.
     *
     * @param serviceAreaEntities Lista de entidades de persistencia con información de áreas de servicio
     * @return Lista de modelos de dominio {@link ServiceArea}
     */
    List<ServiceArea> toServiceAreaList(List<ServiceAreaEntity> serviceAreaEntities);

    /**
     * Vincula automáticamente las relaciones bidireccionales entre la entidad {@link ServiceAreaEntity}
     * y sus entidades relacionadas (ManagerEntity y ClientEquipmentEntity) después del mapeo inicial.
     *
     * Este método se ejecuta después del mapeo para asegurar que las referencias
     * inversas se establezcan correctamente en la base de datos.
     *
     * @param serviceAreaEntity Entidad {@link ServiceAreaEntity} con las relaciones a vincular
     */
    @AfterMapping
    default void linkChildren(@MappingTarget ServiceAreaEntity serviceAreaEntity) {
        if (serviceAreaEntity.getClientEquipmentList() != null) {
            serviceAreaEntity.getClientEquipmentList()
                    .forEach(equipment ->
                            equipment.setServiceArea(serviceAreaEntity));
        }
        if (serviceAreaEntity.getManagerList() != null) {
            for (ManagerEntity managerEntity : serviceAreaEntity.getManagerList()) {
                managerEntity.setServiceArea(serviceAreaEntity);
            }
        }
    }
}
