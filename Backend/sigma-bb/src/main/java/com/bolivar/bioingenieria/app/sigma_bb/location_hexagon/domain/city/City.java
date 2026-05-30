package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.events.CityCreatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.events.CityDeletedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.events.CityPayload;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.events.CityUpdatedEvent;
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
public class City extends AggregateRoot {
    private String id;
    private String name;
    private String countryId;

    public static City create(String id, String name, String countryId) {
        City newCity = City.builder()
                .id(id)
                .name(name)
                .countryId(countryId)
                .build();

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "City", "city.created", 1, Instant.now(), newCity.id);

        newCity.registerEvent(new CityCreatedEvent(metadata, new CityPayload(newCity.name, newCity.countryId)));
        return newCity;
    }

    public void updateCity(String id, String name, String countryId) {
        this.name = name;
        this.countryId = countryId;

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "City", "city.updated", 1, Instant.now(), this.id);

        registerEvent(new CityUpdatedEvent(metadata, new CityPayload(this.name, this.countryId)));
    }

    public void updateCityPatch(String name, String countryId) {
        if (name != null) {
            this.name = name;
        }
        if (countryId != null) {
            this.countryId = countryId;
        }

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "City", "city.patch", 1, Instant.now(), this.id);

        registerEvent(new CityUpdatedEvent(metadata, new CityPayload(this.name, this.countryId)));
    }

    public void deleteCity() {
        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "City", "city.deleted", 1, Instant.now(), this.id);

        registerEvent(new CityDeletedEvent(metadata, new CityPayload(this.name, this.countryId)));
    }

    private static String createIdFromName(String name) {
        return name.substring(0, 2).toUpperCase();
    }
}
