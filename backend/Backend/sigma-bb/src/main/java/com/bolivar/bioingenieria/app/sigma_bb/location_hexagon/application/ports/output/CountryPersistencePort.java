package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.Country;

import java.util.List;
import java.util.Optional;

public interface CountryPersistencePort {
    List<Country> findAll();
    Optional<Country> findById(String id);
    Country save(Country country);
    Country update(String id, Country country);
    void delete(String id);
}
