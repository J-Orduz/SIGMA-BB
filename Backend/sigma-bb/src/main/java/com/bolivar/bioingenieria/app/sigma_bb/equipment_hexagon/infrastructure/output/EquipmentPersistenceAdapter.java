package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.EquipmentPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.Equipment;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.EquipmentEntity;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.EquipmentNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper.EquipmentPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository.SpringEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EquipmentPersistenceAdapter implements EquipmentPersistencePort {
    private final SpringEquipmentRepository repository;
    private final EquipmentPersistenceMapper mapper;

    @Autowired
    public EquipmentPersistenceAdapter(SpringEquipmentRepository repository,
                                       EquipmentPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Equipment> findAll() {
        return repository.findAll().stream().map(this::mapToDomain).toList();
    }

    @Override
    public Optional<Equipment> findById(String id) {
        return repository.findById(UUID.fromString(id)).map(this::mapToDomain);
    }

    @Override
    public Equipment save(Equipment equipment) {
        EquipmentEntity saved = repository.save(mapper.toEquipmentEntity(equipment));
        return mapToDomain(saved);
    }

    @Override
    public Equipment update(String id, Equipment equipment) {
        UUID uuid = UUID.fromString(id);
        EquipmentEntity existing = repository.findById(uuid)
                .orElseThrow(() -> new EquipmentNotFoundException(id));
        mapper.updateEntityFromDomain(equipment, existing);
        EquipmentEntity saved = repository.save(existing);
        return mapToDomain(saved);
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (!repository.existsById(uuid)) throw new EquipmentNotFoundException(id);
        repository.deleteById(uuid);
    }

    private Equipment mapToDomain(EquipmentEntity entity) {
        return mapper.toEquipment(entity);
    }
}
