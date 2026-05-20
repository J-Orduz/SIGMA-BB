package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.ManufacturerPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.Manufacturer;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.ManufacturerEntity;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.ManufacturerNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper.ManufacturerPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository.SpringManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ManufacturerPersistenceAdapter implements ManufacturerPersistencePort {
    private final SpringManufacturerRepository springManufacturerRepository;
    private final ManufacturerPersistenceMapper manufacturerPersistenceMapper;

    @Autowired
    public ManufacturerPersistenceAdapter(SpringManufacturerRepository springManufacturerRepository,
                                           ManufacturerPersistenceMapper manufacturerPersistenceMapper) {
        this.springManufacturerRepository = springManufacturerRepository;
        this.manufacturerPersistenceMapper = manufacturerPersistenceMapper;
    }

    @Override
    public List<Manufacturer> findAll() {
        return springManufacturerRepository.findAll().stream()
                .map(manufacturerPersistenceMapper::toManufacturer).toList();
    }

    @Override
    public Optional<Manufacturer> findById(String id) {
        return springManufacturerRepository.findById(UUID.fromString(id))
                .map(manufacturerPersistenceMapper::toManufacturer);
    }

    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        return manufacturerPersistenceMapper.toManufacturer(
                springManufacturerRepository.save(
                        manufacturerPersistenceMapper.toManufacturerEntity(manufacturer)));
    }

    @Override
    public Manufacturer update(String id, Manufacturer manufacturer) {
        UUID uuid = UUID.fromString(id);
        ManufacturerEntity existing = springManufacturerRepository.findById(uuid)
                .orElseThrow(() -> new ManufacturerNotFoundException(id));
        manufacturerPersistenceMapper.updateEntityFromDomain(manufacturer, existing);
        return manufacturerPersistenceMapper.toManufacturer(springManufacturerRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (!springManufacturerRepository.existsById(uuid)) {
            throw new ManufacturerNotFoundException(id);
        }
        springManufacturerRepository.deleteById(uuid);
    }
}
