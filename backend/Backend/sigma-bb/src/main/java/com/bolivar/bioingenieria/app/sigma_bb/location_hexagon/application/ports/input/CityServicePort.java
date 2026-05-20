package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.CreateCityCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.DeleteCityCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.city_services.commands.UpdateCityCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.city.City;

import java.util.List;

public interface CityServicePort {
    List<City> findAll();
    City findById(String id);
    City save(CreateCityCommand createCityCommand);
    City update(String id, UpdateCityCommand updateCityCommand);
    void delete(DeleteCityCommand deleteCityCommand);
}
