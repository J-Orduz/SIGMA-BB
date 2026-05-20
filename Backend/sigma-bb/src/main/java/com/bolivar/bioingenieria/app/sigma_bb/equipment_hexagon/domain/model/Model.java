package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.events.ModelCreatedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.events.ModelDeletedEvent;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.events.ModelPayload;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.events.ModelUpdatedEvent;
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
public class Model extends AggregateRoot {
    private UUID id;
    private String invima;
    private UUID manufacturerId;
    private UUID equipmentId;

    public static Model create(String invima, String manufacturerId, String equipmentId) {
        Model model = Model.builder()
                .id(UUID.randomUUID())
                .invima(invima)
                .manufacturerId(UUID.fromString(manufacturerId))
                .equipmentId(UUID.fromString(equipmentId))
                .build();

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Model", "model.created", 1, Instant.now(), model.id.toString());

        model.registerEvent(new ModelCreatedEvent(metadata,
                new ModelPayload(model.invima, model.manufacturerId.toString(), model.equipmentId.toString())));
        return model;
    }

    public void updateModel(String invima, String manufacturerId, String equipmentId) {
        this.invima = invima;
        this.manufacturerId = UUID.fromString(manufacturerId);
        this.equipmentId = UUID.fromString(equipmentId);

        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Model", "model.updated", 1, Instant.now(), this.id.toString());

        registerEvent(new ModelUpdatedEvent(metadata,
                new ModelPayload(this.invima, this.manufacturerId.toString(), this.equipmentId.toString())));
    }

    public void deleteModel() {
        EventMetadata metadata = new EventMetadata(
                "events-domain",
                UUID.randomUUID().toString(), "Model", "model.deleted", 1, Instant.now(), this.id.toString());

        registerEvent(new ModelDeletedEvent(metadata, new ModelPayload(this.invima, this.manufacturerId.toString(), this.equipmentId.toString())));
    }
}
