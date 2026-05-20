package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.Manufacturer;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.ManufacturerRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.ManufacturerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ManufacturerRestMapper {
    Manufacturer toManufacturer(ManufacturerRequest manufacturerRequest);
    ManufacturerResponse toManufacturerResponse(Manufacturer manufacturer);
    List<ManufacturerResponse> toManufacturerResponseList(List<Manufacturer> manufacturerList);
}
