package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.Brand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BrandPersistenceMapper {

    Brand toBrand(BrandEntity brandEntity);

    BrandEntity toBrandEntity(Brand brand);

    List<Brand> toBrandList(List<BrandEntity> brandEntities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDomain(Brand source, @MappingTarget BrandEntity target);
}
