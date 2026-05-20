package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.manufacturer.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

public record ManufacturerPayload(String name, String countryId) implements Payload {}
