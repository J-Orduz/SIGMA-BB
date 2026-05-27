package com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.ports.input;

import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.CountryPatchCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.CreateCountryCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.DeleteCountryCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.application.services.country_services.commands.UpdateCountryCommand;
import com.bolivar.bioingenieria.app.sigma_bb.location_hexagon.domain.country.Country;

import java.util.List;

public interface CountryServicePort {
    List<Country> findAll();
    Country findById(String id);
    Country save(CreateCountryCommand createCountryCommand);
    Country update(String id, UpdateCountryCommand updateCountryCommand);
    void delete(DeleteCountryCommand deleteCountryCommand);
    Country patchUpdate(String id, CountryPatchCommand command);
}
