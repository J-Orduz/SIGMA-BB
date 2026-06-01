package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.domain.model.ServiceReport;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.request.ServiceReportRequest;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.input.model.response.ServiceReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ServiceReportRestMapper {
    ServiceReport toServiceReport(ServiceReportRequest serviceReportRequest);
    ServiceReportResponse toServiceReportResponse(ServiceReport serviceReport);
    List<ServiceReportResponse> toServiceReportResponseList(List<ServiceReport> serviceReports);
}
