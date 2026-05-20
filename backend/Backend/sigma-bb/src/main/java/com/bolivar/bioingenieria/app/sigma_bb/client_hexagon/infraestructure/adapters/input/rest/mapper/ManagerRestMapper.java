package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.Manager;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.request.manager_request.ManagerCreateRequest;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.input.rest.model.response.manager_response.ManagerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ManagerRestMapper {

    Manager toManager(ManagerCreateRequest managerCreateRequest);
    ManagerResponse toManagerResponse(Manager manager);
    List<ManagerResponse> toManagerResponseList(List<Manager> managerList);
}
