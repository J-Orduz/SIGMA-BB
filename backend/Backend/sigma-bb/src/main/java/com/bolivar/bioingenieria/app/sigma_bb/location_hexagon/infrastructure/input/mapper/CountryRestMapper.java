package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.Country;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.request.CountryRequest;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.response.CountryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CountryRestMapper {
    Country toCountry(CountryRequest countryRequest);
    CountryResponse toCountryResponse(Country country);
    List<CountryResponse> toCountryResponseList(List<Country> countryList);
}
