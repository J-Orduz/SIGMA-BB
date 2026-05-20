package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.MetrologicalData;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.request.MetrologicalDataRequest;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.input.model.response.MetrologicalDataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MetrologicalDataRestMapper {
    MetrologicalData toMetrologicalData(MetrologicalDataRequest metrologicalDataRequest);
    MetrologicalDataResponse toMetrologicalDataResponse(MetrologicalData metrologicalData);
    List<MetrologicalDataResponse> toMetrologicalDataResponseList(List<MetrologicalData> metrologicalDataList);
}
