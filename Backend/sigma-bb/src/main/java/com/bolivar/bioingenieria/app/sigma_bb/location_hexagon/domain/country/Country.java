package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.events.CountryCreatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.events.CountryDeletedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.events.CountryPayload;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.events.CountryUpdatedEvent;
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
public class Country extends AggregateRoot {
    private String id;
    private String name;

    public static Country create(String id, String name) {
        Country country = Country.builder().id(id).name(name).build();

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Country", "country.created", 1, Instant.now(), country.id);

        country.registerEvent(new CountryCreatedEvent(metadata, new CountryPayload(country.name)));
        return country;
    }

    public void updateCountry(String name) {
        this.name = name;

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Country", "country.updated", 1, Instant.now(), this.id);

        registerEvent(new CountryUpdatedEvent(metadata, new CountryPayload(this.name)));
    }

    public void deleteCountry() {
        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Country", "country.deleted", 1, Instant.now(), this.id);

        registerEvent(new CountryDeletedEvent(metadata, new CountryPayload(this.name)));
    }
}
