package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment_type.EquipmentType;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.Brand;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.events.EquipmentCreatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.events.EquipmentDeletedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.events.EquipmentPayload;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.equipment.events.EquipmentUpdatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.AggregateRoot;
import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.EventMetadata;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Equipment extends AggregateRoot {
    private UUID id;
    private UUID equipmentTypeId;
    private UUID brandId;
    private EquipmentType equipmentType;
    private Brand brand;

    public static Equipment create(String equipmentTypeId, String brandId) {
        Equipment equipment = Equipment.builder()
                .id(UUID.randomUUID())
                .equipmentTypeId(UUID.fromString(equipmentTypeId))
                .brandId(UUID.fromString(brandId))
                .build();

        EventMetadata metadata = new EventMetadata("events-domain",
                UUID.randomUUID().toString(), "Equipment", "equipment.created", 1, Instant.now(), equipment.id.toString());

        equipment.registerEvent(new EquipmentCreatedEvent(metadata,
                new EquipmentPayload(equipment.equipmentTypeId.toString(), equipment.brandId.toString())));
        return equipment;
    }

    public void updateEquipment(String equipmentTypeId, String brandId) {
        this.equipmentTypeId = UUID.fromString(equipmentTypeId);
        this.brandId = UUID.fromString(brandId);

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Equipment", "equipment.updated", 1, Instant.now(), this.id.toString());

        registerEvent(new EquipmentUpdatedEvent(metadata,
                new EquipmentPayload(this.equipmentTypeId.toString(), this.brandId.toString())));
    }

    public void deleteEquipment() {
        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Equipment", "equipment.deleted", 1, Instant.now(), this.id.toString());
        registerEvent(new EquipmentDeletedEvent(metadata, new EquipmentPayload(this.equipmentTypeId.toString(), this.brandId.toString())));
    }
}
