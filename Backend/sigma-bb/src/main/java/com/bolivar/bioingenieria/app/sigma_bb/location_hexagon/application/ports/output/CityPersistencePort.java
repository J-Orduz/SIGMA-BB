package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.output;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.City;

import java.util.List;
import java.util.Optional;

public interface CityPersistencePort {
    List<City> findAll();
    Optional<City> findById(String id);
    City save(City city);
    City update(String id, City city);
    void delete(String id);
}
