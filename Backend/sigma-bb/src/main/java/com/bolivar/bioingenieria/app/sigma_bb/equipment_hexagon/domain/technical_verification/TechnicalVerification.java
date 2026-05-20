package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.events.TechnicalVerificationCreatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.events.TechnicalVerificationDeletedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.events.TechnicalVerificationPayload;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.events.TechnicalVerificationUpdatedEvent;
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
public class TechnicalVerification extends AggregateRoot {
    private UUID id;
    private String description;
    private String verificationType;

    public static TechnicalVerification create(String description, String verificationType) {
        TechnicalVerification tv = TechnicalVerification.builder()
                .id(UUID.randomUUID())
                .description(description)
                .verificationType(verificationType)
                .build();

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "TechnicalVerification", "technicalVerification.created", 1, Instant.now(), tv.id.toString());

        tv.registerEvent(new TechnicalVerificationCreatedEvent(metadata,
                new TechnicalVerificationPayload(tv.description, tv.verificationType)));
        return tv;
    }

    public void updateTechnicalVerification(String description, String verificationType) {
        this.description = description;
        this.verificationType = verificationType;

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "TechnicalVerification", "technicalVerification.updated", 1, Instant.now(), this.id.toString());

        registerEvent(new TechnicalVerificationUpdatedEvent(metadata,
                new TechnicalVerificationPayload(this.description, this.verificationType)));
    }

    public void deleteTechnicalVerification() {
        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "TechnicalVerification", "technicalVerification.deleted", 1, Instant.now(), this.id.toString());

        registerEvent(new TechnicalVerificationDeletedEvent(metadata, new TechnicalVerificationPayload(this.description, this.verificationType)));
    }
}
