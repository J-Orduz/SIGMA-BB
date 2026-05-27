package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.events.BrandCreatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.events.BrandDeletedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.events.BrandPayload;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.events.BrandUpdatedEvent;
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
public class Brand extends AggregateRoot {
    private UUID id;
    private String name;

    public static Brand create(String name) {
        Brand brand = Brand.builder()
                .id(UUID.randomUUID())
                .name(name)
                .build();

        EventMetadata metadata = new EventMetadata("events-domain",
                UUID.randomUUID().toString(), "Brand", "brand.created", 1, Instant.now(), brand.id.toString());

        brand.registerEvent(new BrandCreatedEvent(metadata, new BrandPayload(brand.name)));
        return brand;
    }

    public void updateBrand(String name) {
        this.name = name;

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Brand", "brand.updated", 1, Instant.now(), this.id.toString());

        registerEvent(new BrandUpdatedEvent(metadata, new BrandPayload(this.name)));
    }

    public void updateBrandPatch(String name) {
        if (name != null) {
            this.name = name;
        }

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Brand", "brand.patch", 1, Instant.now(), this.id.toString());

        registerEvent(new BrandUpdatedEvent(metadata, new BrandPayload(this.name)));
    }

    public void deleteBrand() {
        EventMetadata metadata = new EventMetadata("events-domain",
                UUID.randomUUID().toString(), "Brand", "brand.deleted", 1, Instant.now(), this.id.toString());

        registerEvent(new BrandDeletedEvent(metadata, new BrandPayload(this.name)));
    }
}
