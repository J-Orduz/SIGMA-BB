package com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.application.ports.output.ServiceReportPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.domain.model.ServiceReport;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.entities.ServiceReportEntity;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.errors.ServiceReportNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.mapper.ServiceReportPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.reports_hexagon.infrastructure.output.repository.SpringServiceReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ServiceReportPersistenceAdapter implements ServiceReportPersistencePort {
    private final SpringServiceReportRepository springServiceReportRepository;
    private final ServiceReportPersistenceMapper serviceReportPersistenceMapper;

    @Autowired
    public ServiceReportPersistenceAdapter(SpringServiceReportRepository springServiceReportRepository,
                                           ServiceReportPersistenceMapper serviceReportPersistenceMapper) {
        this.springServiceReportRepository = springServiceReportRepository;
        this.serviceReportPersistenceMapper = serviceReportPersistenceMapper;
    }

    @Override
    public List<ServiceReport> findAll() {
        return springServiceReportRepository.findAll().stream()
                .map(serviceReportPersistenceMapper::toServiceReport)
                .toList();
    }

    @Override
    public Optional<ServiceReport> findById(String id) {
        return springServiceReportRepository.findById(UUID.fromString(id))
                .map(serviceReportPersistenceMapper::toServiceReport);
    }

    @Override
    public ServiceReport save(ServiceReport serviceReport) {
        ServiceReportEntity entity = serviceReportPersistenceMapper.toServiceReportEntity(serviceReport);
        return serviceReportPersistenceMapper.toServiceReport(springServiceReportRepository.save(entity));
    }

    @Override
    public ServiceReport update(String id, ServiceReport serviceReport) {
        UUID uuid = UUID.fromString(id);
        ServiceReportEntity existing = springServiceReportRepository.findById(uuid)
                .orElseThrow(() -> new ServiceReportNotFoundException(id));
        serviceReportPersistenceMapper.updateEntityFromDomain(serviceReport, existing);
        return serviceReportPersistenceMapper.toServiceReport(springServiceReportRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (!springServiceReportRepository.existsById(uuid)) {
            throw new ServiceReportNotFoundException(id);
        }
        springServiceReportRepository.deleteById(uuid);
    }
}
