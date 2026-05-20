package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.City;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.request.CityRequest;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.infrastructure.input.model.response.CityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CityRestMapper {
    City toCity(CityRequest cityRequest);
    CityResponse toCityResponse(City city);
    List<CityResponse> toCityResponseList(List<City> cityList);
}
