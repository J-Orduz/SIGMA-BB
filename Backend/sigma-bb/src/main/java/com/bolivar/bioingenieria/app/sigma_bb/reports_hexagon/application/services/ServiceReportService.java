package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services;

import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.ports.input.ServiceReportServicePort;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.ports.output.ServiceReportPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.CreateServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.DeleteServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.PatchServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.services.service_report_services.commands.UpdateServiceReportCommand;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.domain.model.ServiceReport;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.errors.ServiceReportNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceReportService implements ServiceReportServicePort {
    private final ServiceReportPersistencePort serviceReportPersistencePort;

    @Autowired
    public ServiceReportService(ServiceReportPersistencePort serviceReportPersistencePort) {
        this.serviceReportPersistencePort = serviceReportPersistencePort;
    }

    @Override
    public List<ServiceReport> findAll() {
        return serviceReportPersistencePort.findAll();
    }

    @Override
    public ServiceReport findById(String id) {
        return serviceReportPersistencePort.findById(id)
                .orElseThrow(() -> new ServiceReportNotFoundException(id));
    }

    @Override
    public ServiceReport save(CreateServiceReportCommand command) {
        ServiceReport serviceReport = ServiceReport.create(
                command.workOrderId(),
                command.clientEquipmentId(),
                command.observations(),
                command.technicalVerificationResult());

        return serviceReportPersistencePort.save(serviceReport);
    }

    @Override
    public ServiceReport update(String id, UpdateServiceReportCommand command) {
        ServiceReport serviceReport = findById(id);
        serviceReport.update(
                command.workOrderId(),
                command.clientEquipmentId(),
                command.observations(),
                command.technicalVerificationResult());

        return serviceReportPersistencePort.update(id, serviceReport);
    }

    @Override
    public ServiceReport patchUpdate(String id, PatchServiceReportCommand command) {
        ServiceReport serviceReport = findById(id);
        serviceReport.patch(
                command.workOrderId(),
                command.clientEquipmentId(),
                command.observations(),
                command.technicalVerificationResult());

        return serviceReportPersistencePort.update(id, serviceReport);
    }

    @Override
    public void delete(DeleteServiceReportCommand command) {
        findById(command.id());
        serviceReportPersistencePort.delete(command.id());
    }
}
