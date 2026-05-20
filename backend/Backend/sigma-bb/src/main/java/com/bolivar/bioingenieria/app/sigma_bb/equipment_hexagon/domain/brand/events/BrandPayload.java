package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.brand.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

public record BrandPayload(String name) implements Payload {}
