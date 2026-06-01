package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.domain.model.ServiceReport;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.entities.ServiceReportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ServiceReportPersistenceMapper {

    ServiceReport toServiceReport(ServiceReportEntity serviceReportEntity);

    ServiceReportEntity toServiceReportEntity(ServiceReport serviceReport);

    List<ServiceReport> toServiceReportList(List<ServiceReportEntity> serviceReportEntities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDomain(ServiceReport source, @MappingTarget ServiceReportEntity target);
}
