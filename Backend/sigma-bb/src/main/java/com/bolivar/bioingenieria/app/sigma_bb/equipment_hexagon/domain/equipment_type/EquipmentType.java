package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.MetrologicalData;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.events.EquipmentTypeCreatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.events.EquipmentTypeDeletedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.events.EquipmentTypePayload;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.events.EquipmentTypeUpdatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.error.DomainException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.events.MetrologicalDataCreatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.events.MetrologicalDataDeletedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.metrological_data.events.MetrologicalDataUpdatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.tecnical_verification_equipment.events.TechnicalVerificationEquipmentCreated;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.tecnical_verification_equipment.events.TechnicalVerificationEquipmentDeleted;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.tecnical_verification_equipment.events.TechnicalVerificationEquipmentPayload;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.tecnical_verification_equipment.events.TechnicalVerificationEquipmentUpdated;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.AggregateRoot;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EquipmentType extends AggregateRoot {
    private UUID id;
    private String equipmentTypeName;
    private String technicalDefinition;
    private String careRecommendations;
    private Integer voltage;
    private BigDecimal amperage;
    private String predominantTechnology;
    private Boolean verifiable;
    private Long unitMaintenanceValue;
    private List<MetrologicalData> metrologicalData = new ArrayList<>();
    private Set<UUID> technicalVerification = new HashSet<>();

    public static EquipmentType create(
            String equipmentTypeName, String technicalDefinition, String careRecommendations,
            Integer voltage, BigDecimal amperage, String predominantTechnology,
            Boolean verifiable, Long unitMaintenanceValue, List<MetrologicalData> metrologicalData) {

        EquipmentType et = EquipmentType.builder()
                .id(UUID.randomUUID())
                .equipmentTypeName(equipmentTypeName)
                .technicalDefinition(technicalDefinition)
                .careRecommendations(careRecommendations)
                .voltage(voltage)
                .amperage(amperage)
                .predominantTechnology(predominantTechnology)
                .verifiable(verifiable)
                .unitMaintenanceValue(unitMaintenanceValue)
                .metrologicalData(metrologicalData == null ? new ArrayList<>() : new ArrayList<>(metrologicalData))
                .build();

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "EquipmentType", "equipmentType.created", 1, Instant.now(), et.id.toString());

        et.registerEvent(new EquipmentTypeCreatedEvent(metadata, new EquipmentTypePayload(
                et.equipmentTypeName, et.technicalDefinition, et.careRecommendations,
                et.voltage, et.amperage, et.predominantTechnology, et.verifiable, et.unitMaintenanceValue)));
        return et;
    }

    public void updateEquipmentType(
            String equipmentTypeName, String technicalDefinition, String careRecommendations,
            Integer voltage, BigDecimal amperage, String predominantTechnology,
            Boolean verifiable, Long unitMaintenanceValue) {
        this.equipmentTypeName = equipmentTypeName;
        this.technicalDefinition = technicalDefinition;
        this.careRecommendations = careRecommendations;
        this.voltage = voltage;
        this.amperage = amperage;
        this.predominantTechnology = predominantTechnology;
        this.verifiable = verifiable;
        this.unitMaintenanceValue = unitMaintenanceValue;

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "EquipmentType", "equipmentType.updated", 1, Instant.now(), this.id.toString());

        registerEvent(new EquipmentTypeUpdatedEvent(metadata, new EquipmentTypePayload(
                this.equipmentTypeName, this.technicalDefinition, this.careRecommendations,
                this.voltage, this.amperage, this.predominantTechnology, this.verifiable, this.unitMaintenanceValue)));
    }

    public void updateEquipmentTypePatch(
            String equipmentTypeName, String technicalDefinition, String careRecommendations,
            Integer voltage, BigDecimal amperage, String predominantTechnology,
            Boolean verifiable, Long unitMaintenanceValue) {
        if (equipmentTypeName != null) {
            this.equipmentTypeName = equipmentTypeName;
        }
        if (technicalDefinition != null) {
            this.technicalDefinition = technicalDefinition;
        }
        if (careRecommendations != null) {
            this.careRecommendations = careRecommendations;
        }
        if (voltage != null) {
            this.voltage = voltage;
        }
        if (amperage != null) {
            this.amperage = amperage;
        }
        if (predominantTechnology != null) {
            this.predominantTechnology = predominantTechnology;
        }
        if (verifiable != null) {
            this.verifiable = verifiable;
        }
        if (unitMaintenanceValue != null) {
            this.unitMaintenanceValue = unitMaintenanceValue;
        }

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "EquipmentType", "equipmentType.patch", 1, Instant.now(), this.id.toString());

        registerEvent(new EquipmentTypeUpdatedEvent(metadata, new EquipmentTypePayload(
                this.equipmentTypeName, this.technicalDefinition, this.careRecommendations,
                this.voltage, this.amperage, this.predominantTechnology, this.verifiable, this.unitMaintenanceValue)));
    }

    public void deleteEquipmentType() {
        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "EquipmentType", "equipmentType.deleted", 1, Instant.now(), this.id.toString());
        registerEvent(new EquipmentTypeDeletedEvent(metadata, new EquipmentTypePayload(
                this.equipmentTypeName, this.technicalDefinition, this.careRecommendations,
                this.voltage, this.amperage, this.predominantTechnology, this.verifiable, this.unitMaintenanceValue)));
    }

    public void addTechicalVerificationId(UUID technicalVerification) {
        boolean duplicated = this.technicalVerification.contains(technicalVerification);
        if (duplicated) throw new DomainException("Technical verification already exists for this equipment type");

        this.technicalVerification.add(technicalVerification);

        EventMetadata metadata = new EventMetadata(
                "events-domain",UUID.randomUUID().toString(), "EquipmentType",
                "technical-verification.added", 1, Instant.now(), this.id.toString());
        TechnicalVerificationEquipmentPayload tvPayload = new
                TechnicalVerificationEquipmentPayload(
                        technicalVerification,
                        this.id
                );

        registerEvent(new TechnicalVerificationEquipmentCreated(metadata, tvPayload));
    }

    public void updateTechicalVerificationId(UUID oldTechnicalVerification,
                                             UUID newTechnicalVerification) {
        boolean duplicated = this.technicalVerification.contains(newTechnicalVerification)
                && !newTechnicalVerification.equals(oldTechnicalVerification);
        if (duplicated) throw new DomainException("Technical verification already exists for this equipment type");

        if (!this.technicalVerification.contains(oldTechnicalVerification)) {
            throw new DomainException("Technical verification to update does not exist");
        }

        this.technicalVerification.remove(oldTechnicalVerification);
        this.technicalVerification.add(newTechnicalVerification);

        EventMetadata metadata = new EventMetadata(
                "events-domain",UUID.randomUUID().toString(), "EquipmentType",
                "technical-verification.updated", 1, Instant.now(), this.id.toString());
        TechnicalVerificationEquipmentPayload tvPayload = new TechnicalVerificationEquipmentPayload(newTechnicalVerification,this.id);

        registerEvent(new TechnicalVerificationEquipmentUpdated(metadata, tvPayload));
    }

    public void removeTechicalVerificationId(UUID technicalVerification) {
        boolean exists = this.technicalVerification.contains(technicalVerification);
        if (!exists) throw new DomainException("Technical verification does not exist for this equipment type");

        this.technicalVerification.remove(technicalVerification);

        EventMetadata metadata = new EventMetadata(
                "events-domain",UUID.randomUUID().toString(), "EquipmentType",
                "technical-verification.removed", 1, Instant.now(), this.id.toString());
        TechnicalVerificationEquipmentPayload tvPayload = new TechnicalVerificationEquipmentPayload(technicalVerification,this.id);

        registerEvent(new TechnicalVerificationEquipmentDeleted(metadata, tvPayload));
    }

    public void addMetrologicalData(MetrologicalData md) {
        boolean duplicated = this.metrologicalData.stream()
                .anyMatch(existing -> existing.getValue().equals(md.getValue())
                        && existing.getType().equals(md.getType()));
        if (duplicated) throw new DomainException("Metrological data already exists for this equipment type");

        this.metrologicalData.add(md);

        EventMetadata metadata = new EventMetadata(
                "events-domain",UUID.randomUUID().toString(), "EquipmentType", "metrologicalData.added", 1, Instant.now(), this.id.toString());
        registerEvent(new MetrologicalDataCreatedEvent(metadata, md));
    }

    public void updateMetrologicalData(MetrologicalData oldData, MetrologicalData newData) {
        boolean duplicated = this.metrologicalData.stream()
                .anyMatch(existing -> !existing.equals(oldData)
                        && existing.getValue().equals(newData.getValue())
                        && existing.getType().equals(newData.getType()));
        if (duplicated) throw new DomainException("The new metrological data conflicts with an existing one");

        int index = this.metrologicalData.indexOf(oldData);
        if (index == -1) throw new DomainException("Metrological data to update does not exist");

        this.metrologicalData.set(index, newData);

        EventMetadata metadata = new EventMetadata("events-domain",UUID.randomUUID().toString(), "EquipmentType", "metrologicalData.updated", 1, Instant.now(), this.id.toString());
        registerEvent(new MetrologicalDataUpdatedEvent(metadata, oldData));
    }

    public void removeMetrologicalData(MetrologicalData md) {
        boolean exists = this.metrologicalData.stream()
                .anyMatch(existing -> existing.getValue().equals(md.getValue())
                        && existing.getType().equals(md.getType()));
        if (!exists) throw new DomainException("Metrological data does not exist for this equipment type");

        this.metrologicalData.removeIf(p -> p.equals(md));

        EventMetadata metadata = new EventMetadata("events-domain",UUID.randomUUID().toString(), "EquipmentType", "metrologicalData.removed", 1, Instant.now(), this.id.toString());
        registerEvent(new MetrologicalDataDeletedEvent(metadata, md));
    }

    public void setMetrologicalData(List<MetrologicalData> metrologicalData) {
        // Copia defensiva para evitar listas inmutables en el agregado.
        this.metrologicalData = metrologicalData == null ? new ArrayList<>() : new ArrayList<>(metrologicalData);
    }

    public void setTechnicalVerification(Set<UUID> technicalVerification) {
        // Copia defensiva para evitar sets inmutables en el agregado.
        this.technicalVerification = technicalVerification == null ? new HashSet<>() : new HashSet<>(technicalVerification);
    }
}
