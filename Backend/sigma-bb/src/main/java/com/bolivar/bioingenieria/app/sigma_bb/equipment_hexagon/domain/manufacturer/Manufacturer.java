package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.events.ManufacturerCreatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.events.ManufacturerDeletedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.events.ManufacturerPayload;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.events.ManufacturerUpdatedEvent;
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
public class Manufacturer extends AggregateRoot {
    private UUID id;
    private String name;
    private String countryId;

    public static Manufacturer create(String name, String countryId) {
        Manufacturer manufacturer = Manufacturer.builder()
                .id(UUID.randomUUID())
                .name(name)
                .countryId(countryId)
                .build();

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Manufacturer", "manufacturer.created", 1, Instant.now(), manufacturer.id.toString());

        manufacturer.registerEvent(new ManufacturerCreatedEvent(metadata,
                new ManufacturerPayload(manufacturer.name, manufacturer.countryId)));
        return manufacturer;
    }

    public void updateManufacturer(String name, String countryId) {
        this.name = name;
        this.countryId = countryId;

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Manufacturer", "manufacturer.updated", 1, Instant.now(), this.id.toString());

        registerEvent(new ManufacturerUpdatedEvent(metadata,
                new ManufacturerPayload(this.name, this.countryId)));
    }

    public void updateManufacturerPatch(String name, String countryId) {
        if (name != null) {
            this.name = name;
        }
        if (countryId != null) {
            this.countryId = countryId;
        }

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Manufacturer", "manufacturer.patch", 1, Instant.now(), this.id.toString());

        registerEvent(new ManufacturerUpdatedEvent(metadata,
                new ManufacturerPayload(this.name, this.countryId)));
    }

    public void deleteManufacturer() {
        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Manufacturer", "manufacturer.deleted", 1, Instant.now(), this.id.toString());

        registerEvent(new ManufacturerDeletedEvent(metadata, new ManufacturerPayload(this.name, this.countryId)));
    }
}
