package com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.adapters;

import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.application.ports.output.ServiceAreaPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.domain.model.client_model.ServiceArea;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.mapper.ServiceAreaPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.client_hexagon.infraestructure.adapters.output.persistence.repository.ServiceAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ServiceAreaPersistenceAdapter implements ServiceAreaPersistencePort {

    private final ServiceAreaRepository serviceAreaRepository;
    private final ServiceAreaPersistenceMapper serviceAreaPersistenceMapper;

    @Override
    public Optional<ServiceArea> findById(UUID id) {
        return serviceAreaRepository.findById(id)
                .map(serviceAreaPersistenceMapper::toServiceArea);
    }

    @Override
    public List<ServiceArea> findAll() {
        return serviceAreaPersistenceMapper.toServiceAreaList(
                serviceAreaRepository.findAll());
    }

    @Override
    public ServiceArea save(ServiceArea serviceArea) {
        return serviceAreaPersistenceMapper.toServiceArea(
                serviceAreaRepository.save(
                        serviceAreaPersistenceMapper.toServiceAreaEntity(serviceArea)));
    }

    @Override
    public void delete(ServiceArea serviceArea) {
        serviceAreaPersistenceMapper.toServiceArea(
                serviceAreaRepository.save(
                        serviceAreaPersistenceMapper.toServiceAreaEntity(serviceArea)));
    }
}
