package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.EquipmentTypePersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.*;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.EquipmentTypeNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper.EquipmentTypePersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper.MetrologicalDataPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository.SpringEquipmentTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EquipmentTypePersistenceAdapter implements EquipmentTypePersistencePort {

    private final SpringEquipmentTypeRepository repository;
    private final EquipmentTypePersistenceMapper mapper;

    @Autowired
    public EquipmentTypePersistenceAdapter(SpringEquipmentTypeRepository repository,
                                           EquipmentTypePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public List<EquipmentType> findAll() {
        return repository.findAllWithMetrologicalData()
                .stream()
                .map(mapper::toEquipmentType)
                .toList();
    }

    @Override
    @Transactional
    public Optional<EquipmentType> findById(String id) {
        UUID uuid = UUID.fromString(id);
        return repository.findByIdWithMetrologicalData(uuid)
                .map(mapper::toEquipmentType);
    }

    @Override
    @Transactional
    public EquipmentType save(EquipmentType equipmentType) {
        EquipmentTypeEntity entity = mapper.toEquipmentTypeEntity(equipmentType);
        return mapper.toEquipmentType(repository.save(entity));
    }

    @Override
    @Transactional
    public EquipmentType update(String id, EquipmentType equipmentType) {
        UUID uuid = UUID.fromString(id);

        EquipmentTypeEntity existing = repository.findByIdWithMetrologicalData(uuid)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(id));
        
        mapper.updateEntityFromDomain(equipmentType, existing);

        return mapper.toEquipmentType(repository.save(existing));
    }

    @Override
    @Transactional
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);

        EquipmentTypeEntity entity = repository.findByIdWithMetrologicalData(uuid)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(id));

        entity.setActive(false);

        if (entity.getTechnicalVerificationEquipmentEntities() != null) {
            entity.getTechnicalVerificationEquipmentEntities()
                    .forEach(child -> child.setActive(false));
        }

        if (entity.getMetrologicalDataEntities() != null) {
            entity.getMetrologicalDataEntities()
                    .forEach(child -> child.setActive(false));
        }

        repository.save(entity);
    }
}
