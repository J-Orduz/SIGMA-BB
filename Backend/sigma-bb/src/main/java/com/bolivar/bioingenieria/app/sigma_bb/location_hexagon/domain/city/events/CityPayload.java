package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.events;

import com.bolivar.bioingenieria.app.sigma_bb.shared.domain.events.Payload;

public record CityPayload(String name, String countryId) implements Payload {}
