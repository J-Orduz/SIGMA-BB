package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.CreateServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.DeleteServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.PatchServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.UpdateServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.domain.model.ServiceReport;

import java.util.List;

public interface ServiceReportServicePort {
    List<ServiceReport> findAll();
    ServiceReport findById(String id);
    ServiceReport save(CreateServiceReportCommand command);
    ServiceReport update(String id, UpdateServiceReportCommand command);
    ServiceReport patchUpdate(String id, PatchServiceReportCommand command);
    void delete(DeleteServiceReportCommand command);
}
