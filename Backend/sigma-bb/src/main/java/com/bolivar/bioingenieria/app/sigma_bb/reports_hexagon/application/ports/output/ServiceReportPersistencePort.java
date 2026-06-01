package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.domain.model.ServiceReport;

import java.util.List;
import java.util.Optional;

public interface ServiceReportPersistencePort {
    List<ServiceReport> findAll();
    Optional<ServiceReport> findById(String id);
    ServiceReport save(ServiceReport serviceReport);
    ServiceReport update(String id, ServiceReport serviceReport);
    void delete(String id);
}
