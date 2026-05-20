package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.Country;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.output.entities.CountryEntity;
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
public interface CountryPersistenceMapper {

    Country toCountry(CountryEntity countryEntity);

    CountryEntity toCountryEntity(Country country);

    List<Country> toCountryList(List<CountryEntity> countryEntities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDomain(Country source, @MappingTarget CountryEntity target);
}
