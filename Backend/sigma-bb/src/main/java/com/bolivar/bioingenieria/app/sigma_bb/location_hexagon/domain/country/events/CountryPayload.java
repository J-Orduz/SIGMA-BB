package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

public record CountryPayload(String name) implements Payload {}
