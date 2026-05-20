package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.City;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.entities.CityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)

public interface CityPersistenceMapper {

    City toCity(CityEntity cityEntity);

    CityEntity toCityEntity(City city);

    List<City> toCityList(List<CityEntity> cityEntities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDomain(City source, @MappingTarget CityEntity target);
}
