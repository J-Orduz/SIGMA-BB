package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.model.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

public record ModelPayload(String invima, String manufacturerId, String equipmentId) implements Payload {}
