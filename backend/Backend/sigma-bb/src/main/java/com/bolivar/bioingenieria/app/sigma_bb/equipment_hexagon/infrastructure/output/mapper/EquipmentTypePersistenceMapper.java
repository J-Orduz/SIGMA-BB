package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.MetrologicalData;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.EquipmentTypeEntity;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.MetrologicalDataEntity;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.MetrologicalDataId;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.TechnicalVerificationEquipmentEntity;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.TechnicalVerificationEquipmentId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EquipmentTypePersistenceMapper {

    public EquipmentType toEquipmentType(EquipmentTypeEntity entity) {
        if (entity == null) {
            return null;
        }

        return EquipmentType.builder()
                .id(entity.getId())
                .equipmentTypeName(entity.getEquipmentTypeName())
                .technicalDefinition(entity.getTechnicalDefinition())
                .careRecommendations(entity.getCareRecommendations())
                .voltage(entity.getVoltage())
                .amperage(entity.getAmperage())
                .predominantTechnology(entity.getPredominantTechnology())
                .verifiable(entity.getVerifiable())
                .unitMaintenanceValue(entity.getUnitMaintenanceValue())
                .metrologicalData(toMetrologicalDataList(entity.getMetrologicalDataEntities()))
                .technicalVerification(toTechnicalVerificationList(entity.getTechnicalVerificationEquipmentEntities()))
                .build();
    }

    public EquipmentTypeEntity toEquipmentTypeEntity(EquipmentType domain) {
        if (domain == null) {
            return null;
        }

        return EquipmentTypeEntity.builder()
                .id(domain.getId())
                .equipmentTypeName(domain.getEquipmentTypeName())
                .technicalDefinition(domain.getTechnicalDefinition())
                .careRecommendations(domain.getCareRecommendations())
                .voltage(domain.getVoltage())
                .amperage(domain.getAmperage())
                .predominantTechnology(domain.getPredominantTechnology())
                .verifiable(domain.getVerifiable())
                .unitMaintenanceValue(domain.getUnitMaintenanceValue())
                .metrologicalDataEntities(toMetrologicalDataEntityList(domain.getMetrologicalData(),domain.getId()))
                .technicalVerificationEquipmentEntities(toTechnicalVerificationEntityList(domain.getTechnicalVerification(), domain.getId()))
                .active(true)
                .build();
    }

    public List<EquipmentType> toEquipmentTypeList(List<EquipmentTypeEntity> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
        return entities.stream()
                .map(this::toEquipmentType)
                .collect(Collectors.toList());
    }

    public void updateEntityFromDomain(EquipmentType source, EquipmentTypeEntity target) {
        if (source == null || target == null) {
            return;
        }

        if (source.getEquipmentTypeName() != null) {
            target.setEquipmentTypeName(source.getEquipmentTypeName());
        }
        if (source.getTechnicalDefinition() != null) {
            target.setTechnicalDefinition(source.getTechnicalDefinition());
        }
        if (source.getCareRecommendations() != null) {
            target.setCareRecommendations(source.getCareRecommendations());
        }
        if (source.getVoltage() != null) {
            target.setVoltage(source.getVoltage());
        }
        if (source.getAmperage() != null) {
            target.setAmperage(source.getAmperage());
        }
        if (source.getPredominantTechnology() != null) {
            target.setPredominantTechnology(source.getPredominantTechnology());
        }
        if (source.getVerifiable() != null) {
            target.setVerifiable(source.getVerifiable());
        }
        if (source.getUnitMaintenanceValue() != null) {
            target.setUnitMaintenanceValue(source.getUnitMaintenanceValue());
        }

        // Actualizar las listas hijas
        target.setMetrologicalDataEntities(toMetrologicalDataEntityList(source.getMetrologicalData(),source.getId()));
        target.setTechnicalVerificationEquipmentEntities(
                toTechnicalVerificationEntityList(source.getTechnicalVerification(), target.getId()));
    }

    private List<MetrologicalData> toMetrologicalDataList(List<MetrologicalDataEntity> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
        return entities.stream()
                .map(this::toMetrologicalData)
                .collect(Collectors.toList());
    }

    private MetrologicalData toMetrologicalData(MetrologicalDataEntity entity) {
        if (entity == null) {
            return null;
        }
        MetrologicalDataId id = entity.getId();
        return MetrologicalData.builder()
                .value(id != null ? id.getValue() : null)
                .type(id != null ? id.getType() : null)
                .build();
    }

    private List<MetrologicalDataEntity> toMetrologicalDataEntityList(List<MetrologicalData> domains, UUID equipmentTypeId) {
        if (domains == null) {
            return new ArrayList<>();
        }
        return domains.stream()
                .map(e -> toMetrologicalDataEntity(e,equipmentTypeId))
                .collect(Collectors.toList());
    }

    private MetrologicalDataEntity toMetrologicalDataEntity(MetrologicalData domain, UUID equipmentTypeId) {
        if (domain == null) {
            return null;
        }
        MetrologicalDataEntity entity = new MetrologicalDataEntity();
        MetrologicalDataId id = new MetrologicalDataId();
        id.setValue(domain.getValue());
        id.setType(domain.getType());
        id.setEquipmentTypeId(equipmentTypeId);
        entity.setId(id);
        //entity.setActive(true);
        return entity;
    }

    private Set<UUID> toTechnicalVerificationList(List<TechnicalVerificationEquipmentEntity> entities) {
        if (entities == null) {
            return new HashSet<>();
        }
        return entities.stream()
                .map(e -> e.getTecnicalVerificationEquipmentId() != null ? e.getTecnicalVerificationEquipmentId().getVerificationId() : null)
                .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
    }


    private List<TechnicalVerificationEquipmentEntity> toTechnicalVerificationEntityList(Set<UUID> domains, UUID equipmentTypeId) {
        if (domains == null) {
            return new ArrayList<>();
        }
        return domains.stream()
                .map(e -> toTechnicalVerificationEquipmentEntity(e, equipmentTypeId)
                ).collect(Collectors.toList());
    }

    private TechnicalVerificationEquipmentEntity toTechnicalVerificationEquipmentEntity(UUID domain, UUID equipmentTypeId) {
        if (domain == null) {
            return null;
        }
        TechnicalVerificationEquipmentEntity entity = new TechnicalVerificationEquipmentEntity();
        TechnicalVerificationEquipmentId id = new TechnicalVerificationEquipmentId();
        id.setVerificationId(domain);
        id.setTypeEquipmentId(equipmentTypeId);
        entity.setTecnicalVerificationEquipmentId(id);
        return entity;
    }
}