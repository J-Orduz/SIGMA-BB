package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.EquipmentTypePersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.MetrologicalData;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.*;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.EquipmentTypeNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper.EquipmentTypePersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository.SpringEquipmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public boolean exists(UUID id) {
        return repository.existsById(id);
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

    // -------------------------------------------------------
    // ---------- METROLOGICAL DATA ATÓMICOS -----------------
    // -------------------------------------------------------

    @Override
    @Transactional
    public void addMetrologicalData(String equipmentTypeId, MetrologicalData data) {
        EquipmentTypeEntity existing = loadExisting(equipmentTypeId);

        MetrologicalDataEntity child = toMetrologicalDataEntity(data, existing.getId());
        child.setEquipmentType(existing);
        existing.getMetrologicalDataEntities().add(child);

        repository.save(existing);
    }

    @Override
    @Transactional
    public void removeMetrologicalData(String equipmentTypeId, MetrologicalData data) {
        EquipmentTypeEntity existing = loadExisting(equipmentTypeId);

        boolean removed = existing.getMetrologicalDataEntities().removeIf(child ->
                child.getId() != null
                        && Objects.equals(child.getId().getValue(), data.getValue())
                        && Objects.equals(child.getId().getType(), data.getType()));
        if (!removed) {
            throw new EquipmentTypeNotFoundException(
                    "MetrologicalData not found: value=" + data.getValue() + " type=" + data.getType());
        }

        repository.save(existing);
    }

    @Override
    @Transactional
    public void addMetrologicalDataList(String equipmentTypeId, List<MetrologicalData> dataList) {
        EquipmentTypeEntity existing = loadExisting(equipmentTypeId);

        for (MetrologicalData data : dataList) {
            MetrologicalDataEntity child = toMetrologicalDataEntity(data, existing.getId());
            child.setEquipmentType(existing);
            existing.getMetrologicalDataEntities().add(child);
        }

        repository.save(existing);
    }

    @Override
    @Transactional
    public void removeMetrologicalDataList(String equipmentTypeId, List<MetrologicalData> dataList) {
        EquipmentTypeEntity existing = loadExisting(equipmentTypeId);

        existing.getMetrologicalDataEntities().removeIf(child ->
                child.getId() != null && dataList.stream().anyMatch(data ->
                        Objects.equals(child.getId().getValue(), data.getValue())
                                && Objects.equals(child.getId().getType(), data.getType())));

        repository.save(existing);
    }

    // -------------------------------------------------------
    // ---------- TECHNICAL VERIFICATION ATÓMICOS ------------
    // -------------------------------------------------------

    @Override
    @Transactional
    public void addTechnicalVerification(String equipmentTypeId, UUID verificationId) {
        EquipmentTypeEntity existing = loadExisting(equipmentTypeId);

        TechnicalVerificationEquipmentEntity child = new TechnicalVerificationEquipmentEntity();
        TechnicalVerificationEquipmentId id = new TechnicalVerificationEquipmentId();
        id.setVerificationId(verificationId);
        id.setTypeEquipmentId(existing.getId());
        child.setTecnicalVerificationEquipmentId(id);
        child.setActive(true);
        child.setEquipmentTypeEntity(existing);

        existing.getTechnicalVerificationEquipmentEntities().add(child);

        repository.save(existing);
    }

    @Override
    @Transactional
    public void removeTechnicalVerification(String equipmentTypeId, UUID verificationId) {
        EquipmentTypeEntity existing = loadExisting(equipmentTypeId);

        boolean removed = existing.getTechnicalVerificationEquipmentEntities().removeIf(child ->
                child.getTecnicalVerificationEquipmentId() != null
                        && Objects.equals(child.getTecnicalVerificationEquipmentId().getVerificationId(), verificationId));
        if (!removed) {
            throw new EquipmentTypeNotFoundException(
                    "TechnicalVerification not found: " + verificationId);
        }

        repository.save(existing);
    }

    @Override
    @Transactional
    public void addTechnicalVerificationList(String equipmentTypeId, Set<UUID> ids) {
        EquipmentTypeEntity existing = loadExisting(equipmentTypeId);

        for (UUID verificationId : ids) {
            TechnicalVerificationEquipmentEntity child = new TechnicalVerificationEquipmentEntity();
            TechnicalVerificationEquipmentId childId = new TechnicalVerificationEquipmentId();
            childId.setVerificationId(verificationId);
            childId.setTypeEquipmentId(existing.getId());
            child.setTecnicalVerificationEquipmentId(childId);
            child.setActive(true);
            child.setEquipmentTypeEntity(existing);
            existing.getTechnicalVerificationEquipmentEntities().add(child);
        }

        repository.save(existing);
    }

    @Override
    @Transactional
    public void removeTechnicalVerificationList(String equipmentTypeId, Set<UUID> ids) {
        EquipmentTypeEntity existing = loadExisting(equipmentTypeId);

        existing.getTechnicalVerificationEquipmentEntities().removeIf(child ->
                child.getTecnicalVerificationEquipmentId() != null
                        && ids.contains(child.getTecnicalVerificationEquipmentId().getVerificationId()));

        repository.save(existing);
    }

    // -------------------------------------------------------
    // -------------------- HELPERS ---------------------------
    // -------------------------------------------------------

    private EquipmentTypeEntity loadExisting(String equipmentTypeId) {
        UUID uuid = UUID.fromString(equipmentTypeId);
        return repository.findByIdWithMetrologicalData(uuid)
                .orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentTypeId));
    }

    private MetrologicalDataEntity toMetrologicalDataEntity(MetrologicalData domain, UUID equipmentTypeId) {
        MetrologicalDataEntity entity = new MetrologicalDataEntity();
        MetrologicalDataId id = new MetrologicalDataId();
        id.setValue(domain.getValue());
        id.setType(domain.getType());
        id.setEquipmentTypeId(equipmentTypeId);
        entity.setId(id);
        entity.setActive(true);
        return entity;
    }
}
