package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.Manufacturer;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.ManufacturerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ManufacturerPersistenceMapper {

    Manufacturer toManufacturer(ManufacturerEntity manufacturerEntity);

    ManufacturerEntity toManufacturerEntity(Manufacturer manufacturer);

    List<Manufacturer> toManufacturerList(List<ManufacturerEntity> manufacturerEntities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDomain(Manufacturer source, @MappingTarget ManufacturerEntity target);
}
