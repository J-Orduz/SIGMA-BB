package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.MetrologicalData;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.MetrologicalDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface MetrologicalDataPersistenceMapper {

    @Mapping(target = "value", source = "id.value")
    @Mapping(target = "type", source = "id.type")
    MetrologicalData toMetrologicalData(MetrologicalDataEntity entity);

    @Mapping(target = "id.value", source = "value")
    @Mapping(target = "id.type", source = "type")
    @Mapping(target = "id.equipmentTypeId", ignore = true)
    @Mapping(target = "equipmentType", ignore = true)
    @Mapping(target = "active", ignore = true)
    MetrologicalDataEntity toMetrologicalDataEntity(MetrologicalData domain);

    List<MetrologicalData> toMetrologicalDataList(List<MetrologicalDataEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipmentType", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromDomain(MetrologicalData source, @MappingTarget MetrologicalDataEntity target);
}
