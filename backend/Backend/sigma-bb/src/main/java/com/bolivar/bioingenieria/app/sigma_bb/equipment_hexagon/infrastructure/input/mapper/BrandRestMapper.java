package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.Brand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.BrandRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.BrandResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BrandRestMapper {
    Brand toBrand(BrandRequest brandRequest);
    BrandResponse toBrandResponse(Brand brand);
    List<BrandResponse> toBrandResponseList(List<Brand> brandList);
}
